package com.jt.sso.controller;

import javax.persistence.PostRemove;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.SysResult;
import com.jt.sso.pojo.User;
import com.jt.sso.service.UserService;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	//实现用户的检验 http://sso.jt.com/user/check/{param}/{type}
	@RequestMapping("/check/{param}/{type}")
	@ResponseBody
	public MappingJacksonValue checkUser(@PathVariable String param,
			@PathVariable Integer type,String callback){
		//查询后台数据 返回结果信息 true 信息已存在
		boolean flag = userService.findCheckUser(param,type);
		MappingJacksonValue jacksonValue = 
				new MappingJacksonValue(SysResult.oK(flag));
		jacksonValue.setJsonpFunction(callback);
		return jacksonValue;
	}
	
	
	@RequestMapping("/register")
	@ResponseBody
	public SysResult saveUser(User user){
		try {
			userService.saveUser(user);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201, "用户新增失败");
	}
	
	
	//实现用户登录
	@RequestMapping("/login")
	@ResponseBody
	public SysResult findUserByUP(User user){
		try {
			//获取服务端返回的token数据
			String token=userService.findUserByUP(user);
			
			
			if(StringUtils.isEmpty(token)){
				throw new RuntimeException();
			}
			
			
			
			
			
			
			
		return SysResult.oK();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return SysResult.build(201, "用户查询失败");
	}
	
	
	
	
	
	//登录成功友好提示
	@Autowired
	private JedisCluster jedisCluster;
	//通过ticket获取用户信息
	@RequestMapping("/query/{ticket}")
	@ResponseBody
	public MappingJacksonValue findUserByTicket(@PathVariable String ticket
			,String callback){
		MappingJacksonValue jacksonValue;
		try {
			String userJson=jedisCluster.get(ticket);
			
			if(!StringUtils.isEmpty(userJson)){
				
				/**
				 * 问题:是否需要将userjson转化为user对象返回
				 * 不需要,因为页面js解析是已经处理了
				 */
			 jacksonValue=
						new MappingJacksonValue(callback);
				
				return jacksonValue;
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		jacksonValue =
				new MappingJacksonValue(SysResult.build(201, "用户查询失败"));
		
		jacksonValue.setJsonpFunction(callback);
		return jacksonValue;
	}
	
	
	
}
