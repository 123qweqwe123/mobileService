package com.bdcoar.mobileMsg;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.bdcore.webservice.serve.ServeMain;

public class MobileMsgTest {

    private ServeMain mobileS;
    private WebTarget target;

   // @Before
    public void setUp() throws Exception {
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		mobileS = ac.getBean(ServeMain.class);
		mobileS.startServer();
		
		Client c = ClientBuilder.newClient();

        target = c.target(mobileS.BASE_URI);
    }

   // @After
    public void tearDown() throws Exception {
    	mobileS.stopServer();
    }

   // @Test
    public void testSendMsg() {

    	String responseMsg = target.path("mobileMsg/send")
    			//.queryParam("mobileNums", "18610706997")
        		.queryParam("mobileNums", "18611685308")
        		.queryParam("msg", "请按时吃药。若按时吃药，请回复：是，否则回复：否。")
        		.request().get(String.class);
        
        assertEquals("OK", responseMsg);
    }
    
    //@Test
    public void testReadMsg() {

    	String responseMsg = target.path("mobileMsg/read")
        		.request().get(String.class);
        
        
        System.out.println(responseMsg);
    }
    
    //@Test
    public void testRecMsg() {
    	 Form  form = new Form();
         form.param("tel","18611685308");
         form.param("content","段兴呢鞥"); 
         form.param("time","2016-10-22 12:33");
         
         String back = target.path("recMsg/recOne")
         .request()
         .put(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE),String.class);
        
         System.out.println(back);
       
        
       
    }
}
