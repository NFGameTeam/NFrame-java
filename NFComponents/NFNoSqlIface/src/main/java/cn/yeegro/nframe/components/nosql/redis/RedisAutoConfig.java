package cn.yeegro.nframe.components.nosql.redis;

import java.io.IOException;

import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.comm.loader.NFPluginManager;
import cn.yeegro.nframe.common.utils.SpringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;


import io.lettuce.core.RedisClient;

/**
 * @author 作者 zoocee(改)
 * @version 创建时间：2017年04月23日 下午20:01:06 类说明 redis自动装配
 */
@Configuration
@EnableCaching
@SuppressWarnings("all")
@AutoConfigureBefore(RedisTemplate.class)
@EnableConfigurationProperties(RedissonProperties.class)
public class RedisAutoConfig {


	private NFIPluginManager pPluginManager;

	private NFIRedisConfigModule m_pRedisConfigModule;

	private NFIRedisModule m_pRedisModule;

	RedisAutoConfig(ApplicationContext context) {
		// 在初始化AutoConfiguration时会自动传入ApplicationContext
		SpringUtils.setAppContext(context);
		pPluginManager= NFPluginManager.GetSingletonPtr();
		m_pRedisConfigModule=pPluginManager.FindModule(NFIRedisConfigModule.class);
		//初始redis的时候是没有进行连接的空对象，需要调用CreateRedis(....)才能使用，见下面代码
		m_pRedisModule=pPluginManager.FindModule(NFIRedisModule.class);
	}


	@Bean(destroyMethod = "destroy")
	@ConditionalOnClass(RedisClient.class)
	public RedisConnectionFactory lettuceConnectionFactory(GenericObjectPoolConfig genericObjectPoolConfig) {
		return m_pRedisConfigModule.lettuceConnectionFactory(genericObjectPoolConfig);
	}

	/**
	 * GenericObjectPoolConfig 连接池配置
	 */
	@Bean
	public GenericObjectPoolConfig genericObjectPoolConfig() {
		return m_pRedisConfigModule.genericObjectPoolConfig();
	}

 
	@Bean 
	public CacheManager cacheManager(LettuceConnectionFactory lettuceConnectionFactory ) {
		return m_pRedisConfigModule.cacheManager(lettuceConnectionFactory);
	}

	/**
	 * 适配redis cluster单节点
	 */
	@Primary
	@Bean("redisTemplate")
	// 没有此属性就不会装配bean 如果是单个redis 将此注解注释掉
	@ConditionalOnProperty(name = "spring.redis.cluster.nodes", matchIfMissing = false)
	public RedisTemplate<String, Object> getRedisTemplate(RedisConnectionFactory lettuceConnectionFactory) {

		return m_pRedisConfigModule.getRedisTemplate(lettuceConnectionFactory);
	}

	/**
	 * 适配redis单节点
	 */
	@Primary
	@Bean("redisTemplate")
	@ConditionalOnProperty(name = "spring.redis.host", matchIfMissing = true)
	public RedisTemplate<String, Object> getSingleRedisTemplate(RedisConnectionFactory lettuceConnectionFactory) {
		return m_pRedisConfigModule.getSingleRedisTemplate(lettuceConnectionFactory);
	}

	@Bean
	public HashOperations<String, String, String> hashOperations(StringRedisTemplate stringRedisTemplate) {
		return m_pRedisConfigModule.hashOperations(stringRedisTemplate);
	}

	/**
	 * redis工具类
	 */
	@Bean("redisModule")
	public NFIRedisModule redisUtil(RedisConnectionFactory lettuceConnectionFactory,
									StringRedisTemplate stringRedisTemplate, HashOperations<String, String, String> hashOperations) {
		m_pRedisModule.CreateRedis(lettuceConnectionFactory, stringRedisTemplate, hashOperations);
		m_pRedisModule=pPluginManager.FindModule(NFIRedisModule.class);
		return m_pRedisModule;
	}

	@Bean(destroyMethod = "shutdown")
	@ConditionalOnProperty(name = "spring.redis.redisson.enable", matchIfMissing = false, havingValue = "true")
	@ConditionalOnMissingBean(RedissonClient.class)
	public RedissonClient redissonClient() throws IOException {
		return m_pRedisConfigModule.redissonClient();
	}


}
