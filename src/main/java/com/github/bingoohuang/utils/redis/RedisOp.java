package com.github.bingoohuang.utils.redis;

import redis.clients.jedis.Jedis;

public interface RedisOp<T> {
    T exec(Jedis jedis);
}
