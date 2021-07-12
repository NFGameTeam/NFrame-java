package cn.yeegro.nframe.plugin.redis;

import cn.hutool.extra.spring.SpringUtil;
import cn.yeegro.nframe.comm.code.api.NFGUID;
import cn.yeegro.nframe.comm.module.NFILogModule;
import cn.yeegro.nframe.common.utils.SpringUtils;
import cn.yeegro.nframe.components.nosql.event.CACHE_SUBSCRIBE_FUNCTOR;
import cn.yeegro.nframe.components.nosql.redis.NFIRedisModule;
import org.apache.commons.lang3.StringUtils;
import org.pf4j.Extension;
import org.springframework.dao.DataAccessException;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.util.CollectionUtils;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Extension
public class NFRedisModule extends NFIRedisModule {


    private static NFRedisModule SingletonPtr=null;


    public static NFRedisModule GetSingletonPtr()
    {
        if (null==SingletonPtr) {
            SingletonPtr=new NFRedisModule();
            return SingletonPtr;
        }
        return SingletonPtr;
    }

    @Override
    public boolean Awake() {
        return false;
    }

    @Override
    public boolean Init() {
      //  m_pLogModule = pPluginManager.FindModule( NFILogModule.class);
        return true;
    }

    @Override
    public boolean AfterInit() {
        return false;
    }

    @Override
    public boolean CheckConfig() {
        return false;
    }

    @Override
    public boolean ReadyExecute() {
        return false;
    }

    @Override
    public boolean Execute() {
        return false;
    }

    @Override
    public boolean BeforeShut() {
        return false;
    }

    @Override
    public boolean Shut() {
        return false;
    }

    @Override
    public boolean Finalize() {
        return false;
    }

    @Override
    public boolean OnReloadPlugin() {
        return false;
    }


    /**
     * json序列化方式
     */
    private static GenericJackson2JsonRedisSerializer redisObjectSerializer = new GenericJackson2JsonRedisSerializer();

    /**
     * 默认RedisObjectSerializer序列化
     */
    private RedisTemplate<Object, Object> redisTemplate;



    /**
     * 获取消息
     *
     * @param topicGroup
     * @param channel
     * @return
     */
    public String getMsg(String topicGroup, String channel) {
        return hashOperations.get(topicGroup, channel);
    }

    /**
     * 删除消息
     *
     * @param topicGroup
     * @param channel
     * @return
     */
    public boolean removeMsg(String topicGroup, String channel) {
        publishMsg(topicGroup, channel, StringUtils.EMPTY);

        return hashOperations.delete(topicGroup, channel) == 1;
    }

    /**
     * 发送消息，存redis hash
     *
     * @param topicGroup
     * @param channel
     * @param msg
     * @return
     */
    public boolean publishMsg(String topicGroup, String channel, String msg) {
        hashOperations.put(topicGroup, channel, msg);
        //向通道发送消息的方法
        stringRedisTemplate.convertAndSend(topicGroup + "-" + channel, msg);
        return true;
    }

