package com.taotao.portal.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.common.utils.JsonUtils;
import com.taotao.content.service.IContentService;
import com.taotao.entity.TbContent;
import com.taotao.portal.entity.ADNode;

@Controller
public class IndexController {

	@Autowired
	private IContentService contentService;
	
	@Value("${AD_CATEGORY_ID}")
	private Long AD_CATEGORY_ID;
	
	@Value("${AD_WIDTH}")
	private Integer AD_WIDTH;
	@Value("${AD_WIDTH_B}")
	private Integer AD_WIDTH_B;
	@Value("${AD_HEIGHT}")
	private Integer AD_HEIGHT;
	@Value("${AD_HEIGHT_B}")
	private Integer AD_HEIGHT_B;
	
	@RequestMapping("/index")
	public String showIndex(Model model){
		//根据cid查询轮播图片内容
		List<TbContent> list = contentService.getContentByCid(AD_CATEGORY_ID);
		//把列表转换为ADNode列表
		List<ADNode> nodes = new ArrayList<ADNode>();
		for (TbContent tbContent : list) {
			ADNode node = new ADNode();
			node.setHeight(AD_HEIGHT);
			node.setHeightB(AD_HEIGHT_B);
			node.setWidth(AD_WIDTH);
			node.setWidthB(AD_WIDTH_B);
			node.setSrc(tbContent.getPic());
			node.setSrcB(tbContent.getPic2());
			node.setHref(tbContent.getUrl());
			nodes.add(node);
		}
		
		String adJson = JsonUtils.objectToJson(nodes);
		
		model.addAttribute("ad", adJson);
		return "index";
	}
	
}
