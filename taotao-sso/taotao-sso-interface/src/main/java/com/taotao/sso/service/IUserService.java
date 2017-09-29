package com.taotao.sso.service;

import com.taotao.common.entity.TaotaoResult;
import com.taotao.entity.TbUser;

public interface IUserService {

	TaotaoResult checkData(String data, int type);
	
	TaotaoResult register(TbUser user);
	
	TaotaoResult login(String username, String password);
	
	TaotaoResult getUserByToken(String token);
	
	TaotaoResult logout(String token);
}
