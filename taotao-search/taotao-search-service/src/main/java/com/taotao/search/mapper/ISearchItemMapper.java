package com.taotao.search.mapper;

import java.util.List;

import com.taotao.common.entity.SearchItem;

public interface ISearchItemMapper {

	List<SearchItem> getItemList();
	
	SearchItem getItemById(long id);
}
