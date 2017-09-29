package com.taotao.pagehelper;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.entity.TbItem;
import com.taotao.entity.TbItemExample;
import com.taotao.mapper.TbItemMapper;

public class TestPageHelper {

	@Test
	public void testPageHelper(){
		//1、先在mybatis配置文件中配置分页插件
		
		//2、在执行查询之前配置分页条件，使用PageHelper的静态方法
		PageHelper.startPage(1, 10);
		
		//3、执行查询
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
		TbItemMapper tbItemMapper = ctx.getBean(TbItemMapper.class);
		
		//创建Example对象
		TbItemExample example = new TbItemExample();
		/*Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(value);*/
		
		List<TbItem> list = tbItemMapper.selectByExample(example);
		
		//4、取分页信息，使用PageInfo对象
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		System.out.println("查询总记录数："+ pageInfo.getTotal());
		System.out.println("查询总页数："+ pageInfo.getPages());
		System.out.println("返回总记录数："+ pageInfo.getPageSize());
	}
}
