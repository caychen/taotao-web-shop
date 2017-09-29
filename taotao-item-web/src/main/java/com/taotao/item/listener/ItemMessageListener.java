package com.taotao.item.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.taotao.entity.TbItem;
import com.taotao.entity.TbItemDesc;
import com.taotao.item.entity.Item;
import com.taotao.service.IItemService;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class ItemMessageListener implements MessageListener {

	@Autowired
	private IItemService itemService;

	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;

	@Value("${STATIC_HTML_OUT_PATH}")
	private String staticHtmlOutPath;

	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		try {
			// 1、从消息中获取商品id
			TextMessage textMessage = (TextMessage) message;
			long itemId = Long.parseLong(textMessage.getText());

			// 因为在manager-service添加item的时候，还没等事务提交的时候，message已经被发送出去了，listener就监听到添加的事件了。
			// 如果在还未提交事务之前就去查询该item，就会得到null的对象，所以为了等待添加item的事务提交，在此处停留1秒钟
			Thread.sleep(1000);

			// 2、根据商品id查询商品信息及描述
			TbItem tbItem = itemService.getItemById(itemId);
			Item item = new Item(tbItem);

			TbItemDesc tbItemDesc = itemService.getItemDescById(itemId);

			// 3、使用freemarker生成静态页面
			Configuration configuration = freeMarkerConfigurer.getConfiguration();

			Template template = configuration.getTemplate("item.ftl");
			//
			Map<String, Object> data = new HashMap<>();
			data.put("item", item);
			data.put("itemDesc", tbItemDesc);

			Writer out = new FileWriter(new File(staticHtmlOutPath + itemId + ".html"));

			template.process(data, out);

			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
