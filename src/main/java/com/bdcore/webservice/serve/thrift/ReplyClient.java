package com.bdcore.webservice.serve.thrift;

import java.util.Iterator;
import java.util.Set;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bdcor.webservice.tooles.replyCallBcak.thrift.IReplyService;
import com.bdcor.webservice.tooles.replyCallBcak.thrift.ReplyMsg;

@Component
public class ReplyClient extends ThriftService{
	static Logger log = LoggerFactory.getLogger(ReplyClient.class);
	
	
	
	public ReplyClient(){
		
	}
	
	/**
	 * 	回复短信到客户端
	 * @param proid   项目id，项目id为null,通知所有短信回复
	 * @param msg     回复的手机号及内容
	 */
	public String replyOne(String proid,ReplyMsg msg){
		StringBuffer sb = new StringBuffer();
		for(String key:serverMap.keySet()){
			if(proid==null || key.equals(proid)){
				Set<RegisterServer> values = serverMap.get(key);
				Iterator<RegisterServer> iter = values.iterator();
				
				while(iter.hasNext()){
					RegisterServer register = iter.next();
					replyOne(register,msg);
					if(register.failedCount>=5){
						iter.remove();
						log.info("remove server:"+register);
					}
					sb.append(register+"####");
				}
			}
		}
		return sb.toString();
	}
	
	private void replyOne(RegisterServer server,ReplyMsg msg){
		TTransport transport = null;
		try {
		  transport = new TSocket(server.ip, Integer.parseInt(server.port), 5000);
		  TProtocol protocol = new TCompactProtocol(transport);
		  IReplyService.Client client = new IReplyService.Client(protocol);
		  transport.open();
		  boolean result = client.replyOneMsg(msg);
		  server.failedCount = 0;
		} catch (Exception e) {
			server.failedCount = server.failedCount +1;
			log.error("",e);
			
			
		} finally {
		  if (null != transport) {
		    transport.close();
		  }
		}
	}
	


}
