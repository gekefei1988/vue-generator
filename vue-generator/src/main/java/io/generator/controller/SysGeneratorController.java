/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.generator.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;

import io.generator.service.SysGeneratorService;

/**
 * 代码生成器
 */
@Controller
@RequestMapping("/sys/generator")
public class SysGeneratorController {
	
	private static final Logger log = LoggerFactory.getLogger(SysGeneratorController.class);
	
	@Autowired
	private SysGeneratorService sysGeneratorService;
	

	/**
	 * 生成代码
	 */
	@RequestMapping("/code")
	public void code(String tables, HttpServletResponse response) throws IOException{
		byte[] data = sysGeneratorService.generatorCode(tables.split(","));
		
		response.reset();  
        response.setHeader("Content-Disposition", "attachment; filename=\"" + tables + ".zip\"");  
        response.addHeader("Content-Length", "" + data.length);  
        response.setContentType("application/octet-stream; charset=UTF-8");  
  
        IOUtils.write(data, response.getOutputStream());  
	}
	
	/**
	 * 生成vue前端文件
	 * @throws IOException 
	 */
	@RequestMapping("/create")
	public void create(HttpServletRequest request,HttpServletResponse response) throws IOException{
	
		PrintWriter out = response.getWriter();
		String json = request.getParameter("construction");
		JSONObject jsonObj = JSONObject.parseObject(json);
		System.out.println(jsonObj);
		try {
			sysGeneratorService.createCode(jsonObj);
		} catch (Exception e) {
			
			e.printStackTrace();
			out.print("<script>parent.callback('1')</script>");
			
			return;
		}
		out.print("<script>parent.callback('0')</script>");
	}
}
