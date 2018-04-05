package com.bdcor.pip.core.utils;


import com.bdcore.webservice.serve.thrift.ThriftService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;


public class PropertiesTools {

    private static final Logger log = LoggerFactory.getLogger(PropertiesTools.class);

    /**
     * 根据key读取value
     * @param filePath
     * @param key
     * @return
     */
    public static String readValue(String filePath,String key) {
        Properties props = new Properties();
        try {
            InputStream in = new BufferedInputStream (new FileInputStream(filePath));
            props.load(in);
            String value = props.getProperty (key);
            in.close();
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 读取properties的全部信息
     * @param filePath
     */
    public static Map<String,Set<ThriftService.RegisterServer>> readProperties(String filePath) {
        log.info("查看文件全路径"+new File(filePath).getAbsolutePath());
        Map<String,Set<ThriftService.RegisterServer>> m = new HashMap<String,Set<ThriftService.RegisterServer>>();
        if(!new File(filePath).exists() ){
            return m;
        }
        Properties props = new Properties();
        try {
            InputStream in = new BufferedInputStream (new FileInputStream(filePath));
            props.load(in);
            Enumeration en = props.propertyNames();
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();
                String[] propArr = key.split(":");
                ThriftService.RegisterServer rs = new ThriftService.RegisterServer();
                rs.ip = propArr[0];
                rs.port = propArr[1];
                rs.failedCount = 0;
                rs.proid = props.getProperty (key);
                if( m.get(rs.proid) == null ){
                    m.put(rs.proid,new HashSet<ThriftService.RegisterServer>());
                }
                m.get(rs.proid).add(rs);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return m;
    }

    /**
     * 追加写入properties信息
     * @param filePath
     * @param parameterName
     * @param parameterValue
     */
    public static void writeProperties(String filePath,String parameterName,String parameterValue) {
        Properties prop = new Properties();
        try {
            if(!new File(filePath).exists()){ new File(filePath).createNewFile(); }

            InputStream fis = new FileInputStream(filePath);
            //从输入流中读取属性列表（键和元素对）
            prop.load(fis);
            OutputStream fos = new FileOutputStream(filePath);
            prop.setProperty(parameterName, parameterValue);
            prop.store(fos, " 写配置文件 Update '" + parameterName + "' value");
            fis.close();
            fos.close();
        } catch (IOException e) {
            System.err.println("读取 "+filePath+" for updating "+parameterName+" value error");
        }
    }

    public static boolean delProp(String filePath , String propKey ){
        Properties prop = new Properties();
        try {
            if(!new File(filePath).exists()){ return false; }
            InputStream fis = new FileInputStream(filePath);
            //从输入流中读取属性列表（键和元素对）
            prop.load(fis);
            OutputStream fos = new FileOutputStream(filePath);
            prop.remove(propKey);
            prop.store(fos, "删除注册服务 '" + propKey + "' value");
            fis.close();
            fos.close();
        } catch (IOException e) {
            System.err.println("读取 "+filePath+" for 删除注册服务 "+propKey+" value error");
        }
        return false;
    }

}
