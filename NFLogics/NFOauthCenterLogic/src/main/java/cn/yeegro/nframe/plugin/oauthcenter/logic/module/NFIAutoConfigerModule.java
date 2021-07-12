package cn.yeegro.nframe.plugin.oauthcenter.logic.module;

import cn.yeegro.nframe.comm.code.module.NFIModule;
import cn.yeegro.nframe.plugin.oauthcenter.logic.authorize.NFIAuthorizeConfigProvider;
import cn.yeegro.nframe.plugin.oauthcenter.logic.functor.AUTHOR_CONFIG_EVENT;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.List;

public interface NFIAutoConfigerModule extends NFIModule {

    void AuthorizationServerEndpointsConfigurer(AuthorizationServerEndpointsConfigurer endpoints);

    void ClientDetailConfigurer(ClientDetailsServiceConfigurer clients) throws Exception;

    void  SecurityConfigurer(AuthorizationServerSecurityConfigurer security) throws Exception;

    boolean AddAuthoConfigCallBack(AUTHOR_CONFIG_EVENT cb);

    AuthorizationServerEndpointsConfigurer onTokenGranterEvent(AuthorizationServerEndpointsConfigurer endpoints,AuthorizationServerTokenServices tokenServices, AuthorizationCodeServices authorizationCodeServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory);


    boolean AuthAuthorizeConfigProvider(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config);

    void OpenAuthorizeConfigManager(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config, List<NFIAuthorizeConfigProvider> authorizeConfigProviders,String applicationName);


}
