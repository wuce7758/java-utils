package com.github.bingoohuang.utils.redis;

import redis.clients.jedis.Jedis;

public abstract class RedisCall implements RedisOp {
    @Override
    public Object exec(Jedis jedis) {
        call(jedis);
        return null;
    }

    protected abstract void call(Jedis jedis);
}
