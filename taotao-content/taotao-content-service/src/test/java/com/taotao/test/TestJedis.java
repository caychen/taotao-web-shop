package com.taotao.test;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class TestJedis {

	@Test
	public void testJedis() {
		// 创建一个jedis对象
		Jedis jedis = new Jedis("localhost", 6379);

		jedis.set("jedis-key", "1234");

		System.out.println(jedis.get("jedis-key"));

		jedis.close();
	}

	@Test
	public void testJedisPool() {
		JedisPool jedisPool = new JedisPool("localhost", 6379);
		Jedis jedis = jedisPool.getResource();
		jedis.set("key", "value");
		System.out.println(jedis.get("key"));
		jedis.close();
		jedisPool.close();
	}
	
	@Test
	public void testJedisCluster(){
		//创建一个JedisCluster对象，构造参数set类型，集合中每个元素是HostAndPort参数
		Set<HostAndPort> nodes = new HashSet<HostAndPort>();
		nodes.add(new HostAndPort("127.0.0.1", 7001));
		nodes.add(new HostAndPort("127.0.0.1", 7002));
		nodes.add(new HostAndPort("127.0.0.1", 7003));
		nodes.add(new HostAndPort("127.0.0.1", 7004));
		nodes.add(new HostAndPort("127.0.0.1", 7005));
		nodes.add(new HostAndPort("127.0.0.1", 7006));
		
		JedisCluster jedisCluster = new JedisCluster(nodes);
		
		//直接使用JedisCluster操作redis，自带连接池,JedisCluster对象是单例的
		jedisCluster.set("jedis-cluster", "hello jedis-cluster");
		System.out.println(jedisCluster.get("jedis-cluster"));
	}
}
