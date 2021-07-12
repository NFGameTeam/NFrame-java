package cn.yeegro.nframe.plugin.oauthcenter.logic.module;

import cn.yeegro.nframe.comm.code.module.NFIModule;
import cn.yeegro.nframe.plugin.oauthcenter.logic.handler.NFIOauthLogoutHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public interface NFIHandlerConfigerModule extends NFIModule {

    AuthenticationSuccessHandler loginSuccessHandler();

    AuthenticationFailureHandler loginFailureHandler();

    WebResponseExceptionTranslator webResponseExceptionTranslator();

    OAuth2WebSecurityExpressionHandler oAuth2WebSecurityExpressionHandler();

    NFIOauthLogoutHandler oauthLogoutHandler();

    AuthenticationEntryPoint authenticationEntryPoint();

    OAuth2AccessDeniedHandler oAuth2AccessDeniedHandler();
}
