package com.taotao.content.service;

import java.util.List;

import com.taotao.common.entity.TaotaoResult;
import com.taotao.entity.TbContent;

public interface IContentService {

	TaotaoResult addContent(TbContent tbContent);

	List<TbContent> getContentByCid(long cid);

	TaotaoResult updateContentByCid(TbContent tbContent);

	TaotaoResult deleteContentsByCids(String ids);
}
