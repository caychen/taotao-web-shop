package com.taotao.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.entity.EasyUIDataGridResult;
import com.taotao.common.entity.TaotaoResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.entity.TbItem;
import com.taotao.entity.TbItemDesc;
import com.taotao.entity.TbItemExample;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.service.IItemService;

@Service
public class ItemServiceImpl implements IItemService {

	@Autowired
	private TbItemMapper tbItemMapper;

	@Autowired
	private TbItemDescMapper tbItemDescMapper;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Resource(name = "topicDestination")
	private Destination destination;

	@Autowired
	private JedisClient jedisClient;

	@Value("${ITEM_INFO}")
	private String itemInfo;
	@Value("${ITEM_TIME_EXPIRE}")
	private Integer itemTimeExpire;

	@Override
	public TbItem getItemById(long itemId) {
		// TODO Auto-generated method stub
		// 先查询缓存
		try {
			String json = jedisClient.get(itemInfo + ":" + itemId + ":BASE");
			if (StringUtils.isNoneBlank(json)) {
				TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
				return tbItem;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// 缓存中没有
		TbItem item = tbItemMapper.selectByPrimaryKey(itemId);

		// 把查询结果添加到缓存中
		try {
			jedisClient.set(itemInfo + ":" + itemId + ":BASE", JsonUtils.objectToJson(item));
			// 设置过期时间，提高缓存利用率
			jedisClient.expire(itemInfo + ":" + itemId + ":BASE", itemTimeExpire);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}

	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		// TODO Auto-generated method stub
		// 设置分页信息
		PageHelper.startPage(page, rows);

		TbItemExample example = new TbItemExample();
		List<TbItem> list = tbItemMapper.selectByExample(example);

		PageInfo<TbItem> pageInfo = new PageInfo<>(list);

		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal(pageInfo.getTotal());
		result.setRows(list);

		return result;
	}

	@Override
	public TaotaoResult addItem(TbItem tbItem, String desc) {
		// TODO Auto-generated method stub
		// 生成商品ID
		long itemId = IDUtils.genItemId();
		tbItem.setId(itemId);
		// 补全item对象的其他属性字段
		tbItem.setStatus((byte) 1);
		tbItem.setCreated(new Date());
		tbItem.setUpdated(new Date());

		// 向商品表中插入数据
		tbItemMapper.insert(tbItem);

		// 创建一个商品描述表对应的对象
		TbItemDesc tbItemDesc = new TbItemDesc();

		// 补全描述表对应的属性字段
		tbItemDesc.setItemId(itemId);
		tbItemDesc.setItemDesc(desc);
		tbItemDesc.setCreated(new Date());
		tbItemDesc.setUpdated(new Date());

		// 向商品描述表中插入数据
		tbItemDescMapper.insert(tbItemDesc);

		// 向activemq发送商品添加事件
		jmsTemplate.send(destination, new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				// TODO Auto-generated method stub
				return session.createTextMessage(itemId + "");
			}
		});

		// 返回结果
		return TaotaoResult.ok();
	}

	@Override
	public TbItemDesc getItemDescById(long itemId) {
		// TODO Auto-generated method stub
		// 先查询缓存
		try {
			String json = jedisClient.get(itemInfo + ":" + itemId + ":DESC");
			if (StringUtils.isNoneBlank(json)) {
				TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return tbItemDesc;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// 缓存中没有
		TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);

		// 把查询结果添加到缓存中
		try {
			jedisClient.set(itemInfo + ":" + itemId + ":DESC", JsonUtils.objectToJson(tbItemDesc));
			// 设置过期时间，提高缓存利用率
			jedisClient.expire(itemInfo + ":" + itemId + ":DESC", itemTimeExpire);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tbItemDesc;
	}

}
