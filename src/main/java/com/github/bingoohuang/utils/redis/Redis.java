package com.github.bingoohuang.utils.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Redis {
    JedisPool pool;

    public Redis(RedisConfig redisConfig) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

        jedisPoolConfig.setMaxTotal(redisConfig.getMaxClients());

        pool = new JedisPool(jedisPoolConfig,
                redisConfig.getHost(), redisConfig.getPort(),
                redisConfig.getTimeout(), redisConfig.getPassword(),
                redisConfig.getDatabase());
    }

    public void destroy() {
        pool.destroy();
    }

    public Object op(RedisOp redisOp) {
        Jedis jedis = pool.getResource();
        try {
            return redisOp.exec(jedis);
        } finally {
            jedis.close();
        }
    }

    public Long ttl(final String key) {
        Jedis jedis = pool.getResource();
        try {
            return jedis.ttl(key);
        } finally {
            jedis.close();
        }
    }

    public String setex(String key, String value, long expire, TimeUnit timeUnit) {
        Jedis jedis = pool.getResource();
        try {
            return jedis.setex(key, (int) timeUnit.toSeconds(expire), value);
        } finally {
            jedis.close();
        }
    }

    public String hmsetex(String key, Map value, long expire, TimeUnit timeUnit) {
        Jedis jedis = pool.getResource();
        try {
            String result = jedis.hmset(key, value);
            jedis.expire(key, (int) timeUnit.toSeconds(expire));
            return result;
        } finally {
            jedis.close();
        }
    }

    public String set(String key, String value) {
        Jedis jedis = pool.getResource();
        try {
            return jedis.set(key, value);
        } finally {
            jedis.close();
        }
    }

    public long incr(String key) {
        Jedis jedis = pool.getResource();
        try {
            return jedis.incr(key);
        } finally {
            jedis.close();
        }
    }

    public long incrBy(String key, long incrBy) {
        Jedis jedis = pool.getResource();
        try {
            return jedis.incrBy(key, incrBy);
        } finally {
            jedis.close();
        }
    }

    public String hmset(String key, Map value) {
        Jedis jedis = pool.getResource();
        try {
            return jedis.hmset(key, value);
        } finally {
            jedis.close();
        }
    }

    public String get(String key, String defaultValue) {
        String value = get(key);
        return value == null ? defaultValue : value;
    }

    public String get(String key) {
        Jedis jedis = pool.getResource();
        try {
            return jedis.get(key);
        } finally {
            jedis.close();
        }
    }

    public String hget(String key, String field) {
        Jedis jedis = pool.getResource();
        try {
            return jedis.hget(key, field);
        } finally {
            jedis.close();
        }
    }

    public Long lpush(String key, String... names) {
        Jedis jedis = pool.getResource();
        try {
            return jedis.lpush(key, names);
        } finally {
            jedis.close();
        }
    }

    public String lpop(String key) {
        Jedis jedis = pool.getResource();
        try {
            return jedis.lpop(key);
        } finally {
            jedis.close();
        }
    }

    public List lrange(String key) {
        Jedis jedis = pool.getResource();
        try {
            return jedis.lrange(key, 0, -1);
        } finally {
            jedis.close();
        }
    }

    public List lrange(String key, long start, long end) {
        Jedis jedis = pool.getResource();
        try {
            return jedis.lrange(key, start, end);
        } finally {
            jedis.close();
        }
    }


    public String getAndDel(String key) {
        Jedis jedis = pool.getResource();
        try {
            Transaction multi = jedis.multi();
            multi.get(key);
            multi.del(key);
            List<Object> result = multi.exec();
            return (String) result.get(0);
        } finally {
            jedis.close();
        }
    }


    public Long del(String key) {
        Jedis jedis = pool.getResource();
        try {
            return jedis.del(key);
        } finally {
            jedis.close();
        }
    }

    public void expire(String key, long expireValue, TimeUnit timeUnit) {
        Jedis jedis = pool.getResource();
        try {
            jedis.expire(key, (int) timeUnit.toSeconds(expireValue));
        } finally {
            jedis.close();
        }
    }

}
