package com.training.modules.quartz.dao;

import redis.clients.jedis.ShardedJedis;

public interface RedisDataSource {

	public ShardedJedis getRedisClient();
	public void returnResource(ShardedJedis shardedJedis);
	public void returnResource(ShardedJedis shardedJedis, boolean broken);
}
