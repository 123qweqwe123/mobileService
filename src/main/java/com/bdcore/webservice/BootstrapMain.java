package com.bdcore.webservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.bdcore.webservice.serve.ServeMain;
import com.bdcore.webservice.serve.mobile.MobileMessage;

public class BootstrapMain {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		ServeMain mobileS = ac.getBean(ServeMain.class);
		
		mobileS.startServer();
		
		System.in.read();
		
		mobileS.stopServer();
		
		
		
		
		
	}

}
