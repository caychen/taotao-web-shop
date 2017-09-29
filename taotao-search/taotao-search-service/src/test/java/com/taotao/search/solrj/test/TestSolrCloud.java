package com.taotao.search.solrj.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.common.SolrInputDocument;

public class TestSolrCloud {

	public void testSolrCloud() throws Exception{
		//创建zkHost集合
		List<String> zkHosts = new ArrayList<String>();
		zkHosts.add("127.0.0.1:2181");
		zkHosts.add("127.0.0.1:2182");
		zkHosts.add("127.0.0.1:2183");
		
		//创建SolrClient(CloudSolrClient)对象
		CloudSolrClient client = new CloudSolrClient.Builder().withZkHost(zkHosts).build();
		//必须设置默认的collection
		client.setDefaultCollection("collection2");
		
		//创建文档对象
		SolrInputDocument document = new SolrInputDocument();
		
		//向文档中添加域
		document.addField("id", "test001");
		document.addField("item_title", "测试商品名称");
		document.addField("item_price", 100);
		
		client.add(document);
		
		client.commit();
		
	}
}
