package cn.yeegro.nframe.plugin.redis;

import cn.yeegro.nframe.common.utils.SpringUtils;
import cn.yeegro.nframe.components.nosql.redis.NFIRedisConfigModule;
import cn.yeegro.nframe.components.nosql.redis.RedissonProperties;
import cn.yeegro.nframe.components.nosql.redis.serializer.RedisObjectSerializer;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.pf4j.Extension;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Extension
public class NFRedisConfigModule extends NFIRedisConfigModule {

    private static NFRedisConfigModule SingletonPtr=null;


    public static NFRedisConfigModule GetSingletonPtr()
    {
        if (null==SingletonPtr) {
            SingletonPtr=new NFRedisConfigModule();
            return SingletonPtr;
        }
        return SingletonPtr;
    }

    @Override
    public LettuceConnectionFactory lettuceConnectionFactory(GenericObjectPoolConfig genericObjectPoolConfig) {

        RedisProperties redisProperties= SpringUtils.getBean(RedisProperties.class);

        Method clusterMethod = ReflectionUtils.findMethod(RedisProperties.class, "getCluster");
        Method timeoutMethod = ReflectionUtils.findMethod(RedisProperties.class, "getTimeout");
        Object timeoutValue = ReflectionUtils.invokeMethod(timeoutMethod, redisProperties);
        RedisConfiguration redisConfiguration = null;
        LettuceClientConfiguration clientConfig = null;
        if (redisProperties.getSentinel() != null) {
            // 哨兵配置
            Method nodesMethod = ReflectionUtils.findMethod(RedisProperties.Sentinel.class, "getNodes");
            Object nodesValue = ReflectionUtils.invokeMethod(nodesMethod, redisProperties.getSentinel());

            String[] nodes = null;
            Set<String> sentinelHostAndPorts = new HashSet<>();
            if (nodesValue instanceof String) {
                nodes = convert(Arrays.asList(((String) nodesValue).split(",")));
                sentinelHostAndPorts.addAll(Arrays.asList(((String) nodesValue).split(",")));
            } else {
                nodes = convert((List<String>) nodesValue);
                sentinelHostAndPorts.addAll((List<String>) nodesValue);
            }
            redisConfiguration = new RedisSentinelConfiguration(redisProperties.getSentinel().getMaster(),
                    sentinelHostAndPorts);
            ((RedisSentinelConfiguration) redisConfiguration)
                    .setPassword(RedisPassword.of(redisProperties.getPassword()));
            ((RedisSentinelConfiguration) redisConfiguration).setDatabase(redisProperties.getDatabase());
            clientConfig = LettucePoolingClientConfiguration.builder().commandTimeout(redisProperties.getTimeout())
                    .poolConfig(genericObjectPoolConfig).build();

        } else if (clusterMethod != null && ReflectionUtils.invokeMethod(clusterMethod, redisProperties) != null) {
            // 集群配置
            List<String> clusterNodes = redisProperties.getCluster().getNodes();
            Set<RedisNode> nodes = new HashSet<RedisNode>();
            clusterNodes.forEach(address -> nodes
                    .add(new RedisNode(address.split(":")[0].trim(), Integer.valueOf(address.split(":")[1]))));
            redisConfiguration = new RedisClusterConfiguration();
            ((RedisClusterConfiguration) redisConfiguration).setClusterNodes(nodes);
            ((RedisClusterConfiguration) redisConfiguration)
                    .setPassword(RedisPassword.of(redisProperties.getPassword()));

            /**
             * ClusterTopologyRefreshOptions配置用于开启自适应刷新和定时刷新。如自适应刷新不开启，
             * Redis集群变更时将会导致连接异常！
             */
            ClusterTopologyRefreshOptions topologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
                    // 开启自适应刷新
                    .enableAdaptiveRefreshTrigger(ClusterTopologyRefreshOptions.RefreshTrigger.MOVED_REDIRECT,
                            ClusterTopologyRefreshOptions.RefreshTrigger.PERSISTENT_RECONNECTS)
                    // 开启所有自适应刷新，MOVED，ASK，PERSISTENT都会触发
                    // .enableAllAdaptiveRefreshTriggers()
                    // 自适应刷新超时时间(默认30秒)
                    .adaptiveRefreshTriggersTimeout(Duration.ofSeconds(25)) // 默认关闭开启后时间为30秒
                    // 开周期刷新
                    .enablePeriodicRefresh(Duration.ofSeconds(20)) // 默认关闭开启后时间为60秒
                    // ClusterTopologyRefreshOptions.DEFAULT_REFRESH_PERIOD
                    // 60 .enablePeriodicRefresh(Duration.ofSeconds(2)) =
                    // .enablePeriodicRefresh().refreshPeriod(Duration.ofSeconds(2))
                    .build();
            clientConfig = LettucePoolingClientConfiguration.builder().commandTimeout(redisProperties.getTimeout())
                    .poolConfig(genericObjectPoolConfig)
                    .clientOptions(
                            ClusterClientOptions.builder().topologyRefreshOptions(topologyRefreshOptions).build())
                    // 将appID传入连接，方便Redis监控中查看
                    // .clientName(appName + "_lettuce")
                    .build();

        } else {
            // 单机版配置
            redisConfiguration = new RedisStandaloneConfiguration();
            ((RedisStandaloneConfiguration) redisConfiguration).setDatabase(redisProperties.getDatabase());
            ((RedisStandaloneConfiguration) redisConfiguration).setHostName(redisProperties.getHost());
            ((RedisStandaloneConfiguration) redisConfiguration).setPort(redisProperties.getPort());
            ((RedisStandaloneConfiguration) redisConfiguration)
                    .setPassword(RedisPassword.of(redisProperties.getPassword()));

            clientConfig = LettucePoolingClientConfiguration.builder().commandTimeout(redisProperties.getTimeout())
                    .poolConfig(genericObjectPoolConfig).build();

        }

        if (redisProperties.isSsl()) {
            clientConfig.isUseSsl();
        }

        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisConfiguration, clientConfig);
        return factory;
    }

    @Override
    public GenericObjectPoolConfig genericObjectPoolConfig() {
        RedisProperties redisProperties= SpringUtils.getBean(RedisProperties.class);
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(redisProperties.getLettuce().getPool().getMaxIdle());
        poolConfig.setMinIdle(redisProperties.getLettuce().getPool().getMinIdle());
        poolConfig.setMaxTotal(redisProperties.getLettuce().getPool().getMaxActive());
        poolConfig.setMaxWaitMillis(redisProperties.getLettuce().getPool().getMaxWait().getSeconds());
        Duration timeOut = redisProperties.getTimeout();
        Duration shutdownTimeout = redisProperties.getLettuce().getShutdownTimeout();
        return poolConfig;
    }

    @Override
    public CacheManager cacheManager(RedisConnectionFactory lettuceConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        redisCacheConfiguration = redisCacheConfiguration.entryTtl(Duration.ofMinutes(30L)) // 设置缓存的默认超时时间：30分钟
                .disableCachingNullValues() // 如果是空值，不缓存
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string())) // 设置key序列化器
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.java())); // 设置value序列化器
        return RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(lettuceConnectionFactory))
                .cacheDefaults(redisCacheConfiguration).build();
    }

    @Override
    public RedisTemplate<String, Object> getRedisTemplate(RedisConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        RedisSerializer stringSerializer = new StringRedisSerializer();
        // RedisSerializer redisObjectSerializer = new RedisObjectSerializer();
        RedisSerializer redisObjectSerializer = new RedisObjectSerializer();
        redisTemplate.setKeySerializer(stringSerializer); // key的序列化类型
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(redisObjectSerializer); // value的序列化类型
        redisTemplate.setHashValueSerializer(redisObjectSerializer); // value的序列化类型
        redisTemplate.afterPropertiesSet();

        redisTemplate.opsForValue().set("hello", "yeegro");
        return redisTemplate;
    }

    @Override
    public RedisTemplate<String, Object> getSingleRedisTemplate(RedisConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        RedisSerializer redisObjectSerializer = new RedisObjectSerializer();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer()); // key的序列化类型
        redisTemplate.setValueSerializer(redisObjectSerializer); // value的序列化类型
        redisTemplate.setHashValueSerializer(redisObjectSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Override
    public HashOperations<String, String, String> hashOperations(StringRedisTemplate stringRedisTemplate) {
        return stringRedisTemplate.opsForHash();
    }

    @Override
    public RedissonClient redissonClient() throws IOException {

        RedissonProperties redissonProperties= SpringUtils.getBean(RedissonProperties.class);
        RedisProperties redisProperties= SpringUtils.getBean(RedisProperties.class);
        Config config = null;
        Method clusterMethod = ReflectionUtils.findMethod(RedisProperties.class, "getCluster");
        Method timeoutMethod = ReflectionUtils.findMethod(RedisProperties.class, "getTimeout");
        Object timeoutValue = ReflectionUtils.invokeMethod(timeoutMethod, redisProperties);
        int timeout;
        if (null == timeoutValue) {
            timeout = 60000;
        } else if (!(timeoutValue instanceof Integer)) {
            Method millisMethod = ReflectionUtils.findMethod(timeoutValue.getClass(), "toMillis");
            timeout = ((Long) ReflectionUtils.invokeMethod(millisMethod, timeoutValue)).intValue();
        } else {
            timeout = (Integer) timeoutValue;
        }
        // spring.redis.redisson.config=classpath:redisson.yaml
        if (redissonProperties.getConfig() != null) {

            try {
                InputStream is = getConfigStream();
                config = Config.fromJSON(is);
            } catch (IOException e) {
                // trying next format
                try {
                    InputStream is = getConfigStream();
                    config = Config.fromYAML(is);
                } catch (IOException ioe) {
                    throw new IllegalArgumentException("Can't parse config", ioe);
                }
            }
        } else if (redisProperties.getSentinel() != null) {
            // 哨兵配置
            Method nodesMethod = ReflectionUtils.findMethod(RedisProperties.Sentinel.class, "getNodes");
            Object nodesValue = ReflectionUtils.invokeMethod(nodesMethod, redisProperties.getSentinel());

            String[] nodes;
            if (nodesValue instanceof String) {
                nodes = convert(Arrays.asList(((String) nodesValue).split(",")));
            } else {
                nodes = convert((List<String>) nodesValue);
            }

            config = new Config();
            config.useSentinelServers().setMasterName(redisProperties.getSentinel().getMaster())
                    .addSentinelAddress(nodes).setDatabase(redisProperties.getDatabase()).setConnectTimeout(timeout)
                    .setPassword(redisProperties.getPassword());
        } else if (clusterMethod != null && ReflectionUtils.invokeMethod(clusterMethod, redisProperties) != null) {
            // 集群配置
            Object clusterObject = ReflectionUtils.invokeMethod(clusterMethod, redisProperties);
            Method nodesMethod = ReflectionUtils.findMethod(clusterObject.getClass(), "getNodes");
            List<String> nodesObject = (List) ReflectionUtils.invokeMethod(nodesMethod, clusterObject);
            String[] nodes = convert(nodesObject);
            config = new Config();
            config.useClusterServers().addNodeAddress(nodes).setConnectTimeout(timeout)
                    .setPassword(redisProperties.getPassword());
        } else {
            // 单机redssion默认配置
            config = new Config();
            String prefix = "redis://";
            Method method = ReflectionUtils.findMethod(RedisProperties.class, "isSsl");
            if (method != null && (Boolean) ReflectionUtils.invokeMethod(method, redisProperties)) {
                prefix = "rediss://";
            }

            config.useSingleServer().setAddress(prefix + redisProperties.getHost() + ":" + redisProperties.getPort())
                    .setConnectTimeout(timeout).setDatabase(redisProperties.getDatabase())
                    .setPassword(redisProperties.getPassword());

        }

        return Redisson.create(config);
    }

    @Override
    public boolean Awake() {
        return false;
    }

    @Override
    public boolean Init() {
        return false;
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
}
