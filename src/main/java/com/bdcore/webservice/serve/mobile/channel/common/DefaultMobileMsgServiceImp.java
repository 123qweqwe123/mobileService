package com.bdcore.webservice.serve.mobile.channel.common;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bdcore.webservice.serve.mobile.IMobileMsgServer;
import com.bdcore.webservice.serve.mobile.MobileBackMessage;
import com.bdcore.webservice.serve.mobile.MobileMessage;
import com.kszit.util.ssh2upload.SSH2UploadFileUtil;

@Service
public class DefaultMobileMsgServiceImp implements IMobileMsgServer{

	Logger log = LoggerFactory.getLogger(getClass());
	
	@Value("${msgback.ip}")
	String msgbackIp;
	
	@Value("${msgback.path}")
	String msgbackPath;
	
	@Value("${msgback.user}")
	String msgbackUser;
	
	
	@Value("${msgback.pwd}")
	String msgbackPwd;
	

	
	
	@Override
	public String sendMsg(MobileMessage msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MobileBackMessage readMsg() throws Exception {
		
		
		SSH2UploadFileUtil uploadfileUtil = new SSH2UploadFileUtil(
				"",
				//"/usr/local/tomcat/webapps/datserver/upgradePackage/006",
				this.msgbackIp,
				"22",
				this.msgbackUser,
				this.msgbackPwd);
		
		List<String> logs = uploadfileUtil.dirLs(this.msgbackPath);
		
		MobileBackMessage backMsgs = new MobileBackMessage();
		for(String logM:logs){
			
			byte[] byteData = null;
			try {
				byteData = uploadfileUtil.loadFile(this.msgbackPath+"/"+logM);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
			BufferedReader br = new BufferedReader(new InputStreamReader(bais));
			
			String line = null;
			while((line=br.readLine())!=null){
				String[] aa = line.substring("2016-03-07 11:35:30 ".length()).split("		")[1].split(":");
				String t = line.substring(0, "2016-03-07 11:35:30 ".length()-1);
				
				backMsgs.getMsgs().add(new MobileBackMessage.Msg(aa[0], aa[1],t));
				backMsgs.setCount(backMsgs.getCount()+1);
				
				
			}
			
			
			
		}
		
		
		return backMsgs;
	}

	@Override
	public String getChannelNo() {
		return null;
	}

}
