## 整体说明
    短信服务端通过http请求，将短信内容等数据提供给短信平台供应商，由短信
    供应商实现短信的发送，并返回回执信息。
    
## 使用方式

   做为独立的服务进行部署
     
   调用方式1：配合mobileClient进行调用
   调用方式2：本身短信服务是一个服务,可以直接通过Http请求实现调用,例如：
   
    @org.junit.Test
    
    public void testClient(){
    
        Client client = ClientBuilder.newClient();
        
        String url = "http://172.31.201.181:3212/myapps/"; // 所在服务地址 myapps为默认应用名称
        
        String path = "mobileMsg/send";
        
        WebTarget target = client.target(url);
        
        String result = target.path(path)
        
                .queryParam("mobileNums", "18551633281")
                
                .queryParam("msg", "短信内容")
                
                .queryParam("linkId",UUID.randomUUID().toString())
                
                .queryParam("sign", "高危筛查项目组")
                
                .queryParam("projNo", "004")
                
                .queryParam("channelNo", "01")
                
                .queryParam("subCode", "03")
                
                .request().get(String.class);
                
        System.out.println(result);
        
    }
   
## 部署
    
    以jar包形式打包部署
    mvn clean install 
    将jar包上传到服务器
    nohup java -jar mobileService.jar &
    
## 注意事项
    服务地址不要配置错误
    registerServer.properties配置文件的形式为：ip:端口号=项目编号

## 版本说明
    客户端的注册信息存储,重启后重新加载