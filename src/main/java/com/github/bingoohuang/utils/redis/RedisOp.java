package com.github.bingoohuang.utils.redis;

import redis.clients.jedis.Jedis;

public interface RedisOp {
    Object exec(Jedis jedis);
}
