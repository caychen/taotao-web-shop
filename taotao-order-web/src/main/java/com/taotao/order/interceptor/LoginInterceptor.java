package com.taotao.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.entity.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.entity.TbUser;
import com.taotao.sso.service.IUserService;

/*
 * 判断用户是否登录的拦截器
 */
public class LoginInterceptor implements HandlerInterceptor {

	@Value("${TOKEN_KEY}")
	private String tokenKey;
	
	@Value("${SSO_LOGIN_URL}")
	private String ssoLoginUrl;
	
	@Autowired
	private IUserService userService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		//执行handler之前先执行此方法
		//返回true：放行， 返回false：拦截
		
		//1、从cookie中取token
		String token = CookieUtils.getCookieValue(request, tokenKey);
		
		//2、如果取不到token，跳转到sso的登录页面，需要把当前请求的url作为参数传递给sso，当sso登录成功之后再跳转到当前结算页面
		if(StringUtils.isBlank(token)){
			//跳转到登录页面
			response.sendRedirect(ssoLoginUrl + "/page/login?url=" + request.getRequestURL());
			//拦截
			return false;
		}
		//3、如果取到token，调用sso系统的服务判断用户是否登录
		TaotaoResult result = userService.getUserByToken(token);
		//4、如果用户未登录，即未取到用户信息，跳转到登录页面
		if(result.getStatus() != 200){
			//跳转到登录页面
			response.sendRedirect(ssoLoginUrl + "/page/login?url=" + request.getRequestURL());
			//拦截
			return false;
		}
		
		//5、如果取到用户信息，放行。
		
		//把用户信息放到request中
		TbUser tbUser = (TbUser) result.getData();
		request.setAttribute("user", tbUser);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		//handler执行之后，ModelAndView返回之前
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		//ModelAndView返回之后，异常处理
	}

}
