package com.taotao.order.service;

import com.taotao.common.entity.TaotaoResult;
import com.taotao.order.entity.OrderInfo;

public interface IOrderService {

	TaotaoResult createOrder(OrderInfo orderInfo);
}
