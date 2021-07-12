package cn.yeegro.nframe.plugin.oauthcenter.logic;

import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.common.utils.SpringUtils;
import cn.yeegro.nframe.plugin.oauthcenter.logic.module.NFITokenStoreConfigerModule;
import cn.yeegro.nframe.plugin.oauthcenter.logic.token.*;
import org.pf4j.Extension;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.util.Assert;

import javax.sql.DataSource;

@Extension
public class NFTokenStoreConfigerModule implements NFITokenStoreConfigerModule {

    private NFIPluginManager pPluginManager;

    private static NFTokenStoreConfigerModule SingletonPtr = null;

    public static NFTokenStoreConfigerModule GetSingletonPtr() {
        if (null == SingletonPtr) {
            SingletonPtr = new NFTokenStoreConfigerModule();
            return SingletonPtr;
        } else {
            return SingletonPtr;
        }
    }

    @Override
    public JdbcTokenStore jdbcTokenStore() {
        DataSource dataSource = SpringUtils.getBean(DataSource.class);
        return new JdbcTokenStore( dataSource ) ;
    }

    @Override
    public NFIRedisTemplateTokenStore redisTokenStore(RedisConnectionFactory connectionFactory) {
        Assert.state(connectionFactory != null, "connectionFactory must be provided");
        RedisTemplateTokenStore redisTemplateStore = new RedisTemplateTokenStore(connectionFactory)  ;
        return redisTemplateStore ;
    }

    @Override
    public JwtTokenStore jwtTokenStore() {
        return new JwtTokenStore( jwtAccessTokenConverter() );
    }

    @Override
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new ResJwtAccessTokenConverter();
        accessTokenConverter.setSigningKey("ocp");
        return accessTokenConverter ;
    }

    @Override
    public NFISmsCodeAuthenticationToken getSmsCodeAuthenticationToken(String mobile) {
        return new SmsCodeAuthenticationToken(mobile);
    }

    @Override
    public Authentication SmsCodeAuthenticationProvider_authenticate(Authentication authentication) throws AuthenticationException {

        UserDetailsService userDetailsService = SpringUtils.getBean(UserDetailsService.class);

        SmsCodeAuthenticationToken token = (SmsCodeAuthenticationToken) authentication;

        UserDetails userDetails = userDetailsService.loadUserByUsername((String) token.getPrincipal());

        if (userDetails == null) {
            throw new AuthenticationCredentialsNotFoundException("用户不存在");
        } else if (!userDetails.isEnabled()) {
            throw new DisabledException("用户已作废");
        }
        SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(userDetails, userDetails.getAuthorities());
        // 需要把未认证中的一些信息copy到已认证的token中
        authenticationResult.setDetails(token);
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(aClass);
    }

    @Override
    public NFIResJwtTokenStore getResJwtTokenStore(JwtAccessTokenConverter jwtTokenEnhancer) {
        return new ResJwtTokenStore(jwtTokenEnhancer);
    }

    @Override
    public boolean Awake() {
        return false;
    }

    @Override
    public boolean Init() {
        return false;
    }

    @Override
    public boolean AfterInit() {
        return false;
    }

    @Override
    public boolean CheckConfig() {
        return false;
    }

    @Override
    public boolean ReadyExecute() {
        return false;
    }

    @Override
    public boolean Execute() {
        return false;
    }

    @Override
    public boolean BeforeShut() {
        return false;
    }

    @Override
    public boolean Shut() {
        return false;
    }

    @Override
    public boolean Finalize() {
        return false;
    }

    @Override
    public boolean OnReloadPlugin() {
        return false;
    }
}