    @Override
    public void subscribeConfig(String msg, CACHE_SUBSCRIBE_FUNCTOR redisSubscribeCallback) {

    }

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    @Override
    public boolean expire(String key, long time) {
        redisTemplate=SpringUtils.getBean(RedisTemplate.class);
        return redisTemplate.execute(new RedisCallback<Boolean>() {

            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                long rawTimeout = TimeoutUtils.toMillis(time, TimeUnit.SECONDS);
                try {
                    return connection.pExpire(key.getBytes(), rawTimeout);
                } catch (Exception e) {
                    // Driver may not support pExpire or we may be running on
                    // Redis 2.4
                    return connection.expire(key.getBytes(), TimeoutUtils.toSeconds(rawTimeout, TimeUnit.SECONDS));
                }
            }
        });
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    @Override
    public long getExpire(String key) {
        redisTemplate=SpringUtils.getBean(RedisTemplate.class);
        return redisTemplate.execute(new RedisCallback<Long>() {

            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {

                try {
                    return connection.pTtl(key.getBytes(), TimeUnit.SECONDS);
                } catch (Exception e) {
                    // Driver may not support pTtl or we may be running on Redis
                    // 2.4
                    return connection.ttl(key.getBytes(), TimeUnit.SECONDS);
                }
            }
        });
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    @Override
    public boolean hasKey(String key) {
        redisTemplate=SpringUtils.getBean(RedisTemplate.class);
        return redisTemplate.execute((RedisCallback<Boolean>) connection -> connection.exists(key.getBytes()));
    }

    // ============================Object=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    @Override
    public Object get(String key) {
        redisTemplate=SpringUtils.getBean(RedisTemplate.class);
        Object value = redisTemplate.execute(new RedisCallback<Object>() {

            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {

                // redis info
                byte[] temp = null;
                temp = connection.get(key.getBytes());
                connection.close();

                return redisObjectSerializer.deserialize(temp);
            }
        });

        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    @Override
    public boolean set(String key, Object value) {
        redisTemplate=SpringUtils.getBean(RedisTemplate.class);
        try {

            redisTemplate.execute((RedisCallback<Long>) connection -> {
                // redis info
                byte[] values = redisObjectSerializer.serialize(value);
                connection.set(key.getBytes(), values);
                connection.close();

                return 1L;
            });
            return true;
        } catch (Exception e) {
            StackTraceElement stackTraceElement = e.getStackTrace()[0];
           // m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, new NFGUID(), "set redis error", m_pLogModule.CurrTrace().getMethodName(), m_pLogModule.CurrTrace().getLineNumber());
            //log.error("set-" + stackTraceElement.getMethodName() + "--" + stackTraceElement.getLineNumber());
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    @Override
    public boolean set(String key, Object value, long time) {
        redisTemplate=SpringUtils.getBean(RedisTemplate.class);
        try {
            if (time > 0) {

                redisTemplate.execute((RedisCallback<Long>) connection -> {
                    // redis info
                    byte[] values = redisObjectSerializer.serialize(value);
                    connection.set(key.getBytes(), values);
                    connection.expire(key.getBytes(), 60 * time);
                    connection.close();
                    return 1L;
                });

            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            StackTraceElement stackTraceElement = e.getStackTrace()[0];
           // m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, new NFGUID(), "set redis error", m_pLogModule.CurrTrace().getMethodName(), m_pLogModule.CurrTrace().getLineNumber());
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    @Override
    public long incr(String key, long delta) {
        redisTemplate=SpringUtils.getBean(RedisTemplate.class);
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.execute((RedisCallback<Long>) connection -> {

            return connection.incrBy(key.getBytes(), delta);
        });
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    @Override
    public long decr(String key, long delta) {
        redisTemplate=SpringUtils.getBean(RedisTemplate.class);
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }

        return redisTemplate.execute((RedisCallback<Long>) connection -> {

            return connection.incrBy(key.getBytes(), -delta);
        });

    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @Override
    public void del(String... key) {
        redisTemplate=SpringUtils.getBean(RedisTemplate.class);
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param offset 位 8Bit=1Byte
     * @return
     */
    @Override
    public boolean setBit(String key, long offset, boolean isShow) {
        redisTemplate=SpringUtils.getBean(RedisTemplate.class);
        boolean result = false;
        try {
            ValueOperations<Object, Object> operations = redisTemplate.opsForValue();
            operations.setBit(key, offset, isShow);
            result = true;
        } catch (Exception e) {
            StackTraceElement stackTraceElement = e.getStackTrace()[0];
           // m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, new NFGUID(), "setBit redis error", m_pLogModule.CurrTrace().getMethodName(), m_pLogModule.CurrTrace().getLineNumber());
            //log.error("setBit-" + stackTraceElement.getMethodName() + "--" + stackTraceElement.getLineNumber());
            return false;
        }
        return result;
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param offset
     * @return
     */
    @Override
    public boolean getBit(String key, long offset) {
        redisTemplate=SpringUtils.getBean(RedisTemplate.class);
        boolean result = false;
        try {
            ValueOperations<Object, Object> operations = redisTemplate.opsForValue();
            result = operations.getBit(key, offset);
        } catch (Exception e) {
            StackTraceElement stackTraceElement = e.getStackTrace()[0];
           // m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, new NFGUID(), "getBit redis error", m_pLogModule.CurrTrace().getMethodName(), m_pLogModule.CurrTrace().getLineNumber());

            //log.error("getBit-" + stackTraceElement.getMethodName() + "--" + stackTraceElement.getLineNumber());
            return false;
        }
        return result;
    }


    //===============================list=================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     * @return
     */
    @Override
    public List<Object> lGet(String key, long start, long end) {
        redisTemplate=SpringUtils.getBean(RedisTemplate.class);
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            StackTraceElement stackTraceElement = e.getStackTrace()[0];
            //m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, new NFGUID(), "lGet redis error", m_pLogModule.CurrTrace().getMethodName(), m_pLogModule.CurrTrace().getLineNumber());

            //log.error("lGet-" + stackTraceElement.getMethodName() + "--" + stackTraceElement.getLineNumber());
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    @Override
    public long lGetListSize(String key) {
        redisTemplate=SpringUtils.getBean(RedisTemplate.class);
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            StackTraceElement stackTraceElement = e.getStackTrace()[0];
            //m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, new NFGUID(), "lGetListSize redis error", m_pLogModule.CurrTrace().getMethodName(), m_pLogModule.CurrTrace().getLineNumber());

            //log.error("lGetListSize-" + stackTraceElement.getMethodName() + "--" + stackTraceElement.getLineNumber());
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    @Override
    public Object lGetIndex(String key, long index) {
        redisTemplate=SpringUtils.getBean(RedisTemplate.class);
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            StackTraceElement stackTraceElement = e.getStackTrace()[0];
           // m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, new NFGUID(), "lGetIndex redis error", m_pLogModule.CurrTrace().getMethodName(), m_pLogModule.CurrTrace().getLineNumber());

            //log.error("lGetIndex-" + stackTraceElement.getMethodName() + "--" + stackTraceElement.getLineNumber());
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    @Override
    public boolean lSet(String key, Object value) {
        redisTemplate=SpringUtils.getBean(RedisTemplate.class);
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            StackTraceElement stackTraceElement = e.getStackTrace()[0];
           // m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, new NFGUID(), "lSet redis error", m_pLogModule.CurrTrace().getMethodName(), m_pLogModule.CurrTrace().getLineNumber());

            //log.error("lSet-" + stackTraceElement.getMethodName() + "--" + stackTraceElement.getLineNumber());
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    @Override
    public boolean lSet(String key, Object value, long time) {
        redisTemplate=SpringUtils.getBean(RedisTemplate.class);
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) expire(key, time);
            return true;
        } catch (Exception e) {
            StackTraceElement stackTraceElement = e.getStackTrace()[0];
           // m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, new NFGUID(), "lSet redis error", m_pLogModule.CurrTrace().getMethodName(), m_pLogModule.CurrTrace().getLineNumber());

            //log.error("lSet-" + stackTraceElement.getMethodName() + "--" + stackTraceElement.getLineNumber());
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    @Override
    public boolean lSet(String key, List<Object> value) {
        redisTemplate=SpringUtils.getBean(RedisTemplate.class);
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            StackTraceElement stackTraceElement = e.getStackTrace()[0];
           // m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, new NFGUID(), "lSet redis error", m_pLogModule.CurrTrace().getMethodName(), m_pLogModule.CurrTrace().getLineNumber());
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    @Override
    public boolean lSet(String key, List<Object> value, long time) {
        redisTemplate=SpringUtils.getBean(RedisTemplate.class);
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) expire(key, time);
            return true;
        } catch (Exception e) {
            StackTraceElement stackTraceElement = e.getStackTrace()[0];
           // m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, new NFGUID(), "lSet redis error", m_pLogModule.CurrTrace().getMethodName(), m_pLogModule.CurrTrace().getLineNumber());
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    @Override
    public boolean lUpdateIndex(String key, long index, Object value) {
        redisTemplate=SpringUtils.getBean(RedisTemplate.class);
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            StackTraceElement stackTraceElement = e.getStackTrace()[0];
          //  m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, new NFGUID(), "lUpdateIndex redis error", m_pLogModule.CurrTrace().getMethodName(), m_pLogModule.CurrTrace().getLineNumber());

            //log.error("lUpdateIndex-" + stackTraceElement.getMethodName() + "--" + stackTraceElement.getLineNumber());
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    @Override
    public long lRemove(String key, long count, Object value) {
        redisTemplate=SpringUtils.getBean(RedisTemplate.class);
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            StackTraceElement stackTraceElement = e.getStackTrace()[0];
           // m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, new NFGUID(), "lRemove redis error", m_pLogModule.CurrTrace().getMethodName(), m_pLogModule.CurrTrace().getLineNumber());

           // log.error("lRemove-" + stackTraceElement.getMethodName() + "--" + stackTraceElement.getLineNumber());
            return 0;
        }
    }


    // ================================Map=================================

    /**
     * 哈希 添加
     *
     * @param key
     * @param hashKey
     * @param value
     */
    @Override
    public void hmSet(String key, Object hashKey, Object value) {
        redisTemplate=SpringUtils.getBean(RedisTemplate.class);
        HashOperations<Object, Object, Object> hash = redisTemplate.opsForHash();
        hash.put(key, hashKey, value);
    }

    /**
     * 哈希获取数据
     *
     * @param key
     * @param hashKey
     * @return
     */
    @Override
    public Object hmGet(String key, Object hashKey) {
        redisTemplate=SpringUtils.getBean(RedisTemplate.class);
        HashOperations<Object, Object, Object> hash = redisTemplate.opsForHash();
        return hash.get(key, hashKey);
    }


    // ================================set=================================

    /**
     * 集合添加
     *
     * @param key
     * @param value
     */
    @Override
    public void add(String key, Object value) {
        redisTemplate=SpringUtils.getBean(RedisTemplate.class);
        SetOperations<Object, Object> set = redisTemplate.opsForSet();
        set.add(key, value);
    }

    /**
     * 集合获取
     *
     * @param key
     * @return
     */
    @Override
    public Set<Object> setMembers(String key) {
        redisTemplate=SpringUtils.getBean(RedisTemplate.class);
        SetOperations<Object, Object> set = redisTemplate.opsForSet();
        return set.members(key);
    }

    /**
     * 有序集合添加
     *
     * @param key
     * @param value
     * @param scoure
     */
    @Override
    public void zAdd(String key, Object value, double scoure) {
        redisTemplate=SpringUtils.getBean(RedisTemplate.class);
        ZSetOperations<Object, Object> zset = redisTemplate.opsForZSet();
        zset.add(key, value, scoure);
    }

    /**
     * 有序集合获取
     *
     * @param key
     * @param scoure
     * @param scoure1
     * @return
     */
    @Override
    public Set<Object> rangeByScore(String key, double scoure, double scoure1) {
        redisTemplate=SpringUtils.getBean(RedisTemplate.class);
        ZSetOperations<Object, Object> zset = redisTemplate.opsForZSet();
        redisTemplate.opsForValue();
        return zset.rangeByScore(key, scoure, scoure1);
    }


    /**
     * 有序集合获取排名
     *
     * @param key   集合名称
     * @param value 值
     */
    @Override
    public Long zRank(String key, Object value) {
        redisTemplate=SpringUtils.getBean(RedisTemplate.class);
        ZSetOperations<Object, Object> zset = redisTemplate.opsForZSet();
        return zset.rank(key, value);
    }

    /**
     * 有序集合获取排名
     *
     * @param key
     */
    @Override
    public Set<ZSetOperations.TypedTuple<Object>> zRankWithScore(String key, long start, long end) {
        redisTemplate=SpringUtils.getBean(RedisTemplate.class);
        ZSetOperations<Object, Object> zset = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<Object>> ret = zset.rangeWithScores(key, start, end);
        return ret;
    }

    /**
     * 有序集合添加
     *
     * @param key
     * @param value
     */
    @Override
    public Double zSetScore(String key, Object value) {
        redisTemplate=SpringUtils.getBean(RedisTemplate.class);
        ZSetOperations<Object, Object> zset = redisTemplate.opsForZSet();
        return zset.score(key, value);
    }

    /**
     * 有序集合添加分数
     *
     * @param key
     * @param value
     * @param scoure
     */
    @Override
    public void incrementScore(String key, Object value, double scoure) {
        redisTemplate=SpringUtils.getBean(RedisTemplate.class);
        ZSetOperations<Object, Object> zset = redisTemplate.opsForZSet();
        zset.incrementScore(key, value, scoure);
    }

    /**
     * 有序集合获取排名
     *
     * @param key
     */
    @Override
    public Set<ZSetOperations.TypedTuple<Object>> reverseZRankWithScore(String key, long start, long end) {
        redisTemplate=SpringUtils.getBean(RedisTemplate.class);
        ZSetOperations<Object, Object> zset = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<Object>> ret = zset.reverseRangeByScoreWithScores(key, start, end);
        return ret;
    }

    /**
     * 有序集合获取排名
     *
     * @param key
     */
    @Override
    public Set<ZSetOperations.TypedTuple<Object>> reverseZRankWithRank(String key, long start, long end) {
        redisTemplate=SpringUtils.getBean(RedisTemplate.class);
        ZSetOperations<Object, Object> zset = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<Object>> ret = zset.reverseRangeWithScores(key, start, end);
        return ret;
    }

    /**
     * 添加经纬度信息
     * map.put("北京" ,new Point(116.405285 ,39.904989)) //redis 命令：geoadd cityGeo 116.405285 39.904989 "北京"
     */
    @Override
    public Long addGeoPoint(String key ,  Map<Object, Point> map) {
        redisTemplate=SpringUtils.getBean(RedisTemplate.class);
        return  redisTemplate.opsForGeo().add(key, map);
    }

    /**
     * 查找指定key的经纬度信息
     * redis命令：geopos cityGeo 北京
     * @param key
     * @param member
     * @return
     */
    @Override
    public Point geoGetPoint(String key , String member) {
        redisTemplate=SpringUtils.getBean(RedisTemplate.class);
        List<Point> lists = redisTemplate.opsForGeo().position(key, member) ;
        return lists.get(0);
    }

    /**
     * 返回两个地方的距离，可以指定单位
     * redis命令：geodist cityGeo 北京 上海
     * @param key
     * @param srcMember
     * @param targetMember
     * @return
     */
    @Override
    public Distance geoDistance(String key, String srcMember , String targetMember ) {
        redisTemplate=SpringUtils.getBean(RedisTemplate.class);
        Distance distance = redisTemplate.opsForGeo().distance(key, srcMember, targetMember , Metrics.KILOMETERS);
        return distance;
    }
    /**
     * 根据指定的地点查询半径在指定范围内的位置
     * redis命令：georadiusbymember cityGeo 北京 100 km WITHDIST WITHCOORD ASC COUNT 5
     * @param key
     * @param member
     * @param distance
     * @return
     */
    @Override
    public GeoResults geoRadiusByMember(String key, String member , double distance ) {
        redisTemplate=SpringUtils.getBean(RedisTemplate.class);
        return 	redisTemplate.opsForGeo().radius(key, member,  new Distance(distance,Metrics.KILOMETERS )) ;
    }

    /**
     * 根据给定的经纬度，返回半径不超过指定距离的元素
     * redis命令：georadius cityGeo 116.405285 39.904989 100 km WITHDIST WITHCOORD ASC COUNT 5
     * @param key
     * @param circle
     * @param distance
     * @return
     */
    @Override
    public GeoResults geoRadiusByCircle(String key, Circle circle , double distance ) {
        redisTemplate=SpringUtils.getBean(RedisTemplate.class);
        return 	redisTemplate.opsForGeo().radius(key, circle,  new Distance(distance,Metrics.KILOMETERS )) ;
    }

    /**
     *
     * @param prefix 前缀
     * @param ids id
     */
    @Override
    public void delByKeys(String prefix, Set<Long> ids) {
        redisTemplate=SpringUtils.getBean(RedisTemplate.class);
        Set<Object> keys = new HashSet<>();
        for (Long id : ids) {
            keys.addAll(redisTemplate.keys(new StringBuffer(prefix).append(id).toString()));
        }
        long count = redisTemplate.delete(keys);
        // 此处提示可自行删除
        System.out.println("--------------------------------------------");
        System.out.println("成功删除缓存：" + keys.toString());
        System.out.println("缓存删除数量：" + count + "个");
        System.out.println("--------------------------------------------");
    }
}
