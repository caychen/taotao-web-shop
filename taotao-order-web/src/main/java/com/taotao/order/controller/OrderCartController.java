package com.taotao.order.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.taotao.common.entity.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.entity.TbItem;
import com.taotao.entity.TbUser;
import com.taotao.order.entity.OrderInfo;
import com.taotao.order.service.IOrderService;

@Controller
@RequestMapping("/order")
public class OrderCartController {

	@Value("${COOKIE_CART_KEY}")
	private String cookieCartKey;
	
	@Autowired
	private IOrderService orderService;
	
	@RequestMapping("/order-cart")
	public String showOrderCart(HttpServletRequest request){
		//用户必须是登录状态
		
		//取用户id
		TbUser tbUser = (TbUser) request.getAttribute("user");
		//根据用户信息取收货地址列表，使用静态数据（模拟）
		
		//把收货地址列表传递到页面
		
		//从cookie中取购物车商品列表展示到页面
		List<TbItem> cartItemList = getCartItemList(request);
		request.setAttribute("cartList", cartItemList);
		//返回
		return "order-cart";
	}
	
	private List<TbItem> getCartItemList(HttpServletRequest request) {
		String json = CookieUtils.getCookieValue(request, cookieCartKey, true);
		if (StringUtils.isBlank(json)) {
			return new ArrayList<TbItem>();
		}

		List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
		return list;
	}
	
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public String createOrder(OrderInfo orderInfo, Model model){
		//生成订单
		TaotaoResult result = orderService.createOrder(orderInfo);
		//订单号
		model.addAttribute("orderId", result.getData());
		//总价
		model.addAttribute("payment", orderInfo.getPayment());
		
		//预计送达时间，模拟3天后送达
		DateTime dateTime = new DateTime();
		dateTime = dateTime.plusDays(3);
		model.addAttribute("date", dateTime.toString("yyyy-MM-dd"));
		
		//返回
		return "success";
	}
}
