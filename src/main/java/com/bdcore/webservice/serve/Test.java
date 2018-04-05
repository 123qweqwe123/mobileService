package com.bdcore.webservice.serve;

import com.bdcore.webservice.serve.mobile.MobileMessage;
import com.bdcore.webservice.serve.mobile.MobileMsgServerImpl;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by 低调式男 on 2017/5/22.
 * 测试用例
 */

@RunWith(SpringJUnit4ClassRunner.class) //用于配置spring中测试的环境
@ContextConfiguration(locations="/applicationContext.xml") //用于指定配置文件所在的位置
public class Test {

    @Autowired
    private MobileMsgServerImpl sendMsgTools;

    @org.junit.Test
    public void test() throws Exception {
        MobileMessage msg = new  MobileMessage();
        msg.setLinkId(UUID.randomUUID().toString());
        msg.setChannelNo("01");
        msg.setMsg("[消息内容]测试");
        List list = new ArrayList<String>();
        list.add("18551633281");
        msg.setMobileNumber(list);
        msg.setProjNo("007");

//        msg.setSign("测试消息前缀");
//        msg.setSubCode("03");

        sendMsgTools.sendMsg(msg);

    }



    @org.junit.Test
    public void testClient(){
        Client client = ClientBuilder.newClient();
        String url = "http://172.31.201.181:3212/myapps/";
        String path = "mobileMsg/send";
        WebTarget target = client.target(url);
        String result = target.path(path)
                .queryParam("mobileNums", "18551633281")
                .queryParam("msg", "短信内容")
                .queryParam("linkId",UUID.randomUUID().toString())
                .queryParam("sign", "高危筛查项目组")
                .queryParam("projNo", "004")
                .queryParam("channelNo", "01")
//                .queryParam("subCode", "03")
                .request().get(String.class);
        System.out.println(result);
    }

    @org.junit.Test
    public void test3() throws InterruptedException {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://cp.discuz.qq.com/cloud/appOpen?a_id=3&s_id=62268185&s_site_uid=1&sig=87de668bbff65cb8b48df50c0c0bb0b7");
        for( int i = 0 ; i< 100 ; i++ ){

            String result = target.request().get(String.class);

            if( result.contains("系统繁忙，请稍后再试</h4>") ){
                System.out.println("系统繁忙，请稍后再试 次数:" + i);
            }else{
                System.out.println(result);
            }
            Thread.sleep(100L);
        }


    }

    @org.junit.Test
    public void t(){
        System.out.println(this.getClass().getClassLoader().getResource(""));
        System.out.println(System.getProperty("user.dir"));
    }

}
