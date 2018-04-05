package com.bdcore.webservice.serve.mobile;

public interface IMobileMsgServer {

	String sendMsg(MobileMessage msg) throws Exception;
	
	MobileBackMessage readMsg() throws Exception;
	
	String getChannelNo();
}
