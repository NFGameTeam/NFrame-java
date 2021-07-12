package cn.yeegro.nframe.plugin.oauthcenter.logic.config;

import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.comm.loader.NFPluginManager;
import cn.yeegro.nframe.common.utils.SpringUtils;
import cn.yeegro.nframe.plugin.oauthcenter.logic.handler.NFIOauthLogoutHandler;
import cn.yeegro.nframe.plugin.oauthcenter.logic.module.NFIHandlerConfigerModule;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * @author 刘斌（改）
 * @version 创建时间：2017年11月12日 上午22:57:51


 */
@Component
@Configuration
@SuppressWarnings("all")
public class SecurityHandlerConfig {


    private NFIPluginManager pPluginManager;
    private NFIHandlerConfigerModule m_pHandlerConfigerModule;

    SecurityHandlerConfig(ApplicationContext context) {
        // 在初始化AutoConfiguration时会自动传入ApplicationContext
        SpringUtils.setAppContext(context);
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pHandlerConfigerModule = pPluginManager.FindModule(NFIHandlerConfigerModule.class);
    }

    // url匹配器
//	private AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * 登陆成功，返回Token 装配此bean不支持授权码模式
     *
     * @return
     */
    @Bean
    public AuthenticationSuccessHandler loginSuccessHandler() {
        return m_pHandlerConfigerModule.loginSuccessHandler();
    }

    /**
     * 登陆失败
     *
     * @return
     */
    @Bean
    public AuthenticationFailureHandler loginFailureHandler() {
        return m_pHandlerConfigerModule.loginFailureHandler();

    }


    @Bean
    public WebResponseExceptionTranslator webResponseExceptionTranslator() {
        return m_pHandlerConfigerModule.webResponseExceptionTranslator();
    }

    @Bean
    public OAuth2WebSecurityExpressionHandler oAuth2WebSecurityExpressionHandler(ApplicationContext applicationContext) {
        SpringUtils.setAppContext(applicationContext);
        return m_pHandlerConfigerModule.oAuth2WebSecurityExpressionHandler();
    }

    @Bean
    public NFIOauthLogoutHandler oauthLogoutHandler() {
        return m_pHandlerConfigerModule.oauthLogoutHandler();
    }

    /**
     * 未登录，返回401
     *
     * @return
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return m_pHandlerConfigerModule.authenticationEntryPoint();
    }

    /**
     * 处理spring security oauth 处理失败返回消息格式
     * code
     * resp_desc
     */
    @Bean
    public OAuth2AccessDeniedHandler oAuth2AccessDeniedHandler() {
        return m_pHandlerConfigerModule.oAuth2AccessDeniedHandler();
    }


}
