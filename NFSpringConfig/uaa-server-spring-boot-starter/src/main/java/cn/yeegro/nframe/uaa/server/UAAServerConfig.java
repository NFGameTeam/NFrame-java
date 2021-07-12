
package cn.yeegro.nframe.uaa.server;


import javax.sql.DataSource;

import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.comm.loader.NFPluginManager;
import cn.yeegro.nframe.common.utils.SpringUtils;
import cn.yeegro.nframe.plugin.oauthcenter.logic.module.NFIAutoConfigerModule;
import cn.yeegro.nframe.plugin.oauthcenter.logic.module.NFIResourceConfigerModule;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.stereotype.Component;
import cn.yeegro.nframe.common.auth.props.PermitUrlProperties;
import cn.yeegro.nframe.plugin.oauthcenter.logic.feign.FeignInterceptorConfig;
import cn.yeegro.nframe.common.rest.RestTemplateConfig;
import cn.yeegro.nframe.uaa.server.service.RedisAuthorizationCodeServices;
import cn.yeegro.nframe.uaa.server.service.RedisClientDetailsService;

/**
 * @author 刘斌（改）
 * @version 创建时间：2017年11月12日 上午22:57:51


 */

@Configuration
@SuppressWarnings("all")
@Import({RestTemplateConfig.class, FeignInterceptorConfig.class})
public class UAAServerConfig {


    /**
     * 声明 ClientDetails实现
     */
    @Bean
    public RedisClientDetailsService redisClientDetailsService(DataSource dataSource, RedisTemplate<String, Object> redisTemplate) {
        RedisClientDetailsService clientDetailsService = new RedisClientDetailsService(dataSource);
        clientDetailsService.setRedisTemplate(redisTemplate);
        return clientDetailsService;
    }


    @Bean
    public RandomValueAuthorizationCodeServices authorizationCodeServices(RedisTemplate<String, Object> redisTemplate) {
        RedisAuthorizationCodeServices redisAuthorizationCodeServices = new RedisAuthorizationCodeServices();
        redisAuthorizationCodeServices.setRedisTemplate(redisTemplate);
        return redisAuthorizationCodeServices;
    }


    /**
     * @author 刘斌（改）
     * @version 创建时间：2017年11月12日 上午22:57:51 默认token存储在内存中
     * DefaultTokenServices默认处理
     */
    @Component
    @Configuration
    @EnableAuthorizationServer
    @AutoConfigureAfter(AuthorizationServerEndpointsConfigurer.class)
    public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

       private NFIPluginManager pPluginManager;
       private NFIAutoConfigerModule m_pAutoConfigerModule;

        AuthorizationServerConfig(ApplicationContext context) {
            // 在初始化AutoConfiguration时会自动传入ApplicationContext
            SpringUtils.setAppContext(context);
            pPluginManager= NFPluginManager.GetSingletonPtr();
            m_pAutoConfigerModule=pPluginManager.FindModule(NFIAutoConfigerModule.class);
        }
        /**
         * 配置身份认证器，配置认证方式，TokenStore，TokenGranter，OAuth2RequestFactory
         */
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

            m_pAutoConfigerModule.AuthorizationServerEndpointsConfigurer(endpoints);

        }

        /**
         * 配置应用名称 应用id
         * 配置OAuth2的客户端相关信息
         */
        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

            m_pAutoConfigerModule.ClientDetailConfigurer(clients);
        }

        /**
         * 对应于配置AuthorizationServer安全认证的相关信息，创建ClientCredentialsTokenEndpointFilter核心过滤器
         */
        @Override
        public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {

            m_pAutoConfigerModule.SecurityConfigurer(security);

        }

    }

    @Configuration
    @EnableResourceServer
    @EnableConfigurationProperties(PermitUrlProperties.class)
    public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

        private NFIPluginManager pPluginManager;
        private NFIResourceConfigerModule m_pResourceConfigerModule;
        ResourceServerConfig(ApplicationContext context) {
            // 在初始化AutoConfiguration时会自动传入ApplicationContext
            SpringUtils.setAppContext(context);
            pPluginManager= NFPluginManager.GetSingletonPtr();
            m_pResourceConfigerModule=pPluginManager.FindModule(NFIResourceConfigerModule.class);
        }

        public void configure(WebSecurity web) throws Exception {
            m_pResourceConfigerModule.WebSecurityConfiger(web);
        }

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {

            m_pResourceConfigerModule.ResourceConfigurer(resources);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            m_pResourceConfigerModule.HttpSecurityConfiger(http);
        }

    }


}
