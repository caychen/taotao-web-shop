package com.taotao.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.entity.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.entity.TbItem;
import com.taotao.service.IItemService;

@Controller
@RequestMapping("/cart")
public class CartController {

	@Value("${COOKIE_CART_KEY}")
	private String cookieCartKey;

	@Value("${COOKIE_CART_EXPIRE}")
	private Integer cookieCartExpire;

	@Autowired
	private IItemService itemService;

	@RequestMapping("/add/{itemId}")
	public String addCart(@PathVariable long itemId, @RequestParam(defaultValue = "1") Integer num,
			HttpServletRequest request, HttpServletResponse response) {
		// 取购物车商品列表
		List<TbItem> cartItemList = getCartItemList(request);

		// 判断商品在购物车是否存在
		boolean flag = false;
		for (TbItem tbItem : cartItemList) {
			if (tbItem.getId().longValue() == itemId) {
				// 如果存在数量相加
				flag = true;
				tbItem.setNum(tbItem.getNum() + num);
				break;
			}
		}

		// 如果不存在，添加一个新的商品
		if (!flag) {
			// 需要调用服务取商品信息
			TbItem tbItem = itemService.getItemById(itemId);
			// 设置购买的商品数量
			tbItem.setNum(num);
			// 取一张图片
			String image = tbItem.getImage();
			if (StringUtils.isNotBlank(image)) {
				tbItem.setImage(image.split(",")[0]);
			}
			// 加入购物车
			cartItemList.add(tbItem);
		}

		// 把购物车列表写入cookie
		CookieUtils.setCookie(request, response, cookieCartKey, JsonUtils.objectToJson(cartItemList), cookieCartExpire,
				true);

		// 返回页面视图
		return "cartSuccess";
	}

	private List<TbItem> getCartItemList(HttpServletRequest request) {
		String json = CookieUtils.getCookieValue(request, cookieCartKey, true);
		if (StringUtils.isBlank(json)) {
			return new ArrayList<TbItem>();
		}

		List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
		return list;
	}

	@RequestMapping("/cart")
	public String showCartList(HttpServletRequest request) {
		List<TbItem> cartItemList = getCartItemList(request);

		request.setAttribute("cartList", cartItemList);
		return "cart";
	}

	@RequestMapping("/update/num/{itemId}/{num}")
	@ResponseBody
	public TaotaoResult updateItemNum(@PathVariable long itemId, @PathVariable Integer num, HttpServletRequest request,
			HttpServletResponse response) {
		// 取购物车商品列表
		List<TbItem> cartItemList = getCartItemList(request);

		for (TbItem tbItem : cartItemList) {
			if (tbItem.getId().longValue() == itemId) {
				// 修改购买数量
				tbItem.setNum(num);
				break;
			}
		}

		// 写入cookie
		CookieUtils.setCookie(request, response, cookieCartKey, JsonUtils.objectToJson(cartItemList), cookieCartExpire,
				true);

		return TaotaoResult.ok();
	}
	
	@RequestMapping("/delete/{itemId}")
	public String deleteCartItem(@PathVariable long itemId, HttpServletRequest request, HttpServletResponse response){
		// 取购物车商品列表
		List<TbItem> cartItemList = getCartItemList(request);

		for (TbItem tbItem : cartItemList) {
			if(tbItem.getId().longValue() == itemId){
				cartItemList.remove(tbItem);
				break;
			}
		}
		// 写入cookie
		CookieUtils.setCookie(request, response, cookieCartKey, JsonUtils.objectToJson(cartItemList), cookieCartExpire,
				true);
		
		return "redirect:/cart/cart.html";
	}
}
