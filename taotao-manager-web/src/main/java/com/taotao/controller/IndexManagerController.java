package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.entity.TaotaoResult;
import com.taotao.search.service.ISearchItemService;

@Controller
@RequestMapping("/index")
public class IndexManagerController {

	@Autowired
	private ISearchItemService searchItemService;

	@RequestMapping("/import")
	@ResponseBody
	public TaotaoResult importIndex() {
		return searchItemService.importItemsToIndex();
	}
}
