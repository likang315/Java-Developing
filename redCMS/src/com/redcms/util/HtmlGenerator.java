package com.redcms.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.apache.log4j.lf5.util.StreamUtils;

/** 
* 静态页面引擎技术 
* 
*/   
public class HtmlGenerator{   
   private static Logger log = Logger.getLogger(HtmlGenerator.class);  
   private static  CloseableHttpClient httpclient = HttpClients.createDefault();
   /**
    * 根据url生成静态文件
    * @param url
    * @param filename
    */
   public static void createHtmlPage(String url,String filename)
   {
	   
	   HttpGet httpget = new HttpGet(url);
	   CloseableHttpResponse response =null; 
	   try {
		   response =httpclient.execute(httpget);
		   HttpEntity entity = response.getEntity();
		 
		    if (entity != null) {
		        InputStream instream = entity.getContent();
		        FileOutputStream fo= new FileOutputStream(filename);
		        try {
		            // do something useful
		        	StreamUtils.copy(instream,fo);
		  
		        	//IOUtils.copy(instream, new FileWriter(filename));
		        } finally {
		        	fo.close();
		            instream.close();
		        }
		    }
	   }catch(Exception e)
	   {
		   log.error("com.redcms.util.HtmlGenerator_ERROR_56_生成静态化出错！"+e);
		   
	   }finally {
	       try {
			response.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   }
   }
 //测试方法    
   public static void main(String[] args){   
        
	   HtmlGenerator.createHtmlPage("http://localhost:8080/redcms-web/web/showContent?id=4&channel_id=5&t_name=data1","c:/a.html");   
       System.out.println("静态页面已经生成到c:/a.html");   
          
   }   
}   