package com.noahframe.nfcore.iface.module;


import redis.clients.jedis.JedisCommands;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author:zoocee
 * @Date:2018/11/18 19:29
 */
public abstract class NFIRedis<T> {

    private T m_Redis;

    protected JedisCommands m_Command;

    public abstract boolean Execute();
    public abstract boolean Enable();

    /**
     * 获取指定key的值,如果key不存在返回null，如果该Key存储的不是字符串，会抛出一个错误
     *
     * @param key
     * @return
     */
    public String get(String key) {
        JedisCommands JedisCommands = getJedisCommand();
        String value = null;
        value = JedisCommands.get(key);
        return value;
    }

    /**
     * 设置key的值为value
     *
     * @param key
     * @param value
     * @return
     */
    public String set(String key, String value) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.set(key, value);
    }

    /**
     * 删除指定的key
     *
     * @param key
     * @return
     */
    public Long del(String key) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.del(key);
    }

    /**
     * 通过key向指定的value值追加值
     *
     * @param key
     * @param str
     * @return
     */
    public Long append(String key, String str) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.append(key, str);
    }

    /**
     * 判断key是否存在
     *
     * @param key
     * @return
     */
    public Boolean exists(String key) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.exists(key);
    }

    /**
     * 设置key value,如果key已经存在则返回0
     *
     * @param key
     * @param value
     * @return
     */
    public Long setnx(String key, String value) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.setnx(key, value);
    }

    /**
     * 设置key value并指定这个键值的有效期
     *
     * @param key
     * @param seconds
     * @param value
     * @return
     */
    public String setex(String key, int seconds, String value) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.setex(key, seconds, value);
    }

    /**
     * 通过key 和offset 从指定的位置开始将原先value替换
     *
     * @param key
     * @param offset
     * @param str
     * @return
     */
    public Long setrange(String key, int offset, String str) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.setrange(key, offset, str);
    }

    /**
     * 设置key的值,并返回一个旧值
     *
     * @param key
     * @param value
     * @return
     */
    public String getSet(String key, String value) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.getSet(key, value);
    }

    /**
     * 通过下标 和key 获取指定下标位置的 value
     *
     * @param key
     * @param startOffset
     * @param endOffset
     * @return
     */
    public String getrange(String key, int startOffset, int endOffset) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.getrange(key, startOffset, endOffset);
    }

    /**
     * 通过key 对value进行加值+1操作,当value不是int类型时会返回错误,当key不存在是则value为1
     *
     * @param key
     * @return
     */
    public Long incr(String key) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.incr(key);
    }

    /**
     * 通过key给指定的value加值,如果key不存在,则这是value为该值
     *
     * @param key
     * @param integer
     * @return
     */
    public Long incrBy(String key, long integer) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.incrBy(key, integer);
    }

    /**
     * 对key的值做减减操作,如果key不存在,则设置key为-1
     *
     * @param key
     * @return
     */
    public Long decr(String key) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.decr(key);
    }

    /**
     * 减去指定的值
     *
     * @param key
     * @param integer
     * @return
     */
    public Long decrBy(String key, long integer) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.decrBy(key, integer);
    }

    /**
     * 通过key获取value值的长度
     *
     * @param key
     * @return
     */
    public Long strLen(String key) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.strlen(key);
    }

    /**
     * 通过key给field设置指定的值,如果key不存在则先创建,如果field已经存在,返回0
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    public Long hsetnx(String key, String field, String value) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.hsetnx(key, field, value);
    }

    /**
     * 通过key给field设置指定的值,如果key不存在,则先创建
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    public Long hset(String key, String field, String value) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.hset(key, field, value);
    }

    /**
     * 通过key同时设置 hash的多个field
     *
     * @param key
     * @param hash
     * @return
     */
    public String hmset(String key, Map<String, String> hash) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.hmset(key, hash);
    }

    /**
     * 通过key 和 field 获取指定的 value
     *
     * @param key
     * @param failed
     * @return
     */
    public String hget(String key, String failed) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.hget(key, failed);
    }

    /**
     * 设置key的超时时间为seconds
     *
     * @param key
     * @param seconds
     * @return
     */
    public Long expire(String key, int seconds) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.expire(key, seconds);
    }

    /**
     * 通过key 和 fields 获取指定的value 如果没有对应的value则返回null
     *
     * @param key
     * @param fields 可以是 一个String 也可以是 String数组
     * @return
     */
    public List<String> hmget(String key, String... fields) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.hmget(key, fields);
    }

    /**
     * 通过key给指定的field的value加上给定的值
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    public Long hincrby(String key, String field, Long value) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.hincrBy(key, field, value);
    }

    /**
     * 通过key和field判断是否有指定的value存在
     *
     * @param key
     * @param field
     * @return
     */
    public Boolean hexists(String key, String field) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.hexists(key, field);
    }

    /**
     * 通过key返回field的数量
     *
     * @param key
     * @return
     */
    public Long hlen(String key) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.hlen(key);
    }

    /**
     * 通过key 删除指定的 field
     *
     * @param key
     * @param fields 可以是 一个 field 也可以是 一个数组
     * @return
     */
    public Long hdel(String key, String... fields) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.hdel(key, fields);
    }

    /**
     * 通过key返回所有的field
     *
     * @param key
     * @return
     */
    public Set<String> hkeys(String key) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.hkeys(key);
    }

    /**
     * 通过key返回所有和key有关的value
     *
     * @param key
     * @return
     */
    public List<String> hvals(String key) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.hvals(key);
    }

    /**
     * 通过key获取所有的field和value
     *
     * @param key
     * @return
     */
    public Map<String, String> hgetall(String key) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.hgetAll(key);
    }

    /**
     * 通过key向list头部添加字符串
     *
     * @param key
     * @param strs 可以是一个string 也可以是string数组
     * @return 返回list的value个数
     */
    public Long lpush(String key, String... strs) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.lpush(key, strs);
    }

    /**
     * 通过key向list尾部添加字符串
     *
     * @param key
     * @param strs 可以是一个string 也可以是string数组
     * @return 返回list的value个数
     */
    public Long rpush(String key, String... strs) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.rpush(key, strs);
    }

    /**
     * 通过key设置list指定下标位置的value
     * 如果下标超过list里面value的个数则报错
     *
     * @param key
     * @param index 从0开始
     * @param value
     * @return 成功返回OK
     */
    public String lset(String key, Long index, String value) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.lset(key, index, value);
    }

    /**
     * 通过key从对应的list中删除指定的count个 和 value相同的元素
     *
     * @param key
     * @param count 当count为0时删除全部
     * @param value
     * @return 返回被删除的个数
     */
    public Long lrem(String key, long count, String value) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.lrem(key, count, value);
    }

    /**
     * 通过key保留list中从strat下标开始到end下标结束的value值
     *
     * @param key
     * @param start
     * @param end
     * @return 成功返回OK
     */
    public String ltrim(String key, long start, long end) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.ltrim(key, start, end);
    }

    /**
     * 通过key从list的头部删除一个value,并返回该value
     *
     * @param key
     * @return
     */
    public synchronized String lpop(String key) {

        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.lpop(key);
    }

    /**
     * 通过key从list尾部删除一个value,并返回该元素
     *
     * @param key
     * @return
     */
    synchronized public String rpop(String key) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.rpop(key);
    }

    /**
     * 通过key获取list中指定下标位置的value
     *
     * @param key
     * @param index
     * @return 如果没有返回null
     */
    public String lindex(String key, long index) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.lindex(key, index);
    }

    /**
     * 通过key返回list的长度
     *
     * @param key
     * @return
     */
    public Long llen(String key) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.llen(key);
    }

    /**
     * 通过key获取list指定下标位置的value
     * 如果start 为 0 end 为 -1 则返回全部的list中的value
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<String> lrange(String key, long start, long end) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.lrange(key, start, end);
    }

    /**
     * 通过key向指定的set中添加value
     *
     * @param key
     * @param members 可以是一个String 也可以是一个String数组
     * @return 添加成功的个数
     */
    public Long sadd(String key, String... members) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.sadd(key, members);
    }

    /**
     * 通过key删除set中对应的value值
     *
     * @param key
     * @param members 可以是一个String 也可以是一个String数组
     * @return 删除的个数
     */
    public Long srem(String key, String... members) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.srem(key, members);
    }

    /**
     * 通过key随机删除一个set中的value并返回该值
     *
     * @param key
     * @return
     */
    public String spop(String key) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.spop(key);
    }

    /**
     * 通过key获取set中value的个数
     *
     * @param key
     * @return
     */
    public Long scard(String key) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.scard(key);
    }

    /**
     * 通过key判断value是否是set中的元素
     *
     * @param key
     * @param member
     * @return
     */
    public Boolean sismember(String key, String member) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.sismember(key, member);
    }

    /**
     * 通过key获取set中随机的value,不删除元素
     *
     * @param key
     * @return
     */
    public String srandmember(String key) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.srandmember(key);
    }

    /**
     * 通过key获取set中所有的value
     *
     * @param key
     * @return
     */
    public Set<String> smembers(String key) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.smembers(key);
    }


    /**
     * 通过key向zset中添加value,score,其中score就是用来排序的
     * 如果该value已经存在则根据score更新元素
     *
     * @param key
     * @param score
     * @param member
     * @return
     */
    public Long zadd(String key, double score, String member) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.zadd(key, score, member);
    }

    /**
     * 通过key删除在zset中指定的value
     *
     * @param key
     * @param members 可以 是一个string 也可以是一个string数组
     * @return
     */
    public Long zrem(String key, String... members) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.zrem(key, members);
    }

    /**
     * 通过key增加该zset中value的score的值
     *
     * @param key
     * @param score
     * @param member
     * @return
     */
    public Double zincrby(String key, double score, String member) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.zincrby(key, score, member);
    }

    /**
     * 通过key返回zset中value的排名
     * 下标从小到大排序
     *
     * @param key
     * @param member
     * @return
     */
    public Long zrank(String key, String member) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.zrank(key, member);
    }

    /**
     * 通过key返回zset中value的排名
     * 下标从大到小排序
     *
     * @param key
     * @param member
     * @return
     */
    public Long zrevrank(String key, String member) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.zrevrank(key, member);
    }

    /**
     * 通过key将获取score从start到end中zset的value
     * socre从大到小排序
     * 当start为0 end为-1时返回全部
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<String> zrevrange(String key, long start, long end) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.zrevrange(key, start, end);
    }

    /**
     * 通过key返回指定score内zset中的value
     *
     * @param key
     * @param max
     * @param min
     * @return
     */
    public Set<String> zrangebyscore(String key, String max, String min) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.zrevrangeByScore(key, max, min);
    }

    /**
     * 通过key返回指定score内zset中的value
     *
     * @param key
     * @param max
     * @param min
     * @return
     */
    public Set<String> zrangeByScore(String key, double max, double min) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.zrevrangeByScore(key, max, min);
    }

    /**
     * 返回指定区间内zset中value的数量
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Long zcount(String key, String min, String max) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.zcount(key, min, max);
    }

    /**
     * 通过key返回zset中的value个数
     *
     * @param key
     * @return
     */
    public Long zcard(String key) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.zcard(key);
    }

    /**
     * 通过key获取zset中value的score值
     *
     * @param key
     * @param member
     * @return
     */
    public Double zscore(String key, String member) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.zscore(key, member);
    }

    /**
     * 通过key删除给定区间内的元素
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Long zremrangeByRank(String key, long start, long end) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.zremrangeByRank(key, start, end);
    }

    /**
     * 通过key删除指定score内的元素
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Long zremrangeByScore(String key, double start, double end) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.zremrangeByScore(key, start, end);
    }

    /**
     * 通过key判断值得类型
     *
     * @param key
     * @return
     */
    public String type(String key) {
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.type(key);
    }

    public Long ttl(String key){
        JedisCommands JedisCommands = getJedisCommand();
        return JedisCommands.ttl(key);
    }

    private JedisCommands getJedisCommand() {
        return m_Command;
    }


    public T getRedis() {
        return m_Redis;
    }

    public void setRedis(T m_Redis) {
        this.m_Redis = m_Redis;
    }
}
