package com.bdcore.webservice.serve.mobile.channel.hlqx;

import com.bdcore.webservice.serve.mobile.IMobileMsgServer;
import com.bdcore.webservice.serve.mobile.MobileBackMessage;
import com.bdcore.webservice.serve.mobile.MobileMessage;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*
 * 
 * 
 * url参数：
 *  
 * phone	要发送的手机号码	电话号码
	message	短信内容	信息内容
 * 
 * username	用户名	鸿联分配
	password	系统访问接口密码	鸿联分配
	
	epid	企业id	鸿联分配
	linkid	备用	唯一ID，可为空
	subcode	扩展小号	可为空

 * 
 * 
 * 短信调用返回值：
 * 
 * 00	提交成功
	1	参数不完整
	2	鉴权失败（包括：用户状态不正常、密码错误、用户不存在、地址验证失败，黑户）
	3	号码数量超出50条
	4	发送失败
	5	余额不足
	6	发送内容含屏蔽词
	7	短信内容超出350个字数 

 */
@Service
public class HlqxMobileMsgServerImpl implements IMobileMsgServer {

	Logger log = LoggerFactory.getLogger(HlqxMobileMsgServerImpl.class);
	
	@Value("${h1qx.channelNo}")
	private String channelNo;
	
	@Value("${hlqx.url}")
	private String url;
	
	@Value("${hlqx.username}")
	private String username;
	
	@Value("${hlqx.password}")
	private String password;
	
	@Value("${hlqx.epid}")
	private String epid;
	
	@Value("${hlqx.subcode}")
	private String subcode;



	@Value("${hlqx.msgBegin}")
	private String msgBegin;
	
	@Value("${hlqx.canContainBegin}")
	private String canContainBegin;

	private String getIps(MobileMessage msg){
		
		if(msg.getMobileNumber()==null){
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		for(String ip:msg.getMobileNumber()){
			sb.append(ip+",");
		}
		
		sb.deleteCharAt(sb.length()-1);
		
		return sb.toString();
	}
	private String getMsg(MobileMessage msg){
		if(org.apache.commons.lang3.StringUtils.isEmpty(msg.getMsg())){
			return null;
		}
	
		if(StringUtils.isNotBlank(canContainBegin) && Boolean.parseBoolean(canContainBegin)){
			String sign = this.msgBegin;
			if(StringUtils.isNotBlank(msg.getSign())){
				sign = msg.getSign();
			}
			return "【"+sign+"】"+msg.getMsg();
		}else{
			return msg.getMsg();
		}
	}
	
	@Override
	public String sendMsg(MobileMessage msg) throws Exception {
		
		String ips = this.getIps(msg);
		if(StringUtils.isEmpty(ips)){
			throw new Exception("手机号不能为空");
		}
		
		String userMsg = msg.getMsg();
		if(StringUtils.isEmpty(userMsg)){
			throw new Exception("短信内容不能为空");
		}
		if(userMsg.contains("【")||userMsg.contains("】")){
			throw new Exception("短信内容不能包括【或】");
		}
		if(userMsg.length()>350){
			throw new Exception("短信内容不能超过350个字符");
		}
		
		String sendMsg = "【"+ (StringUtils.isEmpty(msg.getSign()) ? this.msgBegin : msg.getSign() )+"】" +  msg.getMsg() ;//this.getMsg(msg);

		HlqxSendMsg hlqxMsg = new HlqxSendMsg();
		hlqxMsg.iphones = ips;
		hlqxMsg.msg = sendMsg;
		hlqxMsg.linkId = msg.getLinkId();
        hlqxMsg.subcode = StringUtils.isEmpty(msg.getSubCode()) ? "01" : msg.getSubCode();
		
		try {
			return sendMsg(hlqxMsg);
		} catch (IOException e) {
			throw new Exception("发送短信发生错误:"+e.getMessage());
		}
	}
	
	private String sendMsg(HlqxSendMsg msg) throws IOException{
		String url = this.url;
		
		Map<String,String> params = new HashMap<String,String>();
		params.put("username", this.username);
		params.put("password", this.password);
		params.put("epid", this.epid);
		params.put("linkid" , msg.linkId);
		params.put("subcode" , msg.subcode);
		
		params.put("phone", msg.iphones);
		params.put("message", msg.msg);
		
		
		
		List<NameValuePair> urlParams = new ArrayList<NameValuePair>();
		for(Map.Entry<String, String> entity:params.entrySet()){
			urlParams.add(new BasicNameValuePair(entity.getKey(),entity.getValue()));
		}
		
		
		
		
		String responseBody = null;
		

		String requetUrl = url + "?" + URLEncodedUtils.format(urlParams, "utf-8");
		log.debug("调用接口:"+requetUrl);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpget = new HttpGet(requetUrl);
			
			httpget.setHeader(new BasicHeader("Connection","close"));
			
			//setHttpConfig(httpget);
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

				public String handleResponse(final HttpResponse response)
						throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity)
								: null;
					} else {
						
						throw new ClientProtocolException(
								"Unexpected response status: " + status);
					}
				}

			};
			
			try {
				responseBody = httpclient.execute(httpget, responseHandler);
			} catch (ClientProtocolException e) {
				throw new IOException("http连接错误<br>"+e.getMessage()+"url:"+url,e);
			}
			//System.out.println(responseBody);
		} finally {
			httpclient.close();
		}
		
		return (responseBody!=null&&"00".equals(responseBody))?"成功":"失败:"+responseBody;
	}
	@Override
	public MobileBackMessage readMsg() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getChannelNo() {
		return this.channelNo;
	}

}
