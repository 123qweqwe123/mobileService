package com.bdcore.webservice;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bdcor.pip.core.utils.SpringContextHolder;
import com.bdcore.webservice.serve.mobile.IMobileMsgServer;
import com.bdcore.webservice.serve.mobile.MobileBackMessage;
import com.bdcore.webservice.serve.mobile.MobileMessage;
import com.bdcore.webservice.serve.mobile.MobileMsgServerImpl;
import com.bdcore.webservice.serve.thrift.ReplyClient;
import com.bdcore.webservice.serve.thrift.SendStatusClient;

@Path("mobileMsg")
public class MobileMsgHandlerWithClient {
	Logger log = LoggerFactory.getLogger(MobileMsgHandlerWithClient.class);
	//@Autowired
	//private IMobileMsgServer msgService;
	
	/**
	 * 短信发送
	 * @param mobileNums
	 * @param msg
	 * @return
	 */
	/*
	@POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("send")
	public String sendMobileMsg(
			@FormParam("mobileNums") String mobileNums,
			@FormParam("msg") String msg,
			@FormParam("sign") String sign,
			@FormParam("projNo") String projNo,
			@FormParam("channelNo") String channelNo) {
		IMobileMsgServer msgService = SpringContextHolder.getBean(MobileMsgServerImpl.class);
		
		List<String> l = new ArrayList<String>();
		for(String mobilen:mobileNums.split("\\|")){
			l.add(mobilen);
		}
		
		MobileMessage msgBy = new MobileMessage();
		msgBy.setMobileNumber(l);
		msgBy.setMsg(msg);
		msgBy.setChannelNo(channelNo);
		msgBy.setSign(sign);
		msgBy.setProjNo(projNo);
log.info("send:"+msgBy);
		String backMsg;
		try {
			backMsg = msgService.sendMsg(msgBy);
		} catch (Exception e) {
			log.error("短信发送错误", e);
			backMsg = e.getMessage();
		}
log.info("result:"+backMsg);
		
        return backMsg;
    }
	*/
	
	/**
	 * 发送短信
	 * @param mobileNums
	 * @param msg
	 * @param sign
	 * @param projNo
	 * @param channelNo
	 * @param linkId
	 * @return
	 */
	@GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("send")
	public String sendMobileMsg(
			@QueryParam("mobileNums") String mobileNums,
			@QueryParam("msg") String msg,
			@QueryParam("sign") String sign,
			@QueryParam("projNo") String projNo,
			@QueryParam("channelNo") String channelNo,
			@QueryParam("linkId") String linkId,
			@QueryParam("subCode")String subCode) {
		IMobileMsgServer msgService = SpringContextHolder.getBean(MobileMsgServerImpl.class);
		
		List<String> l = new ArrayList<String>();
		for(String mobilen:mobileNums.split("\\|")){
			l.add(mobilen);
		}
		
		MobileMessage msgBy = new MobileMessage();
		msgBy.setMobileNumber(l);
		msgBy.setMsg(msg);
		msgBy.setChannelNo(channelNo);
		msgBy.setSign(sign);
		msgBy.setProjNo(projNo);
		msgBy.setLinkId(linkId);
		msgBy.setSubCode(subCode);
log.info("send:"+msgBy);
		String backMsg;
		try {
			backMsg = msgService.sendMsg(msgBy);
		} catch (Exception e) {
			log.error("短信发送错误", e);
			backMsg = e.getMessage();
		}
log.info("result:"+backMsg);
		
        return backMsg;
    }
	
	
	
	/**
	 * 
	 * @return
	 * @throws Exception 
	 */
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("rec")
	public MobileBackMessage getRecMsg(@QueryParam("fromTime") String fromTime,
			@QueryParam("toTime") String toTime){
		
		MobileBackMessage msgs = new MobileBackMessage();
		IMobileMsgServer msgService = SpringContextHolder.getBean(MobileMsgServerImpl.class);
		try {
			msgs = msgService.readMsg();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return msgs;
    }
	

	/**
	 * 查看可以接受短信发送状态的thrift服务
	 * 
	 * 
	 * http://172.31.131.238:3212/myapps/mobileMsg/seeSendStatusServer
	 * 
	 * @return
	 * @throws Exception 
	 */
	@GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("seeSendStatusServer")
	public String seeServers(){
		
		SendStatusClient client1 = SpringContextHolder.getBean(SendStatusClient.class);
		
		ReplyClient client2 = SpringContextHolder.getBean(ReplyClient.class);
		
		StringBuilder sb = new StringBuilder();
		sb.append("SendStatus:"+client1.getServersOfStr());
		sb.append("\n");
		sb.append("ReplyStatus:"+client2.getServersOfStr());
		
		sb.append("\n");
		sb.append("\n");
		sb.append("\n");
		
		sb.append("SendStatus:http://ip:3212/myapps/mobileMsg/registerSendStatusServer?proid=01&ip=10.24.10.116&port=12303");
		sb.append("\n");
		
		sb.append("ReplyStatus:http://ip:3212/myapps/mobileMsg/registerRecServer?proid=01&ip=10.24.10.116&port=12302");
		sb.append("\n");
		
		
		
        return sb.toString();
    }
	
	/**
	 * 注册短信发送状态服务
	 * @return
	 * @throws Exception 
	 */
	@GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("registerSendStatusServer")
	public String registerSendStatusServer(
			@QueryParam("proid") String proid,
			@QueryParam("ip") String ip,
			@QueryParam("port") String port){
		log.info("register server send status:"+proid+" "+ip+" "+port+" ");
		SendStatusClient client = SpringContextHolder.getBean(SendStatusClient.class);
		client.registerServer(proid, ip, port);
		
        return "1";
    }
	
	/**
	 * 取消短信发送状态服务
	 * @return
	 * @throws Exception 
	 */
	@GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("cancelSendStatusServer")
	public String cancelSendStatusServer(
			@QueryParam("proid") String proid,
			@QueryParam("ip") String ip,
			@QueryParam("port") String port){
		log.info("cancel server send status:"+proid+" "+ip+" "+port+" ");
		SendStatusClient client = SpringContextHolder.getBean(SendStatusClient.class);
		client.cancelServer(proid, ip, port);
		
        return "1";
    }
	
	
	
	/**
	 * 注册短信接收服务
	 * @return
	 * @throws Exception 
	 */
	@GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("registerRecServer")
	public String registerRecServer(
			@QueryParam("proid") String proid,
			@QueryParam("ip") String ip,
			@QueryParam("port") String port){
		log.info("register server rec:"+proid+" "+ip+" "+port+" ");
		ReplyClient client = SpringContextHolder.getBean(ReplyClient.class);
		client.registerServer(proid, ip, port);
		
        return "1";
    }
	
	/**
	 * 取消短信接收服务
	 * @return
	 * @throws Exception 
	 */
	@GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("cancelRecServer")
	public String cancelRecServer(
			@QueryParam("proid") String proid,
			@QueryParam("ip") String ip,
			@QueryParam("port") String port){
		log.info("cancel server rec:"+proid+" "+ip+" "+port+" ");
		ReplyClient client = SpringContextHolder.getBean(ReplyClient.class);
		client.cancelServer(proid, ip, port);
		
        return "1";
    }
	
}
