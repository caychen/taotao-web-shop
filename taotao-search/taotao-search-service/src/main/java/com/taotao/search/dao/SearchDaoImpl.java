package com.taotao.search.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.taotao.common.entity.SearchItem;
import com.taotao.common.entity.SearchResult;

@Repository
public class SearchDaoImpl implements ISearchDao {

	@Autowired
	private SolrClient solrClient;

	@Override
	public SearchResult search(SolrQuery query) throws SolrServerException, IOException {
		// TODO Auto-generated method stub

		// 根据query对象进行查询
		QueryResponse response = solrClient.query(query);

		// 取查询结果
		SolrDocumentList documentList = response.getResults();

		// 取查询结果总记录数
		long numFound = documentList.getNumFound();

		// 将结果封装到SearchResult对象中
		SearchResult result = new SearchResult();
		result.setRecordCount(numFound);

		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();

		List<SearchItem> itemList = new ArrayList<SearchItem>();
		for (SolrDocument solrDocument : documentList) {
			SearchItem item = new SearchItem();
			item.setCategory_name((String) solrDocument.get("item_category_name"));
			item.setId((String) solrDocument.get("id"));
			item.setSell_point((String) solrDocument.get("item_sell_point"));
			item.setPrice((long) solrDocument.get("item_price"));
			
			//如果上传多张图片，数据库或索引库存在的路径是多个以逗号分隔的路径
			String image = (String) solrDocument.get("item_image");
			if(StringUtils.isNoneBlank(image)){
				image = image.split(",")[0];
			}
			
			item.setImage(image);

			// 取高亮显示
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			String itemTitle = "";
			if (list != null && list.size() > 0) {
				itemTitle = list.get(0);
			} else {
				itemTitle = (String) solrDocument.get("item_title");
			}
			item.setTitle(itemTitle);

			itemList.add(item);
		}

		result.setItemList(itemList);
		// 返回
		return result;
	}

}
