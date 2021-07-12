package cn.yeegro.nframe.components.nosql.redis;

import cn.yeegro.nframe.comm.code.api.NFModule;
import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.comm.loader.NFPluginManager;
import cn.yeegro.nframe.comm.module.NFILogModule;
import cn.yeegro.nframe.components.nosql.event.CACHE_SUBSCRIBE_FUNCTOR;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public abstract class  NFIRedisModule extends NFModule {

    protected NFIPluginManager pPluginManager;

    protected StringRedisTemplate stringRedisTemplate;

    protected HashOperations<String, String, String> hashOperations;

    protected RedisConnectionFactory redisConnectionFactory;

    //protected NFILogModule m_pLogModule;

    /**
     * json序列化方式
     */
    private static GenericJackson2JsonRedisSerializer redisObjectSerializer = new GenericJackson2JsonRedisSerializer();

    /**
     * 默认RedisObjectSerializer序列化
     */
    private RedisTemplate<String, Object> redisTemplate;
    

    /**
     * redis工具类
     *
     * @param lettuceConnectionFactory
     * @param stringRedisTemplate
     * @param hashOperations
     */
    public void CreateRedis(RedisConnectionFactory lettuceConnectionFactory, StringRedisTemplate stringRedisTemplate,
                     HashOperations<String, String, String> hashOperations){
        this.redisConnectionFactory = lettuceConnectionFactory;
        this.stringRedisTemplate = stringRedisTemplate;
        this.hashOperations = hashOperations;
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        RedisSerializer redisObjectSerializer = new GenericJackson2JsonRedisSerializer();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer()); // key的序列化类型
        redisTemplate.setValueSerializer(redisObjectSerializer); // value的序列化类型
        redisTemplate.setHashValueSerializer(redisObjectSerializer);
        redisTemplate.afterPropertiesSet();

        this.redisTemplate = redisTemplate ;
        pPluginManager = NFPluginManager.GetSingletonPtr();
    }


    /**
     * 获取消息
     *
     * @param topicGroup
     * @param channel
     * @return
     */
    public abstract String getMsg(String topicGroup, String channel);

    /**
     * 删除消息
     *
     * @param topicGroup
     * @param channel
     * @return
     */
    public abstract boolean removeMsg(String topicGroup, String channel);

    /**
     * 发送消息，存redis hash
     *
     * @param topicGroup
     * @param channel
     * @param msg
     * @return
     */
    public abstract boolean publishMsg(String topicGroup, String channel, String msg);

    /**
     * 订阅回调
     *
     * @param msg
     * @param redisSubscribeCallback
     */
    public abstract void subscribeConfig(String msg, CACHE_SUBSCRIBE_FUNCTOR redisSubscribeCallback);

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public abstract boolean expire(String key, long time);

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public abstract long getExpire(String key) ;

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public abstract boolean hasKey(String key);

    // ============================Object=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public abstract Object get(String key);

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public abstract boolean set(String key, Object value);

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public abstract boolean set(String key, Object value, long time);
    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    public abstract long incr(String key, long delta);

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    public abstract long decr(String key, long delta);

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    public abstract void del(String... key);

    /**
     * 写入缓存
     *
     * @param key
     * @param offset 位 8Bit=1Byte
     * @return
     */
    public abstract boolean setBit(String key, long offset, boolean isShow);

    /**
     * 写入缓存
     *
     * @param key
     * @param offset
     * @return
     */
    public abstract boolean getBit(String key, long offset);


    //===============================list=================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     * @return
     */
    public abstract List<Object> lGet(String key, long start, long end);

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public abstract long lGetListSize(String key);

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public abstract Object lGetIndex(String key, long index);

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public abstract boolean lSet(String key, Object value);

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public abstract boolean lSet(String key, Object value, long time);

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public abstract boolean lSet(String key, List<Object> value);

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public abstract boolean lSet(String key, List<Object> value, long time);

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public abstract boolean lUpdateIndex(String key, long index, Object value) ;

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public abstract long lRemove(String key, long count, Object value);


    // ================================Map=================================

    /**
     * 哈希 添加
     *
     * @param key
     * @param hashKey
     * @param value
     */
    public abstract void hmSet(String key, Object hashKey, Object value) ;

    /**
     * 哈希获取数据
     *
     * @param key
     * @param hashKey
     * @return
     */
    public abstract Object hmGet(String key, Object hashKey);

    // ================================set=================================

    /**
     * 集合添加
     *
     * @param key
     * @param value
     */
    public abstract void add(String key, Object value) ;

    /**
     * 集合获取
     *
     * @param key
     * @return
     */
    public abstract Set<Object> setMembers(String key);

    /**
     * 有序集合添加
     *
     * @param key
     * @param value
     * @param scoure
     */
    public abstract void zAdd(String key, Object value, double scoure);

    /**
     * 有序集合获取
     *
     * @param key
     * @param scoure
     * @param scoure1
     * @return
     */
    public abstract Set<Object> rangeByScore(String key, double scoure, double scoure1);


    /**
     * 有序集合获取排名
     *
     * @param key   集合名称
     * @param value 值
     */
    public abstract Long zRank(String key, Object value);

    /**
     * 有序集合获取排名
     *
     * @param key
     */
    public abstract Set<ZSetOperations.TypedTuple<Object>> zRankWithScore(String key, long start, long end);

    /**
     * 有序集合添加
     *
     * @param key
     * @param value
     */
    public abstract Double zSetScore(String key, Object value);

    /**
     * 有序集合添加分数
     *
     * @param key
     * @param value
     * @param scoure
     */
    public abstract void incrementScore(String key, Object value, double scoure);

    /**
     * 有序集合获取排名
     *
     * @param key
     */
    public abstract Set<ZSetOperations.TypedTuple<Object>> reverseZRankWithScore(String key, long start, long end);

    /**
     * 有序集合获取排名
     *
     * @param key
     */
    public abstract Set<ZSetOperations.TypedTuple<Object>> reverseZRankWithRank(String key, long start, long end);

    /**
     * 添加经纬度信息 
     * map.put("北京" ,new Point(116.405285 ,39.904989)) //redis 命令：geoadd cityGeo 116.405285 39.904989 "北京"
     */
    public abstract Long addGeoPoint(String key ,  Map<Object, Point> map);

    /**
     * 查找指定key的经纬度信息
     * redis命令：geopos cityGeo 北京
     * @param key
     * @param member
     * @return
     */
    public abstract Point geoGetPoint(String key , String member);

    /**
     * 返回两个地方的距离，可以指定单位
     * redis命令：geodist cityGeo 北京 上海
     * @param key
     * @param srcMember
     * @param targetMember
     * @return
     */
    public abstract Distance geoDistance(String key, String srcMember , String targetMember );
    /**
     * 根据指定的地点查询半径在指定范围内的位置
     * redis命令：georadiusbymember cityGeo 北京 100 km WITHDIST WITHCOORD ASC COUNT 5
     * @param key
     * @param member
     * @param distance
     * @return
     */
    public abstract GeoResults geoRadiusByMember(String key, String member ,  double distance );

    /**
     * 根据给定的经纬度，返回半径不超过指定距离的元素
     * redis命令：georadius cityGeo 116.405285 39.904989 100 km WITHDIST WITHCOORD ASC COUNT 5
     * @param key
     * @param circle
     * @param distance
     * @return
     */
    public abstract GeoResults geoRadiusByCircle(String key, Circle circle ,  double distance );


    /**
     *
     * @param prefix 前缀
     * @param ids id
     */
    public abstract void delByKeys(String prefix, Set<Long> ids);
}
