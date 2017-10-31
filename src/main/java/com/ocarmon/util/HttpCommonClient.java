package com.ocarmon.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 */
public class HttpCommonClient {

	public static String httpClientUtil(String url,String requestBody){
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = null;
		String result=null;
		try {
			httpPost = new HttpPost(url);
			httpPost.addHeader("Content ", "text/xml,charset=UTF-8");
			httpPost.setEntity(new StringEntity(requestBody));
			
			CloseableHttpResponse httpResponse = httpclient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			InputStream responseBodyAsStream = entity.getContent();
			
			result = IOUtils.toString(responseBodyAsStream, "UTF-8");
			
//			byte[] buf = new byte[2048];
//			StringBuffer sb = new StringBuffer();
//			try {
//				for(int len=0;(len=responseBodyAsStream.read(buf))!=-1;){
//					sb.append(new String(buf, 0, len,"UTF-8"));
//				}
//			} catch (Exception e) {
//				System.out.println("报错啦。。。。。。。。。");
//			}
//			
			return result;
		} catch (Exception e) {
			if(httpPost!=null){
				httpPost.abort();
			}
			e.printStackTrace();
		} finally{
			try {
				if(httpclient!=null){
					httpclient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	
	
	

}
