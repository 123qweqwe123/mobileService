package com.example;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.bdcore.webservice.serve.mobile.IMobileMsgServer;
import com.bdcore.webservice.serve.mobile.MobileMessage;
import com.bdcore.webservice.serve.mobile.channel.hlqx.HlqxMobileMsgServerImpl;

public class SpringStart {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		IMobileMsgServer mobileS = ac.getBean(HlqxMobileMsgServerImpl.class);
		
		MobileMessage msg = new MobileMessage();
		
		List<String> l = new ArrayList<String>();
		l.add("18611685308");
		
		
		msg.setMobileNumber(l);
		msg.setMsg("是否按时吃药。请回复，是或者否");
		
		mobileS.sendMsg(msg);
		
		
	}

}
