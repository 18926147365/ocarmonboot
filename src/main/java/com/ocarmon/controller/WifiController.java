package com.ocarmon.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ocarmon.dao.UserDao;
import com.ocarmon.entity.User;

/**
 * @author 李浩铭
 * @since 2017年7月31日 下午2:04:29
 */
@Controller
@EnableAutoConfiguration
@Transactional
@RequestMapping("/wifi")
public class WifiController  {
	

	@RequestMapping(value = "/lhm")
	@ResponseBody
	String lhm() {
		JSONObject json=new JSONObject();
		json.put("password", "454!@^##&44asqAqlk()(+4a11(0''4564++===");
		json.put("name", "Li family");
		return json.toString();
	}
}
