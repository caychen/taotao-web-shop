package com.taotao.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.entity.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.entity.TbUser;
import com.taotao.sso.service.IUserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private IUserService userService;

	@Value("${TOKEN_KEY}")
	private String tokenKey;

	@RequestMapping("/check/{param}/{type}")
	@ResponseBody
	public TaotaoResult checkUserData(@PathVariable String param, @PathVariable int type) {
		return userService.checkData(param, type);
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult register(TbUser user) {
		return userService.register(user);
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult login(String username, String password, HttpServletResponse response,
			HttpServletRequest request) {
		TaotaoResult result = userService.login(username, password);

		if (result.getStatus() == 200) {
			// 把token写入cookie
			CookieUtils.setCookie(request, response, tokenKey, result.getData().toString());
		}
		return result;
	}

	/*// jsonp的第一种解决跨域的方法
	 * @RequestMapping(value = "/token/{token}", produces =
	 * MediaType.APPLICATION_JSON_UTF8_VALUE)
	 * 
	 * @ResponseBody public String getUserByToken(@PathVariable String token,
	 * String callback) { TaotaoResult result =
	 * userService.getUserByToken(token);
	 * 
	 * if (StringUtils.isNotBlank(callback)) { return callback + "(" +
	 * JsonUtils.objectToJson(result) + ");"; }
	 * 
	 * return JsonUtils.objectToJson(result); }
	 */

	// jsonp的第二种解决跨域的方法，spring4.1以上版本支持MappingJacksonValue类
	/*@RequestMapping(value = "/token/{token}")
	@ResponseBody
	public Object getUserByToken(@PathVariable String token, String callback) {
		TaotaoResult result = userService.getUserByToken(token);

		if (StringUtils.isNoneBlank(callback)) {
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}
		return result;
	}*/
	
	
	//json跨域问题的第三种解决跨域的方法，springmvc4.2以上版本支持@CrossOrigin注解
	@CrossOrigin(origins="*")
	@RequestMapping(value = "/token/{token}")
	@ResponseBody
	public Object getUserByToken(@PathVariable String token) {
		TaotaoResult result = userService.getUserByToken(token);

		return result;
	}

	@RequestMapping("/logout/{token}")
	@ResponseBody
	public TaotaoResult logout(@PathVariable String token) {
		return userService.logout(token);
	}
}
