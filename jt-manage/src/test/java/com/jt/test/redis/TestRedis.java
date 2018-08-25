package com.jt.test.redis;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class TestRedis {
	/**
	 * 1.先实例化jedis(IP:端口)
	 * 2.实现redis取值复制
	 */
	@Test
	public void test01(){
		Jedis jedis = new Jedis("192.168.157.133",6379);
		jedis.set("name","tomcat");
		System.out.println("获取redis数据:" +jedis.get("name"));
	}
	
	//测试redis分片实现redis内存扩容
	@Test
	public void test02(){
		
		//定义redis池的配置文件
		JedisPoolConfig poolConfig=new JedisPoolConfig();
		poolConfig.setMaxTotal(1000);
		poolConfig.setMaxIdle(200);
		poolConfig.setMinIdle(10);
		poolConfig.setTestOnBorrow(true);//链接前效验
		
		
		//定义jedis分片的节点信息
		List<JedisShardInfo> shards=new ArrayList<JedisShardInfo>();
		
		shards.add(new JedisShardInfo("192.168.157.133", 6379));
		shards.add(new JedisShardInfo("192.168.157.133", 6380));
		shards.add(new JedisShardInfo("192.168.157.133", 6381));
		
		ShardedJedisPool jedisPool=
				new ShardedJedisPool(poolConfig, shards);
		
		
		ShardedJedis shardedJedis = jedisPool.getResource();
		shardedJedis.set("name", "我是redis分片");
		System.out.println("获取redis信息:"+shardedJedis.get("name"));
	}


	
	
	
	//哨兵的测试
	@Test
	public void test03(){
		
		
		
		
		
		Set<String> sentinels=new HashSet<>();
		System.out.println(
	    new HostAndPort("192.168.157.133",26379).toString());
		
		sentinels.add("192.168.157.133:26379");
		sentinels.add("192.168.157.133:26380");
		sentinels.add("192.168.157.133:26381");
		
		
		//定义哨兵的连接池
		JedisSentinelPool jedisSentinelPool=
				new JedisSentinelPool("mymaster", sentinels);
		
		Jedis jedis = jedisSentinelPool.getResource();
		jedis.set("name", "我是哨兵的redis");
		System.out.println("获取redis数据:"+jedis.get("name"));
	}
	
	
	/**
	 * 步骤:
	 * 1.redis的节点3主6从9个节点
	 * 2。每个节点需要通过ip端口进行联结
	 * 3。创建集群的连接
	 * 
	 */
	@Test
	public  void testCluster(){
		String host="192.168.157.133";
		//定义集群的集合
		Set<HostAndPort> nodes=new HashSet<HostAndPort>();
		nodes.add(new HostAndPort(host, 7000));
		nodes.add(new HostAndPort(host, 7001));
		nodes.add(new HostAndPort(host, 7002));
		nodes.add(new HostAndPort(host, 7003));
		nodes.add(new HostAndPort(host, 7004));
		nodes.add(new HostAndPort(host, 7005));
		nodes.add(new HostAndPort(host, 7006));
		nodes.add(new HostAndPort(host, 7007));
		nodes.add(new HostAndPort(host, 7008));
		JedisCluster jedisCluster = new JedisCluster(nodes);
	
	jedisCluster.set("1803", "集群搭建完成了");
	System.out.println("获取数据："+jedisCluster.get("1803"));
	
	
	}
	
	
	
	
	
	@Test
	public void testFactor(){
		ApplicationContext context=
				new ClassPathXmlApplicationContext("/spring/factory.xml");
		
		
		Calendar calendar1 = (Calendar)context.getBean("calendar");
		Calendar calendar2 = (Calendar)context.getBean("calendar");
	
	System.out.println("1:"+calendar1.getTime());
	System.out.println("2"+calendar2.getTime());
	
	}
	
	
	
	
}
