package com.bdcore.webservice.serve.thrift;

import com.bdcor.pip.core.utils.PropertiesTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.Map.Entry;


public class ThriftService {
	static Logger log = LoggerFactory.getLogger(ThriftService.class);
	static final String PropsFileName = "registerServer.properties";
	
	Map<String,Set<RegisterServer>> serverMap = new HashMap<String,Set<RegisterServer>>();
	public ThriftService(){
		
	}

	@PostConstruct
	public void  init(){
		log.info("启动服务,初始化注册服务器的恢复");
        serverMap = PropertiesTools.readProperties(PropsFileName);

	}

	/**
	 * 注册服务
	 * @param proid
	 * @param ip
	 * @param port
	 */
	public void registerServer(String proid,String ip,String port){
		RegisterServer registerServer = new RegisterServer();
		registerServer.ip = ip;
		registerServer.port = port;
		registerServer.proid = proid;
		registerServer.failedCount = 0;
		
		if(serverMap.get(proid)==null){
			serverMap.put(proid, new HashSet<RegisterServer>());
		}
		
		serverMap.get(proid).add(registerServer);
        PropertiesTools.writeProperties(PropsFileName,ip+":"+port,proid);
		
	}
	
	/**
	 * 取消服务
	 * @param proid
	 * @param ip
	 * @param port
	 */
	public void cancelServer(String proid,String ip,String port){
		for(String key:serverMap.keySet()){
			if(key.equals(proid)){
				Set<RegisterServer> values = serverMap.get(key);
				Iterator<RegisterServer> iter = values.iterator();
				
				while(iter.hasNext()){
					RegisterServer register = iter.next();
					if(register.ip.equals(ip)){
						iter.remove();
					}
				}
				
			}
		}
        PropertiesTools.delProp(PropsFileName , ip+":"+port); // 配置文件中删掉该配置项
	}
	
	public static class RegisterServer{
        public String proid;
        public String ip;
        public String port;
        public int failedCount;
		
		public String toString(){
			return " "+proid+" "+ip+" "+port+" "+failedCount;
		}

		@Override
		public boolean equals(Object obj) {
			if(obj==null){
				return false;
			}
			
			if(!(obj instanceof RegisterServer)){
				return false;
			}
			
			RegisterServer inObj = (RegisterServer)obj;
			// TODO Auto-generated method stub
			return proid.equals(inObj.proid) 
					&& ip.equals(inObj.ip) 
					&& port.equals(inObj.port) ;
		}

		@Override
		public int hashCode() {
			// TODO Auto-generated method stub
			return proid.hashCode()+ip.hashCode()+port.hashCode();
		}
		
		
		
		
	}
	
	public String getServersOfStr(){
		StringBuilder sb = new StringBuilder();
		for(Entry<String, Set<RegisterServer>> entry:serverMap.entrySet()){
			sb.append("[");
			sb.append(entry.getKey());
			sb.append("==>");
			
			for(RegisterServer server:entry.getValue()){
				sb.append(server.toString());
				
				sb.append(",");
			}
			
			sb.append("]");
			
			
		}
		
		return sb.toString();
	}
	
	public static void main(String[] a){
		Set<RegisterServer> set = new HashSet<RegisterServer>();
		
		RegisterServer obj1 = new RegisterServer();
		obj1.ip = "331";
		obj1.port = "33";
		obj1.proid = "33";
		
		System.out.println(obj1.hashCode()+":"+set.add(obj1));
		
		RegisterServer obj2 = new RegisterServer();
		obj2.ip = "33";
		obj2.port = "333";
		obj2.proid = "33";
		System.out.println(obj2.hashCode()+":"+set.add(obj2));
		
		RegisterServer obj4 = new RegisterServer();
		obj4.ip = "3344";
		obj4.port = "333";
		obj4.proid = "33";
		System.out.println(obj4.hashCode()+":"+set.add(obj4));
		
		Iterator<RegisterServer> iter = set.iterator();
		while(iter.hasNext()){
			RegisterServer obj = iter.next();
			if(obj.ip.equals("33")){
				iter.remove();
			}
		}
		
		for(RegisterServer s:set){
			System.out.println(s);
		}
		
		
		
	}

}
