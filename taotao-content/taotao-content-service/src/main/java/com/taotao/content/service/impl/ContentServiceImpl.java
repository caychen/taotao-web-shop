package com.taotao.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.entity.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.content.service.IContentService;
import com.taotao.entity.TbContent;
import com.taotao.entity.TbContentExample;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbContentMapper;

@Service
public class ContentServiceImpl implements IContentService {

	@Autowired
	private TbContentMapper tbContentMapper;

	@Autowired
	private JedisClient jedisClient;

	@Value("${INDEX_CONTENT}")
	private String INDEX_CONTENT;

	@Override
	public TaotaoResult addContent(TbContent tbContent) {
		// TODO Auto-generated method stub

		tbContent.setCreated(new Date());
		tbContent.setUpdated(new Date());

		tbContentMapper.insert(tbContent);

		// 同步缓存，即删除对应的缓存信息
		jedisClient.hdel(INDEX_CONTENT, tbContent.getCategoryId().toString());

		return TaotaoResult.ok();
	}

	// 用于前台访问首页的时候
	@Override
	public List<TbContent> getContentByCid(long cid) {
		// TODO Auto-generated method stub
		// 先查询缓存，不能影响正常的业务逻辑
		try {
			// 如果在缓存中查询到了
			String json = jedisClient.hget(INDEX_CONTENT, cid + "");

			if (StringUtils.isNoneBlank(json)) {
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				return list;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// 缓存中没有命中，查询数据库

		TbContentExample example = new TbContentExample();
		example.createCriteria().andCategoryIdEqualTo(cid);
		List<TbContent> list = tbContentMapper.selectByExample(example);

		// 将结果添加到缓存
		try {
			jedisClient.hset(INDEX_CONTENT, cid + "", JsonUtils.objectToJson(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 返回结果
		return list;
	}

	// 后台管理系统的更新功能
	@Override
	public TaotaoResult updateContentByCid(TbContent tbContent) {
		// TODO Auto-generated method stub
		tbContent.setUpdated(new Date());
		tbContentMapper.updateByPrimaryKey(tbContent);

		// 同步缓存，即删除对应的缓存信息
		jedisClient.hdel(INDEX_CONTENT, tbContent.getCategoryId().toString());

		return TaotaoResult.ok();
	}

	// 后台管理系统的删除功能
	@Override
	public TaotaoResult deleteContentsByCids(String ids) {
		// TODO Auto-generated method stub
		String[] idsArray = ids.split(",");

		// 找到父节点
		Long parentId = tbContentMapper.selectByPrimaryKey(Long.parseLong(idsArray[0])).getCategoryId();

		for (String id : idsArray) {
			tbContentMapper.deleteByPrimaryKey(Long.parseLong(id));
		}

		// 同步缓存，即删除对应的缓存信息
		jedisClient.hdel(INDEX_CONTENT, parentId.toString());

		return TaotaoResult.ok();
	}

}
