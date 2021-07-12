package com.shsnc.annotation;

import com.shsnc.config.ElasticJobAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 *   ElasticJob 开启注解
 *      在Spring Boot 启动类上加此注解开启自动配置
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({ElasticJobAutoConfiguration.class})
public @interface EnableElasticJob {
}
