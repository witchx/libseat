package com.libseat.utils.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author Witch
 */

public class RedisUtil {

    private JedisPool jedisPool;

    public void initPool(String host, int port, int database,String password) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(200);
        poolConfig.setMaxIdle(30);
        poolConfig.setBlockWhenExhausted(true);
        poolConfig.setMaxWaitMillis(10 * 1000);
        poolConfig.setTestOnBorrow(true);
        jedisPool = new JedisPool(poolConfig, host, port, 20 * 1000, password);
    }

    public Jedis getJedis() {
        Jedis jedis = jedisPool.getResource();
        return jedis;
    }
}
