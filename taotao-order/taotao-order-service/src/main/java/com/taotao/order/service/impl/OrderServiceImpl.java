package com.taotao.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.entity.TaotaoResult;
import com.taotao.entity.TbOrderItem;
import com.taotao.entity.TbOrderShipping;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbOrderItemMapper;
import com.taotao.mapper.TbOrderMapper;
import com.taotao.mapper.TbOrderShippingMapper;
import com.taotao.order.entity.OrderInfo;
import com.taotao.order.service.IOrderService;

@Service
public class OrderServiceImpl implements IOrderService {

	@Autowired
	private TbOrderMapper orderMapper;

	@Autowired
	private TbOrderItemMapper orderItemMapper;

	@Autowired
	private TbOrderShippingMapper orderShippingMapper;

	@Autowired
	private JedisClient jedisClient;

	@Value("${ORDER_ID_GEN_KEY}")
	private String orderIDKey;

	@Value("${ORDER_ID_BEGIN_VALUE}")
	private String orderIdBegin;

	@Value("${ORDER_ITEM_ID_GEN_KEY}")
	private String orderItemIDKey;

	@Override
	public TaotaoResult createOrder(OrderInfo orderInfo) {
		// TODO Auto-generated method stub
		// 生成订单号,使用redis的incr生成
		if (!jedisClient.exists(orderIDKey)) {
			jedisClient.set(orderIDKey, orderIdBegin);
		}
		String orderId = jedisClient.incr(orderIDKey).toString();

		// 补全属性
		orderInfo.setOrderId(orderId);
		orderInfo.setPostFee("0");// 邮费
		// 状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
		orderInfo.setStatus(1);// 状态
		orderInfo.setCreateTime(new Date());// 创建时间
		orderInfo.setUpdateTime(new Date());// 更新时间

		// 向订单表插入数据
		orderMapper.insert(orderInfo);

		// 向订单明细表插入数据
		List<TbOrderItem> orderItems = orderInfo.getOrderItems();
		for (TbOrderItem tbOrderItem : orderItems) {
			// 获得明细主键
			String orderItemId = jedisClient.incr(orderItemIDKey).toString();
			tbOrderItem.setId(orderItemId);
			tbOrderItem.setOrderId(orderId);
			// 插入明细数据
			orderItemMapper.insert(tbOrderItem);
		}

		// 向订单物流部插入数据
		TbOrderShipping orderShipping = orderInfo.getOrderShipping();
		orderShipping.setOrderId(orderId);
		orderShipping.setCreated(new Date());
		orderShipping.setUpdated(new Date());
		// 插入数据
		orderShippingMapper.insert(orderShipping);

		return TaotaoResult.ok(orderId);
	}

}
