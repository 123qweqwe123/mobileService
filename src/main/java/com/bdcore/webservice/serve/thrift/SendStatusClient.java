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

import com.bdcor.webservice.tooles.sendStateCallBcak.thrift.SendStateService;

@Component
public class SendStatusClient extends ThriftService{
	static Logger log = LoggerFactory.getLogger(SendStatusClient.class);
	
	
	
	public SendStatusClient(){
		
	}
	
	public String sendState(String linkId, String FOrgAddr,String reportCode) {
		StringBuffer sb = new StringBuffer();
		for(String key:serverMap.keySet()){
			Set<RegisterServer> values = serverMap.get(key);
			Iterator<RegisterServer> iter = values.iterator();
			
			while(iter.hasNext()){
				RegisterServer register = iter.next();
				sendStateExec(register,linkId,FOrgAddr+"|"+reportCode);
				if(register.failedCount>=5){
					iter.remove();
					log.info("remove server:"+register);
				}
				sb.append(register+"####");
			}
		}
		return sb.toString();
	}
	
	private void sendStateExec(RegisterServer register,
			String linkId,
			String reportCode){
		TTransport transport = null;
		try {
		  transport = new TSocket(register.ip, Integer.parseInt(register.port), 5000);
		  TProtocol protocol = new TCompactProtocol(transport);
		  SendStateService.Client client = new SendStateService.Client(protocol);
		  transport.open();
		  boolean result = client.sendState(linkId, reportCode);
		  register.failedCount = 0;
		} catch (Exception e) {
			register.failedCount = register.failedCount +1;
			log.error("",e);
		} finally {
		  if (null != transport) {
		    transport.close();
		  }
		}
	}
	
}
