package com.ocarmon.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.alibaba.fastjson.JSONObject;

/** 
* @author 李浩铭 
* @since 2017年7月31日 下午4:44:27
*/
@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(value = Exception.class)
	 public void ErrorHandler(HttpServletRequest req,HttpServletResponse response, Exception e) throws Exception {
		JSONObject json=new JSONObject();
		json.put("success", false);
		json.put("message", "系统出现异常");
		response.getWriter().write(json.toString());
		e.printStackTrace();
	 }
}
