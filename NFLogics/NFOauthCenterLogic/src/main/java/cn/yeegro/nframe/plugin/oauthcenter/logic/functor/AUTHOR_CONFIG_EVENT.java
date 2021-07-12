package cn.yeegro.nframe.plugin.oauthcenter.logic.functor;

import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

public interface AUTHOR_CONFIG_EVENT {

    TokenGranter opt(AuthorizationServerTokenServices tokenServices, AuthorizationCodeServices authorizationCodeServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory);
}
