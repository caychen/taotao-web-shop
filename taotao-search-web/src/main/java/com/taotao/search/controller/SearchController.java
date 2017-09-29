package com.taotao.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.common.entity.SearchResult;
import com.taotao.search.service.ISearchService;

@Controller
public class SearchController {

	@Autowired
	private ISearchService searchService;
	
	@Value("${SEARCH_PER_PAGE_OF_ROWS}")
	private int rows;
	
	@RequestMapping("/search")
	public String search(@RequestParam("q") String queryString, @RequestParam(defaultValue="1") int page, Model model){
		try {
			//把查询条件进行转码，解决乱码问题
			queryString = new String(queryString.getBytes("iso-8859-1"), "utf-8");
			
			SearchResult searchResult = searchService.search(queryString, page, rows);
			
			//把结果传递给页面
			model.addAttribute("query", queryString);
			model.addAttribute("totalPages", searchResult.getTotalPages());
			model.addAttribute("itemList", searchResult.getItemList());
			model.addAttribute("page", page);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "search";
	}
}
