package com.taotao.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.entity.TbItem;
import com.taotao.entity.TbItemDesc;
import com.taotao.item.entity.Item;
import com.taotao.service.IItemService;

@Controller
@RequestMapping("/item")
public class ItemController {

	@Autowired
	private IItemService itemService;

	@RequestMapping("/{itemId}")
	public String showItem(@PathVariable long itemId, Model model) {
		// 取商品信息
		TbItem tbItem = itemService.getItemById(itemId);
		Item item = new Item(tbItem);

		// 取商品详情
		TbItemDesc tbItemDesc = itemService.getItemDescById(itemId);

		// 把数据传递给页面
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", tbItemDesc);

		// 返回页面
		return "item";
	}
}
