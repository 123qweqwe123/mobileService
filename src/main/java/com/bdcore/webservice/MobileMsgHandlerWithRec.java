package com.bdcore.webservice;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bdcor.pip.core.utils.SpringContextHolder;
import com.bdcor.webservice.tooles.replyCallBcak.thrift.ReplyMsg;
import com.bdcore.webservice.serve.mobile.IMobileMsgServer;
import com.bdcore.webservice.serve.mobile.MobileBackMessage;
import com.bdcore.webservice.serve.mobile.MobileMsgServerImpl;
import com.bdcore.webservice.serve.thrift.ReplyClient;
import com.bdcore.webservice.serve.thrift.SendStatusClient;

@Path("recMsg")
public class MobileMsgHandlerWithRec {
	
	Logger log = LoggerFactory.getLogger(MobileMsgHandlerWithRec.class);
	
	/**
	 * 读取所有已经接受的短信
	 * @return
	 * @throws Exception 
	 */
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("readAll")
	public MobileBackMessage readBackMsg(){
		
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
	 * 接受一条回执短信
	 * @return
	 * @throws Exception 
	 */
	/*
	@PUT
	@Produces(MediaType.TEXT_PLAIN)
    @Path("recOne")
	public String receivedOneMsg(
			@FormParam("channel") String channel,
			@FormParam("tel") String tel,
			@FormParam("content") String content,
			@FormParam("time") String time){
		
		MobileBackMessage.Msg msg = new MobileBackMessage.Msg(tel,content, time);
log.info("rec "+channel+" "+msg);
		
		ReplyMsg regMsg = new ReplyMsg();
		regMsg.setContent(content);
		regMsg.setTel(tel);
		regMsg.setTime(time);
		
		ReplyClient client = SpringContextHolder.getBean(ReplyClient.class);
		client.replyOne(regMsg);

		return "1";
    }
	*/
	/**
	 * 短信发送状态
	 * @param linkId
	 * @param reportCode
	 * @param FUnikey
	 * @param FOrgAddr
	 * @param FDestAddr
	 * @param FSubmitTime
	 * @param FFeeTerminal
	 * @param FServiceUPID
	 * @param FAckStatus
	 * @return
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
    @Path("sendState")
	public String sendState(
			@QueryParam("linkId") String linkId,
			@QueryParam("reportCode") String reportCode,
			@QueryParam("FUnikey") String FUnikey,
			@QueryParam("FOrgAddr") String FOrgAddr,
			@QueryParam("FDestAddr") String FDestAddr,
			@QueryParam("FSubmitTime") String FSubmitTime,
			@QueryParam("FFeeTerminal") String FFeeTerminal,
			@QueryParam("FServiceUPID") String FServiceUPID,
			@QueryParam("FAckStatus") String FAckStatus){
	
		SendStatusClient client = SpringContextHolder.getBean(SendStatusClient.class);
log.info("reply "+linkId+" "+reportCode+" "+ FOrgAddr);
		String toServers = client.sendState(linkId,FOrgAddr,reportCode);
log.info("reply content to server:"+toServers);
		return "1";
    }
	
	/**
	 * 接收到短信
	 * @param channel
	 * @param tel
	 * @param content
	 * @param time
	 * @return
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
    @Path("recOne")
	public String receivedOneMsg(
			@QueryParam("channel") String channel,
			@QueryParam("tel") String tel,
			@QueryParam("content") String content,
			@QueryParam("time") String time){
		
		MobileBackMessage.Msg msg = new MobileBackMessage.Msg(tel,content, time);
log.info("rec "+channel+" "+msg);
		
		ReplyMsg regMsg = new ReplyMsg();
		regMsg.setContent(content);
		regMsg.setTel(tel);
		regMsg.setTime(time);
		
		ReplyClient client = SpringContextHolder.getBean(ReplyClient.class);
		String toServers = client.replyOne(null,regMsg);
log.info("rec content to server:"+toServers);
		return "1";
    }
}
