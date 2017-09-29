package com.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.entity.EasyUITreeNode;
import com.taotao.service.IItemCategoryService;

@Controller
@RequestMapping("/item/cat")
public class ItemCategoryController {

	@Autowired
	private IItemCategoryService itemCategoryService;

	@RequestMapping("/list")
	@ResponseBody
	public List<EasyUITreeNode> getItemCategoryList(@RequestParam(name = "id", defaultValue = "0") long parentId) {
		return itemCategoryService.getItemCategoryList(parentId);
	}
}
