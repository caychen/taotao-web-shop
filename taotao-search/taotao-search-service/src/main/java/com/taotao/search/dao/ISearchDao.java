package com.taotao.search.dao;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;

import com.taotao.common.entity.SearchResult;

public interface ISearchDao {

	SearchResult search(SolrQuery query) throws SolrServerException, IOException;
}
