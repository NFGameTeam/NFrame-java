package cn.yeegro.nframe.client.config;

import org.springframework.context.annotation.Configuration;

import com.didispace.swagger.butler.EnableSwaggerButler;

/**
 * @author 作者 zoocee(改)
 * @version 创建时间：2018年4月5日 下午19:52:21 类说明
 * swagger 聚合文档配置
 * zuul routers 映射具体服务的/v2/api-docs swagger
 */
@Configuration
@EnableSwaggerButler
public class SwaggerConfig {

}
