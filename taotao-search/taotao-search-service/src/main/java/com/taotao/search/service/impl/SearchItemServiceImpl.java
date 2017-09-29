package com.taotao.search.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.entity.SearchItem;
import com.taotao.common.entity.TaotaoResult;
import com.taotao.search.mapper.ISearchItemMapper;
import com.taotao.search.service.ISearchItemService;

@Service
public class SearchItemServiceImpl implements ISearchItemService {

	@Autowired
	private ISearchItemMapper searchItemMapper;

	@Autowired
	private SolrClient solrClient;

	@Override
	public TaotaoResult importItemsToIndex() {
		// TODO Auto-generated method stub
		try {
			// 1、先查询所有的商品数据
			List<SearchItem> items = searchItemMapper.getItemList();

			// 2、遍历商品数据，添加到索引库
			for (SearchItem searchItem : items) {
				// 3、创建文档对象
				SolrInputDocument document = new SolrInputDocument();

				// 4、向文档中添加域
				document.addField("id", searchItem.getId());
				document.addField("item_title", searchItem.getTitle());
				document.addField("item_sell_point", searchItem.getSell_point());
				document.addField("item_price", searchItem.getPrice());
				document.addField("item_image", searchItem.getImage());
				document.addField("item_category_name", searchItem.getCategory_name());
				document.addField("item_desc", searchItem.getItem_desc());

				// 5、将文档写入索引库
				solrClient.add(document);

			}
			// 6、提交
			solrClient.commit();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return TaotaoResult.build(500, "数据导入失败!");
		}

		// 7、返回
		return TaotaoResult.ok();
	}

}
