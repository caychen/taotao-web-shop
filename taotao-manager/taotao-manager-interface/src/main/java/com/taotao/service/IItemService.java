package com.taotao.service;

import com.taotao.common.entity.EasyUIDataGridResult;
import com.taotao.common.entity.TaotaoResult;
import com.taotao.entity.TbItem;
import com.taotao.entity.TbItemDesc;

public interface IItemService {

	public TbItem getItemById(long itemId);

	public EasyUIDataGridResult getItemList(int page, int rows);

	TaotaoResult addItem(TbItem tbItem, String desc);
	
	TbItemDesc getItemDescById(long itemId);
}
