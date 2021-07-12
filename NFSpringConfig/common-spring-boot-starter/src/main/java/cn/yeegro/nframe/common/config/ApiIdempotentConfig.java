package cn.yeegro.nframe.common.config;

import javax.annotation.Resource;

import cn.yeegro.nframe.common.utils.SpringUtils;
import cn.yeegro.nframe.components.nosql.redis.NFIRedisModule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import cn.yeegro.nframe.common.interceptor.AccessLimitInterceptor;
import cn.yeegro.nframe.common.interceptor.ApiIdempotentInterceptor;

/**


 */
@Configuration
@ConditionalOnClass(WebMvcConfigurer.class)
public class ApiIdempotentConfig implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        RedisTemplate<String, Object> redisTemplate= SpringUtils.getBean(RedisTemplate.class);

        NFIRedisModule redisModule= SpringUtils.getBean(NFIRedisModule.class);

        registry.addInterceptor(new AccessLimitInterceptor(redisModule));
        registry.addInterceptor(new ApiIdempotentInterceptor(redisTemplate)).addPathPatterns("/**");


    }
}
