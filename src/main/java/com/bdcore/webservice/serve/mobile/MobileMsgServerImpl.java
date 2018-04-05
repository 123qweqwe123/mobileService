package com.bdcore.webservice.serve.mobile;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class MobileMsgServerImpl implements IMobileMsgServer {

	static Logger log = LoggerFactory.getLogger(MobileMsgServerImpl.class);
	
	@Value("${default.channelNo}")
	String defaultChannel;
	
	@Autowired 
	@Qualifier("defaultMobileMsgServiceImp")
	IMobileMsgServer commMsgServer;
	
	@Autowired 
	@Qualifier("hlqxMobileMsgServerImpl")
	IMobileMsgServer hlqxMsgServer;
	
	
	
	
	@Override
	public String sendMsg(MobileMessage msg) throws Exception {
		
		String channelNo = this.defaultChannel;
		if(StringUtils.isNotBlank(msg.getChannelNo())){
			channelNo = msg.getChannelNo();
		}
		
		if(channelNo.equals(hlqxMsgServer.getChannelNo())){
			return hlqxMsgServer.sendMsg(msg);
		}else if(channelNo.equals(commMsgServer.getChannelNo())){
			return commMsgServer.sendMsg(msg);
		}
		
		return null;
	}

	@Override
	public MobileBackMessage readMsg() throws Exception {
		return commMsgServer.readMsg();
	}

	@Override
	public String getChannelNo() {
		// TODO Auto-generated method stub
		return null;
	}

}
