package com.noahframe.nfcore.iface.module;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author:zoocee
 * @Date:2018/11/18 19:29
 */
public interface NFIRedis<T> {


    public abstract Long append(String key, String value);

    public abstract String auth(String password);

    public abstract String bgrewriteaof();

    public abstract String bgsave();

    public abstract Long bitcount(String key);

    public abstract Long bitcount(String key, long start, long end);

    public abstract List<String> blpop(String arg);

    public abstract List<String> brpop(String arg);

    public abstract String configResetStat();

    public abstract Long dbSize();

    // String	debug(DebugParams params);
    public abstract Long decr(String key);

    public abstract Long decrBy(String key, long integer);

    public abstract Long del(String key);

    public abstract String echo(String string);

    public abstract Boolean exists(String key);

    public abstract Long expire(String key, int seconds);

    public abstract Long expireAt(String key, long unixTime);

    public abstract String flushAll();

    public abstract String flushDB();

    public abstract String get(String key);

    public abstract Boolean getbit(String key, long offset);

    public abstract Map<String, T> getClusterNodes();

    public abstract Long getDB();

    public abstract String getrange(String key, long startOffset, long endOffset);

    public abstract String getSet(String key, String value);

    public abstract Long hdel(String key, String... field);

    public abstract Boolean hexists(String key, String field);

    public abstract String hget(String key, String field);

    public abstract Map<String, String> hgetAll(String key);

    public abstract Long hincrBy(String key, String field, long value);

    public abstract Set<String> hkeys(String key);

    public abstract Long hlen(String key);

    public abstract List<String> hmget(String key, String... fields);

    public abstract String hmset(String key, Map<String, String> hash);

    //ScanResult<Map.Entry<String,String>>	hscan(String key, int cursor);
    //Deprecated.
    //ScanResult<Map.Entry<String,String>>	hscan(String key, String cursor);
    public abstract Long hset(String key, String field, String value);

    public abstract Long hsetnx(String key, String field, String value);

    public abstract List<String> hvals(String key);

    public abstract Long incr(String key);

    public abstract Long incrBy(String key, long integer);

    public abstract String info();

    public abstract String info(String section);

    public abstract Long lastsave();

    public abstract String lindex(String key, long index);

    public abstract Long llen(String key);

    public abstract String lpop(String key);

    public abstract Long lpush(String key, String... string);

    public abstract Long lpushx(String key, String... string);

    public abstract List<String> lrange(String key, long start, long end);

    public abstract Long lrem(String key, long count, String value);

    public abstract String lset(String key, long index, String value);

    public abstract String ltrim(String key, long start, long end);

    public abstract Long move(String key, int dbIndex);

    public abstract Long persist(String key);

    public abstract Long pfadd(String key, String... elements);

    public abstract long pfcount(String key);

    public abstract String ping();

    public abstract String quit();

    public abstract String rpop(String key);

    public abstract Long rpush(String key, String... string);

    public abstract Long rpushx(String key, String... string);

    public abstract Long sadd(String key, String... member);

    public abstract String save();

    public abstract Long scard(String key);

    public abstract String select(int index);

    public abstract String set(String key, String value);

    public abstract String set(String key, String value, String nxxx, String expx, long time);

    public abstract Boolean setbit(String key, long offset, boolean value);

    public abstract Boolean setbit(String key, long offset, String value);

    public abstract String setex(String key, int seconds, String value);

    public abstract Long setnx(String key, String value);

    public abstract Long setrange(String key, long offset, String value);

    public abstract String shutdown();

    public abstract Boolean sismember(String key, String member);

    public abstract String slaveof(String host, int port);

    public abstract String slaveofNoOne();

    public abstract Set<String> smembers(String key);

    public abstract List<String> sort(String key);

    public abstract String spop(String key);

    public abstract String srandmember(String key);

    public abstract Long srem(String key, String... member);

    public abstract Long strlen(String key);

    public abstract String substr(String key, int start, int end);

    public abstract Long ttl(String key);

    public abstract String type(String key);

    public abstract Long waitReplicas(int replicas, long timeout);

    public abstract Long zadd(String key, double score, String member);

    public abstract Long zadd(String key, Map<String, Double> scoreMembers);

    public abstract Long zcard(String key);

    public abstract Long zcount(String key, double min, double max);

    public abstract Long zcount(String key, String min, String max);

    public abstract Double zincrby(String key, double score, String member);

    public abstract Set<String> zrange(String key, long start, long end);

    public abstract Set<String> zrangeByScore(String key, double min, double max);

    public abstract Set<String> zrangeByScore(String key, double min, double max, int offset, int count);

    public abstract Set<String> zrangeByScore(String key, String min, String max);

    public abstract Set<String> zrangeByScore(String key, String min, String max, int offset, int count);

    public abstract Long zrank(String key, String member);

    public abstract Long zrem(String key, String... member);

    public abstract Long zremrangeByRank(String key, long start, long end);

    public abstract Long zremrangeByScore(String key, double start, double end);

    public abstract Long zremrangeByScore(String key, String start, String end);

    public abstract Set<String> zrevrange(String key, long start, long end);

    public abstract Set<String> zrevrangeByScore(String key, double max, double min);

    public abstract Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count);

    public abstract Set<String> zrevrangeByScore(String key, String max, String min);

    public abstract Set<String> zrevrangeByScore(String key, String max, String min, int offset, int count);

    public abstract Long zrevrank(String key, String member);

    public abstract Double zscore(String key, String member);

}
