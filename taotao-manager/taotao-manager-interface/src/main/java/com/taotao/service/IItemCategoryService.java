package com.taotao.service;

import java.util.List;

import com.taotao.common.entity.EasyUITreeNode;

public interface IItemCategoryService {

	List<EasyUITreeNode> getItemCategoryList(long parentId);
}
