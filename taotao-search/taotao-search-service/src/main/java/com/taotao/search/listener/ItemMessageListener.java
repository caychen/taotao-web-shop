package com.taotao.search.listener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.taotao.common.entity.SearchItem;
import com.taotao.search.mapper.ISearchItemMapper;

/*
 * 监听商品添加事件
 */
public class ItemMessageListener implements MessageListener {

	@Autowired
	private ISearchItemMapper searchItemMapper;
	
	@Autowired
	private SolrClient solrClient;
	
	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		try{
			//从消息中取商品id
			TextMessage textMessage = (TextMessage) message;
			long itemId = Long.parseLong(textMessage.getText());
			
			//因为在manager-service添加item的时候，还没等事务提交的时候，message已经被发送出去了，listener就监听到添加的事件了。
			//如果在还未提交事务之前就去查询该item，就会得到null的对象，所以为了等待添加item的事务提交，在此处停留1秒钟
			Thread.sleep(1000);
			
			//根据商品id查询数据，取商品信息
			SearchItem searchItem = searchItemMapper.getItemById(itemId);
			
			//创建文档对象
			SolrInputDocument document = new SolrInputDocument();
			
			//向文档对象中添加域
			document.addField("id", searchItem.getId());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSell_point());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCategory_name());
			document.addField("item_desc", searchItem.getItem_desc());

			//把文档对象写入索引库
			solrClient.add(document);
			
			//提交
			solrClient.commit();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
