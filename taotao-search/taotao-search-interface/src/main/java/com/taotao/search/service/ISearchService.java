package com.taotao.search.service;

import com.taotao.common.entity.SearchResult;

public interface ISearchService {

	SearchResult search(String queryString, int page, int rows) throws Exception;
}
