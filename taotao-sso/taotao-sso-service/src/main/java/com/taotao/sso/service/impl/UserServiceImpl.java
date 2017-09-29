package com.taotao.sso.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.taotao.common.entity.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.entity.TbUser;
import com.taotao.entity.TbUserExample;
import com.taotao.entity.TbUserExample.Criteria;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbUserMapper;
import com.taotao.sso.service.IUserService;

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private TbUserMapper userMapper;

	@Autowired
	private JedisClient jedisClient;

	@Value("${TOKEN_PREFIX}")
	private String tokenPrefix;
	@Value("${TOKEN_EXPIRE}")
	private int tokenExpire;

	@Override
	public TaotaoResult checkData(String data, int type) {
		// TODO Auto-generated method stub
		// 执行查询
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		if (type == 1) {
			// 用户名
			criteria.andUsernameEqualTo(data);
		} else if (type == 2) {
			// 手机号
			criteria.andPhoneEqualTo(data);
		} else if (type == 3) {
			// 邮箱
			criteria.andEmailEqualTo(data);
		} else {
			return TaotaoResult.build(400, "存在非法数据");
		}

		List<TbUser> list = userMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return TaotaoResult.ok(false);
		}

		return TaotaoResult.ok(true);
	}

	@Override
	public TaotaoResult register(TbUser user) {
		// TODO Auto-generated method stub
		// 检查数据有效性
		if (StringUtils.isBlank(user.getUsername())) {
			return TaotaoResult.build(400, "用户名不能为空");
		}
		// 判断用户名是否重复
		TaotaoResult result = checkData(user.getUsername(), 1);
		if (!(boolean) result.getData()) {
			return TaotaoResult.build(400, "用户名已存在");
		}

		// 判断密码是否为空
		if (StringUtils.isBlank(user.getPassword())) {
			return TaotaoResult.build(400, "密码不能为空");
		}

		// 手机号可以为空
		if (StringUtils.isNotBlank(user.getPhone())) {
			result = checkData(user.getPhone(), 2);
			if (!(boolean) result.getData()) {
				return TaotaoResult.build(400, "手机号已被注册");
			}
		}
		// 邮箱可以为空
		if (StringUtils.isNotBlank(user.getEmail())) {
			result = checkData(user.getEmail(), 3);
			if (!(boolean) result.getData()) {
				return TaotaoResult.build(400, "邮箱已被注册");
			}
		}

		// md5加密
		user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));

		// 补全属性
		user.setCreated(new Date());
		user.setUpdated(new Date());

		userMapper.insert(user);

		return TaotaoResult.ok();
	}

	@Override
	public TaotaoResult login(String username, String password) {
		// TODO Auto-generated method stub
		// 判断用户名和密码是否正确
		// 密码要进行md5加密
		password = DigestUtils.md5DigestAsHex(password.getBytes());

		TbUserExample example = new TbUserExample();
		example.createCriteria().andUsernameEqualTo(username).andPasswordEqualTo(password);
		List<TbUser> list = userMapper.selectByExample(example);
		if (list == null || list.size() == 0) {
			return TaotaoResult.build(400, "用户名或密码不正确");
		}
		// 生成token，使用uuid
		String token = UUID.randomUUID().toString();

		TbUser user = list.get(0);

		// 把用户信息保存到redis，key就是token，value就是用户信息
		// 存入redis的时候，把密码清掉
		user.setPassword(null);
		jedisClient.set(tokenPrefix + ":" + token, JsonUtils.objectToJson(user));
		// 设置key的过期时间
		jedisClient.expire(tokenPrefix + ":" + token, tokenExpire);

		// 返回登录成功，其中要把token返回
		return TaotaoResult.ok(token);
	}

	@Override
	public TaotaoResult getUserByToken(String token) {
		// TODO Auto-generated method stub

		String json = jedisClient.get(tokenPrefix + ":" + token);
		if (StringUtils.isBlank(json)) {
			return TaotaoResult.build(400, "用户登录已经过期");
		}

		// 重置token的过期时间
		jedisClient.expire(tokenPrefix + ":" + token, tokenExpire);

		TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
		return TaotaoResult.ok(user);
	}

	@Override
	public TaotaoResult logout(String token) {
		// TODO Auto-generated method stub
		jedisClient.expire(tokenPrefix + ":" + token, 0);

		return TaotaoResult.ok();
	}

}
