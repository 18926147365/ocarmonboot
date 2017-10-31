package com.ocarmon.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import com.aliyun.oss.OSSClient;

/**
 * @author 李浩铭
 *
 * @time 2017年10月23日 下午9:12:09
 */
public class AliyunOSSClient {

	private static String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";
	private static String accessKeyId = "LTAIwP5b0ptQhpfl";
	private static String accessKeySecret = "wKDgoHbPC896xBfei9ivkFL99ERBV8";

	public static void uploadImage(String url,String fileName) {
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		// 构造URL
		// 打开连接
		try {
			InputStream inputStream = new URL(url).openStream();
			ossClient.putObject("myocarmon", "ocarmon/zhihu/headimg/"+fileName+".jpg", inputStream);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			ossClient.shutdown();
		}
		// 输入流
	}
	public static void main(String[] args) {
		uploadImage("https://pic2.zhimg.com/f94273606fc2fd6763514a4a51bd14e5_xl.jpg","123");
	}
}
