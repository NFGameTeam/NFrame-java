package cn.yeegro.nframe.plugin.oauthcenter.logic.module;

import cn.yeegro.nframe.comm.code.module.NFIModule;
import cn.yeegro.nframe.plugin.oauthcenter.logic.token.NFIRedisTemplateTokenStore;
import cn.yeegro.nframe.plugin.oauthcenter.logic.token.NFIResJwtTokenStore;
import cn.yeegro.nframe.plugin.oauthcenter.logic.token.NFISmsCodeAuthenticationToken;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

public interface NFITokenStoreConfigerModule extends NFIModule {

    JdbcTokenStore jdbcTokenStore();
    NFIRedisTemplateTokenStore redisTokenStore(RedisConnectionFactory connectionFactory);

    JwtTokenStore jwtTokenStore();

    JwtAccessTokenConverter jwtAccessTokenConverter();

    NFISmsCodeAuthenticationToken getSmsCodeAuthenticationToken(String mobile);

    Authentication SmsCodeAuthenticationProvider_authenticate(Authentication authentication) throws AuthenticationException;

    boolean supports(Class<?> aClass);

    NFIResJwtTokenStore getResJwtTokenStore(JwtAccessTokenConverter jwtTokenEnhancer);

}
