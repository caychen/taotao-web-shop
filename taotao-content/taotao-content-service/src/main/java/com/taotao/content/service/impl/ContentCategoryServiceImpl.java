package com.taotao.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.entity.EasyUITreeNode;
import com.taotao.common.entity.TaotaoResult;
import com.taotao.content.service.IContentCategoryService;
import com.taotao.entity.TbContentCategory;
import com.taotao.entity.TbContentCategoryExample;
import com.taotao.mapper.TbContentCategoryMapper;

@Service
public class ContentCategoryServiceImpl implements IContentCategoryService {

	@Autowired
	private TbContentCategoryMapper tbContentCategoryMapper;

	@Override
	public List<EasyUITreeNode> getContentCategoryList(long parentId) {
		// TODO Auto-generated method stub
		TbContentCategoryExample example = new TbContentCategoryExample();
		example.createCriteria().andParentIdEqualTo(parentId);

		List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(example);

		List<EasyUITreeNode> resultList = new ArrayList<EasyUITreeNode>();
		for (TbContentCategory tbContentCategory : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent() ? "closed" : "open");

			resultList.add(node);
		}

		return resultList;
	}

	@Override
	public TaotaoResult addContentCategory(long parentId, String name) {
		// TODO Auto-generated method stub
		// 创建一个实体对象
		TbContentCategory tbContentCategory = new TbContentCategory();
		// 填充对象属性
		tbContentCategory.setParentId(parentId);
		tbContentCategory.setName(name);
		tbContentCategory.setStatus(1);
		tbContentCategory.setSortOrder(1);
		tbContentCategory.setIsParent(false);
		tbContentCategory.setCreated(new Date());
		tbContentCategory.setUpdated(new Date());
		// 插入数据库
		tbContentCategoryMapper.insert(tbContentCategory);

		// 判断父节点状态
		TbContentCategory parent = tbContentCategoryMapper.selectByPrimaryKey(parentId);
		if (!parent.getIsParent()) {
			// 如果父节点为叶子节点，则应改为父节点
			parent.setIsParent(true);

			tbContentCategoryMapper.updateByPrimaryKey(parent);
		}

		return TaotaoResult.ok(tbContentCategory);
	}

	@Override
	public void updateContentCategory(long id, String name) {
		// TODO Auto-generated method stub
		TbContentCategory category = tbContentCategoryMapper.selectByPrimaryKey(id);

		category.setName(name);
		tbContentCategoryMapper.updateByPrimaryKey(category);

	}

	@Override
	public TaotaoResult deleteContentCategory(long id) {
		// TODO Auto-generated method stub
		TbContentCategory category = tbContentCategoryMapper.selectByPrimaryKey(id);
		long parentId = category.getParentId();
		
		if (!category.getIsParent()) {
			// 叶子节点
			tbContentCategoryMapper.deleteByPrimaryKey(id);
		} else {
			// 父节点
			TbContentCategoryExample example = new TbContentCategoryExample();
			//先查询子节点
			example.createCriteria().andParentIdEqualTo(id);
			tbContentCategoryMapper.deleteByExample(example);
			//删除父节点
			tbContentCategoryMapper.deleteByPrimaryKey(id);
		}
		
		//如果子节点的个数为0，则将父节点变为叶子节点
		TbContentCategoryExample example = new TbContentCategoryExample();
		example.createCriteria().andParentIdEqualTo(parentId);
		List<TbContentCategory> childList = tbContentCategoryMapper.selectByExample(example);
		if(childList.size() == 0){
			TbContentCategory parent = tbContentCategoryMapper.selectByPrimaryKey(parentId);
			parent.setIsParent(false);
			tbContentCategoryMapper.updateByPrimaryKey(parent);
		}
		return TaotaoResult.ok();
	}

}
