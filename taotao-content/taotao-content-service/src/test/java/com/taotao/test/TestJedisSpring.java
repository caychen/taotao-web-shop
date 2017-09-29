package com.taotao.test;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.taotao.jedis.JedisClient;


public class TestJedisSpring {

	private ApplicationContext ac;

	@Before
	public void before() {
		ac = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
	}

	@Test
	public void testJedisClient() {
		JedisClient bean = ac.getBean(JedisClient.class);
		bean.set("jedis-pool-key", "jedis-pool-value");

		System.out.println(bean.get("jedis-pool-key"));
	}
	
}
