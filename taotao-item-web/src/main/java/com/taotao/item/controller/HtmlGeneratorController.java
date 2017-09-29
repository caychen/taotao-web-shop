package com.taotao.item.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;

/*
 * 网页静态化控制器
 */
@Controller
public class HtmlGeneratorController {

	String path = "F:/Code/Maven/Taotao/taotao-item-web/src/main/webapp/";
	
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	@RequestMapping("/generator")
	@ResponseBody
	public String generatorHtml() throws Exception{
		Configuration configuration = freeMarkerConfigurer.getConfiguration();
		Template template = configuration.getTemplate("hello.ftl");
		Map<String, Object> data = new HashMap<>();
		data.put("hello", "Spring freemarker");
		Writer out = new FileWriter(new File(path + "/tmp/hello.html"));
		
		template.process(data, out);
		
		out.close();
		
		return "OK";
	}
}
