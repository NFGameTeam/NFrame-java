package cn.yeegro.nframe.plugin.oauthcenter.logic;

import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.comm.loader.NFPluginManager;
import cn.yeegro.nframe.common.auth.props.PermitUrlProperties;
import cn.yeegro.nframe.common.utils.SpringUtils;
import cn.yeegro.nframe.plugin.oauthcenter.logic.authorize.NFIAuthorizeConfigProvider;
import cn.yeegro.nframe.plugin.oauthcenter.logic.functor.AUTHOR_CONFIG_EVENT;
import cn.yeegro.nframe.plugin.oauthcenter.logic.module.NFIAutoConfigerModule;
import cn.yeegro.nframe.plugin.oauthcenter.logic.module.NFITokenGranterModule;
import cn.yeegro.nframe.uaa.server.service.RedisClientDetailsService;
import org.pf4j.Extension;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import cn.yeegro.nframe.plugin.oauthcenter.logic.constant.NFAuthServiceConstant;

import java.util.ArrayList;
import java.util.List;

@Extension
public class NFAutoConfigerModule implements NFIAutoConfigerModule {

    private NFIPluginManager pPluginManager;

    private static NFAutoConfigerModule SingletonPtr=null;

    public static NFAutoConfigerModule GetSingletonPtr()
    {
        if (null==SingletonPtr) {
            SingletonPtr=new NFAutoConfigerModule();
            return SingletonPtr;
        }
        return SingletonPtr;
    }
    NFAutoConfigerModule(){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        mtAuthoConfigCallback=new ArrayList<>();
    }

    List<AUTHOR_CONFIG_EVENT> mtAuthoConfigCallback;


    private NFITokenGranterModule m_pTokenGranter;

    @Override
    public void AuthorizationServerEndpointsConfigurer(AuthorizationServerEndpointsConfigurer endpoints) {
        //加载配置身份认证器，配置认证方式
        m_pTokenGranter = pPluginManager.FindModule(NFITokenGranterModule.class);
        for (AUTHOR_CONFIG_EVENT event:m_pTokenGranter.mx_ConfigEvents)
        {
            AddAuthoConfigCallBack(event);
        }

        TokenStore m_tokenStore= SpringUtils.getBean(TokenStore.class);
        AuthenticationManager m_authenticationManager= SpringUtils.getBean(AuthenticationManager.class);
        UserDetailsService m_userDetailsService=SpringUtils.getBean(UserDetailsService.class);
        JwtAccessTokenConverter m_jwtAccessTokenConverter=SpringUtils.getBean(JwtAccessTokenConverter.class);
        RandomValueAuthorizationCodeServices m_authorizationCodeServices=SpringUtils.getBean(RandomValueAuthorizationCodeServices.class);
        WebResponseExceptionTranslator m_webResponseExceptionTranslator=SpringUtils.getBean(WebResponseExceptionTranslator.class);
        //通用处理
        endpoints.tokenStore(m_tokenStore).authenticationManager(m_authenticationManager)
                // 支持
                .userDetailsService(m_userDetailsService);

        if (m_tokenStore instanceof JwtTokenStore) {
            endpoints.accessTokenConverter(m_jwtAccessTokenConverter);
        }

        //处理授权码
        endpoints.authorizationCodeServices(m_authorizationCodeServices);
        // 处理 ExceptionTranslationFilter 抛出的异常
        endpoints.exceptionTranslator(m_webResponseExceptionTranslator);

        //处理oauth 模式
        ClientDetailsService clientDetails = endpoints.getClientDetailsService();
        AuthorizationServerTokenServices tokenServices = endpoints.getTokenServices();
        AuthorizationCodeServices authorizationCodeServices = endpoints.getAuthorizationCodeServices();
        OAuth2RequestFactory requestFactory = endpoints.getOAuth2RequestFactory();

        endpoints= onTokenGranterEvent(endpoints,tokenServices,authorizationCodeServices,clientDetails,requestFactory);

        //最后加入组合模式
        endpoints.tokenGranter(new CompositeTokenGranter(m_pTokenGranter.getTokenGranterList()));
    }

    @Override
    public void ClientDetailConfigurer(ClientDetailsServiceConfigurer clients) throws Exception {
        RedisClientDetailsService m_redisClientDetailsService= SpringUtils.getBean(RedisClientDetailsService.class);
        clients.withClientDetails(m_redisClientDetailsService);
        m_redisClientDetailsService.loadAllClientToCache();
    }

    @Override
    public void SecurityConfigurer(AuthorizationServerSecurityConfigurer security) throws Exception {
        // url:/oauth/token_key,exposes
        security.tokenKeyAccess("permitAll()")
                /// public key for token
                /// verification if using
                /// JWT tokens
                // url:/oauth/check_token
                .checkTokenAccess("isAuthenticated()")
                // allow check token
                .allowFormAuthenticationForClients();
    }

    @Override
    public boolean AddAuthoConfigCallBack(AUTHOR_CONFIG_EVENT cb) {
        return mtAuthoConfigCallback.add(cb);
    }

    @Override
    public AuthorizationServerEndpointsConfigurer onTokenGranterEvent(AuthorizationServerEndpointsConfigurer endpoints,AuthorizationServerTokenServices tokenServices, AuthorizationCodeServices authorizationCodeServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
        for (int i = 0; i < mtAuthoConfigCallback.size(); i++) {
            AUTHOR_CONFIG_EVENT pFunPtr=mtAuthoConfigCallback.get(i);
            TokenGranter tokenGranter= pFunPtr.opt(tokenServices, authorizationCodeServices, clientDetailsService, requestFactory);
            endpoints.tokenGranter(tokenGranter);
        }
        return endpoints;
    }

    @Override
    public boolean AuthAuthorizeConfigProvider(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        PermitUrlProperties permitUrlProperties= SpringUtils.getBean(PermitUrlProperties.class);
        // 免token登录设置
        config.antMatchers(permitUrlProperties.getIgnored()).permitAll();
        config.requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll() ;//监控断点放权
        //前后分离时需要带上
        config.antMatchers(HttpMethod.OPTIONS).permitAll();

        return true;
    }

    @Override
    public void OpenAuthorizeConfigManager(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config,List<NFIAuthorizeConfigProvider>authorizeConfigProviders,String applicationName) {
        //设置访问
        for (NFIAuthorizeConfigProvider authorizeConfigProvider : authorizeConfigProviders) {
            authorizeConfigProvider.config(config) ;
        }

        int flag = 0 ;


        if(NFAuthServiceConstant.GATEWAY_SERVICE.equalsIgnoreCase(applicationName)){
            //网关API权限
            flag = 1 ;

        } else if (NFAuthServiceConstant.AUTH_SERVICE.equalsIgnoreCase(applicationName)){
            //认证中心
            flag=  2 ;

        }

        switch(flag){

            case 1 :
                //方式1：网关根据API权限处理，根据应用分配服务权限，建议采用此方式
//				if(rbacService!=null){
//					config.anyRequest().access("@rbacService.hasPermission(request, authentication)") ;
//				}
                //方式2：强制校验token 非API权限处理，此方式不需要页面根据应用分配服务权限
                config.anyRequest().authenticated() ;
                break ;
            case 2 :
                // 认证中心 强制校验token
                config.anyRequest().authenticated() ;
                break ;
            default:
                //方式1：非网关可以采用不强制鉴权模式
                //config.anyRequest().permitAll();
                //方式2：强制校验token
//				config.anyRequest().authenticated();
                config.anyRequest().permitAll();
        }
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
