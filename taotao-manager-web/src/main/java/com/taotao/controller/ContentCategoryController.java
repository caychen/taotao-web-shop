package com.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.entity.EasyUITreeNode;
import com.taotao.common.entity.TaotaoResult;
import com.taotao.content.service.IContentCategoryService;

@Controller
@RequestMapping("/content/category")
public class ContentCategoryController {

	@Autowired
	private IContentCategoryService contentCategoryService;

	@RequestMapping("/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCategoryList(@RequestParam(name = "id", defaultValue = "0") long parentId) {
		return contentCategoryService.getContentCategoryList(parentId);
	}

	@RequestMapping("/create")
	@ResponseBody
	public TaotaoResult addContentCategory(long parentId, String name) {
		return contentCategoryService.addContentCategory(parentId, name);
	}

	@RequestMapping("/update")
	@ResponseBody
	public void updateContentCategory(long id, String name) {
		contentCategoryService.updateContentCategory(id, name);
	}

	@RequestMapping("/delete")
	@ResponseBody
	public TaotaoResult deleteContentCategory(long id) {
		return contentCategoryService.deleteContentCategory(id);
	}
}
