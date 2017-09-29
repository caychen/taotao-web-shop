package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.entity.EasyUIDataGridResult;
import com.taotao.common.entity.TaotaoResult;
import com.taotao.entity.TbItem;
import com.taotao.service.IItemService;

@Controller
@RequestMapping("/item")
public class ItemController {

	@Autowired
	private IItemService itemService;

	@RequestMapping("/{itemId}")
	@ResponseBody
	public TbItem getItemById(@PathVariable Long itemId) {
		return itemService.getItemById(itemId);
	}

	@RequestMapping("/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(int page, int rows) {
		return itemService.getItemList(page, rows);
	}

	@PostMapping("/save")
	@ResponseBody
	public TaotaoResult addItem(TbItem tbItem, String desc) {
		return itemService.addItem(tbItem, desc);
	}
}
