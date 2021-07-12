package cn.yeegro.nframe.components.nosql.redis;

import cn.yeegro.nframe.comm.code.api.NFModule;
import cn.yeegro.nframe.common.utils.SpringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.redisson.api.RedissonClient;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class NFIRedisConfigModule extends NFModule {

    public abstract RedisConnectionFactory lettuceConnectionFactory(GenericObjectPoolConfig genericObjectPoolConfig);

    public abstract GenericObjectPoolConfig genericObjectPoolConfig();

    public abstract CacheManager cacheManager(RedisConnectionFactory lettuceConnectionFactory );

    public abstract RedisTemplate<String, Object> getRedisTemplate(RedisConnectionFactory lettuceConnectionFactory);

    public abstract RedisTemplate<String, Object> getSingleRedisTemplate(RedisConnectionFactory lettuceConnectionFactory);

    public abstract HashOperations<String, String, String> hashOperations(StringRedisTemplate stringRedisTemplate);

    public abstract RedissonClient redissonClient() throws IOException;

    protected String[] convert(List<String> nodesObject) {
        List<String> nodes = new ArrayList<String>(nodesObject.size());
        for (String node : nodesObject) {
            if (!node.startsWith("redis://") && !node.startsWith("rediss://")) {
                nodes.add("redis://" + node);
            } else {
                nodes.add(node);
            }
        }
        return nodes.toArray(new String[nodes.size()]);
    }

    protected InputStream getConfigStream() throws IOException {
        ApplicationContext ctx= SpringUtils.getAppContext();
        RedissonProperties redissonProperties=SpringUtils.getBean(RedissonProperties.class);
        Resource resource = ctx.getResource(redissonProperties.getConfig());
        InputStream is = resource.getInputStream();
        return is;
    }
}
