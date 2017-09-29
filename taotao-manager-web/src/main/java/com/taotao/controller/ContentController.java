package com.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.entity.TaotaoResult;
import com.taotao.content.service.IContentService;
import com.taotao.entity.TbContent;

@Controller
@RequestMapping("/content")
public class ContentController {

	@Autowired
	private IContentService contentService;

	@RequestMapping("/save")
	@ResponseBody
	public TaotaoResult addContent(TbContent tbContent) {
		return contentService.addContent(tbContent);
	}

	@RequestMapping("/list")
	@ResponseBody
	public List<TbContent> getContentList(long categoryId) {
		List<TbContent> list = contentService.getContentByCid(categoryId);
		return list;
	}

	@RequestMapping("/edit")
	@ResponseBody
	public TaotaoResult updateContentByCid(TbContent tbContent) {
		TaotaoResult result = contentService.updateContentByCid(tbContent);
		return result;
	}

	@RequestMapping("/delete")
	@ResponseBody
	public TaotaoResult deleteContentsByCids(String ids) {
		TaotaoResult result = contentService.deleteContentsByCids(ids);
		return result;
	}
}
