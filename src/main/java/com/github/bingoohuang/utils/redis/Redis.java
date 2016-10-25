package com.github.bingoohuang.utils.redis;

import redis.clients.jedis.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
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

    public <T> T op(RedisOp<T> redisOp) {
        Jedis jedis = pool.getResource();
        try {
            return redisOp.exec(jedis);
        } finally {
            jedis.close();
        }
    }

    public boolean tryLock(final String key) {
        boolean tryLock = setnx(key, "tryLock");
        if (tryLock) expire(key, 10, TimeUnit.SECONDS);
        return tryLock;
    }

    public boolean isLocked(final String key) {
        String s = get(key);
        return s != null;
    }

    public boolean setnx(final String key, final String value) {
        Jedis jedis = pool.getResource();
        try {
            return jedis.setnx(key, value) == 1;
        } finally {
            jedis.close();
        }
    }

    public boolean exists(final String key) {
        Jedis jedis = pool.getResource();
        try {
            return jedis.exists(key);
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


    public long decr(String key) {
        Jedis jedis = pool.getResource();
        try {
            return jedis.decr(key);
        } finally {
            jedis.close();
        }
    }

    public long decrBy(String key, long incrBy) {
        Jedis jedis = pool.getResource();
        try {
            return jedis.decrBy(key, incrBy);
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

    public Long zadd(String key, double score, String member) {
        Jedis jedis = pool.getResource();
        try {
            return jedis.zadd(key, score, member);
        } finally {
            jedis.close();
        }
    }

    public Double zscore(String key, String member) {
        Jedis jedis = pool.getResource();
        try {
            return jedis.zscore(key, member);
        } finally {
            jedis.close();
        }
    }

    public void topn(String key, double score, String member, int topn) {
        Jedis jedis = pool.getResource();
        try {
            jedis.zadd(key, score, member);
            jedis.zremrangeByRank(key, 0, -topn - 1);
        } finally {
            jedis.close();
        }
    }

    public Set<Tuple> topn(String key) {
        Jedis jedis = pool.getResource();
        try {
            return jedis.zrevrangeWithScores(key, 0, -1);
        } finally {
            jedis.close();
        }
    }

    public Long zadd(String key, Map<String, Double> scoreMembers) {
        Jedis jedis = pool.getResource();
        try {
            return jedis.zadd(key, scoreMembers);
        } finally {
            jedis.close();
        }
    }

    /*
    Delete all except the top 5, zremrangebyrank myzset 0 -6
     */
    public Long zremrangeByRank(String key, long start, long stop) {
        Jedis jedis = pool.getResource();
        try {
            return jedis.zremrangeByRank(key, start, stop);
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
