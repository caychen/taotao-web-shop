package com.taotao.search.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/*
 * 全局异常处理器，需要在spring配置文件中配置该bean
 */
public class GlobalExceptionResolver implements HandlerExceptionResolver {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionResolver.class);
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		// TODO Auto-generated method stub
		logger.info("进入全局异常处理器");
		
		logger.debug("handler类型：" + handler.getClass());
		//控制台打印异常
		ex.printStackTrace();
		
		//向日志文件中写入异常
		logger.error("系统发生异常", ex);
		
		//发邮件 :jmail
		
		//发短信 :短信接口
		
		//展示错误页面
		ModelAndView mv = new ModelAndView();
		mv.addObject("message", "系统发生异常，请稍后重试");
		mv.setViewName("error/exception");
		return mv;
	}

}
