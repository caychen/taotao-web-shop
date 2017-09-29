package com.taotao.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.entity.SearchResult;
import com.taotao.search.dao.ISearchDao;
import com.taotao.search.service.ISearchService;

@Service
public class SearchServiceImpl implements ISearchService {

	@Autowired
	private ISearchDao searchDao;

	@Override
	public SearchResult search(String queryString, int page, int rows) throws Exception {
		// TODO Auto-generated method stub
		//根据查询条件封装查询对象
		
		//创建SolrQuery对象
		SolrQuery query = new SolrQuery();
		
		//设置查询条件
		query.setQuery(queryString);
		
		//设置分页条件
		page = page < 1 ? 1 : page;
		query.setStart((page - 1) * rows);
		query.setRows(rows);
		
		//设置默认搜索域
		query.set("df", "item_title");
		
		//设置高亮显示
		query.setHighlight(true);
		query.addHighlightField("item_title");
		
		query.setHighlightSimplePre("<font color='red'>");
		query.setHighlightSimplePost("</font>");
		
		//调用dao执行查询
		SearchResult searchResult = searchDao.search(query);
		
		//计算查询结果的总页数
		long recordCount = searchResult.getRecordCount();
		long pages = recordCount / rows;
		if(recordCount % rows != 0){
			pages++;
		}

		searchResult.setTotalPages(pages);
		//返回结果
		return searchResult;
	}

}
