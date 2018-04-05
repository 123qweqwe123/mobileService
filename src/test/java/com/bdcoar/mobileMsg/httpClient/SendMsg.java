package com.bdcoar.mobileMsg.httpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class SendMsg {

	
	
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
	public static void main2(String[] a) throws IOException{
		
		String ips = "18611685308";
		String msg = "【牛津中心】通知：请按时吃药";
		
		
		
		
		
		String url = "http://q.hl95.com:8061/";
		
		Map<String,String> params = new HashMap<String,String>();
		params.put("username", "fwyy");
		params.put("password", "fwyy123");
		params.put("epid", "121124");
		params.put("linkid", "");
		params.put("subcode", "");
		
		params.put("phone", ips);
		params.put("message", msg);
		
		System.out.println("发送消息");
		
		
		List<NameValuePair> urlParams = new ArrayList<NameValuePair>();
		for(Map.Entry<String, String> entity:params.entrySet()){
			urlParams.add(new BasicNameValuePair(entity.getKey(),entity.getValue()));
		}
		
		
		
		
		String responseBody = null;
		

		String requetUrl = url + "?" + URLEncodedUtils.format(urlParams, "gb2312");
		
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
			System.out.println(responseBody);
		} finally {
			httpclient.close();
		}
		
	}
	
public static void main(String[] a) throws IOException{
		
		
		
		
		
		//String url = "http://114.242.126.168:8983/mobilecallback/mobileCallbackServlet";
	String url = "http://localhost:8983/mobilecallback/mobileCallbackServlet";
	
		Map<String,String> params = new HashMap<String,String>();
		params.put("msgContent", "中文");
		
		
		System.out.println("发送消息");
		
		
		List<NameValuePair> urlParams = new ArrayList<NameValuePair>();
		for(Map.Entry<String, String> entity:params.entrySet()){
			urlParams.add(new BasicNameValuePair(entity.getKey(),entity.getValue()));
		}
		
		
		
		
		String responseBody = null;
		

		String requetUrl = url + "?" + URLEncodedUtils.format(urlParams, "UTF-8");
		
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
			System.out.println(responseBody);
		} finally {
			httpclient.close();
		}
		
	}
}
