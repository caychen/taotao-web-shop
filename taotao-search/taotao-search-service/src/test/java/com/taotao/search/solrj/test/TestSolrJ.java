package com.taotao.search.solrj.test;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrJ {

	private final static String SOLR_URL = "http://localhost:8079/solr/collection1";
	
	@Test
	public void testAddDocument() throws Exception{
		//创建SolrServer(低版本)对象或者SolrClient(高版本)对象
		//需要指定solr服务的url
		SolrClient client = new HttpSolrClient.Builder(SOLR_URL).build();
				
		//创建文档对象SolrInputDocument
		SolrInputDocument document = new SolrInputDocument();
		
		//向文档中添加域，必须有id域，域的名称必须在schema.xml中定义
		document.addField("id", "test002");
		document.addField("item_title", "测试商品2");
		document.addField("item_price", 2000L);
		
		//把文档对象写入索引库
		client.add(document);
		
		//提交
		client.commit();
	}
	
	@Test
	public void testDeleteDocumentById() throws Exception{
		SolrClient client = new HttpSolrClient.Builder(SOLR_URL).build();
		
		client.deleteById("test001");
		
		client.commit();
	}
	
	@Test
	public void testDeleteDocumentByQuery() throws Exception{
		SolrClient client = new HttpSolrClient.Builder(SOLR_URL).build();
		
		client.deleteByQuery("*:*");//*:*表示所有的
		//client.deleteByQuery("id:test001");//删除id为test001的文档
		//client.deleteByQuery("item_title:测试商品1");//由于item_title进行了分词，所以凡是查询到"测试","商品"的都会删除
		
		client.commit();
	}
	
	@Test
	public void searchDocument() throws Exception{
		//创建SolrServer(低版本)对象或者SolrClient(高版本)对象
		SolrClient client = new HttpSolrClient.Builder(SOLR_URL).build();
		
		//创建SolrQuery对象
		SolrQuery query = new SolrQuery();
		
		//设置查询条件、过滤条件、分页条件、排序条件、高亮
		//query.setQuery("*:*");//等价于下一行
		//query.set("q", "*:*");

		query.setQuery("手机");

		//设置分页
		//query.set("start", 30);//等价下一行
		query.setStart(30);
		
		//query.set("rows", 20);//等价下一行
		query.setRows(20);
		
		//设置默认搜索域
		query.set("df", "item_keywords");
		
		//设置高亮
		//query.set("hl", true);//等价下一行
		query.setHighlight(true);
		
		//设置高亮显示的域
		//query.set("hl.fl", "item_title");//等价下一行
		query.addHighlightField("item_title");
		
		//设置高亮显示的格式
		//query.set("hl.simple.pre", "<em>");//等价下一行
		query.setHighlightSimplePre("<em>");
		
		//query.set("hl.simple.post", "</em>");//等价下一行
		query.setHighlightSimplePost("</em>");
		
		//执行查询，得到Response对象
		QueryResponse response = client.query(query);
		
		//取查询结果
		SolrDocumentList documentList = response.getResults();
		
		//查询结果总记录数
		System.out.println(documentList.getNumFound());
		
		//取高亮域
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		
		for (SolrDocument solrDocument : documentList) {
			System.out.println(solrDocument.get("id"));
			
			//title取高亮
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			String itemTitle = "";
			if(list != null && list.size() > 0){
				itemTitle = list.get(0);
			}else{
				itemTitle = (String) solrDocument.get("item_title");
			}
			System.out.println(itemTitle);
			
			System.out.println(solrDocument.get("item_sell_point"));
			System.out.println(solrDocument.get("item_price"));
			System.out.println(solrDocument.get("item_image"));
			System.out.println(solrDocument.get("item_category_name"));
			System.out.println("=================================");
		}
	}
	
}
