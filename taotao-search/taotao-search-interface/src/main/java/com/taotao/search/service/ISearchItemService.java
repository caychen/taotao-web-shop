package com.taotao.search.service;

import com.taotao.common.entity.TaotaoResult;

public interface ISearchItemService {

	// 将数据库里的数据导入solr的索引库
	TaotaoResult importItemsToIndex();
}
