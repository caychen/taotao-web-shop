package com.taotao.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/*
 * 登录和注册页面的控制器
 */
@Controller
public class PageController {

	@RequestMapping("/page/register")
	public String showRegisterPage() {
		return "register";
	}

	@RequestMapping("/page/login")
	public String showLoginPage(String url, Model model) {
		model.addAttribute("redirect", url);
		return "login";
	}
}
