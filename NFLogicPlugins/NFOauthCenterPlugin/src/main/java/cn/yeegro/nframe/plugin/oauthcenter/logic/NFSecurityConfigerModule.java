package cn.yeegro.nframe.plugin.oauthcenter.logic;

import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.common.auth.props.PermitUrlProperties;
import cn.yeegro.nframe.common.utils.SpringUtils;
import cn.yeegro.nframe.plugin.oauthcenter.logic.authorize.NFIAuthorizeConfigManager;
import cn.yeegro.nframe.plugin.oauthcenter.logic.handler.NFIOauthLogoutHandler;
import cn.yeegro.nframe.plugin.oauthcenter.logic.module.NFISecurityConfigerModule;
import cn.yeegro.nframe.uaa.server.constant.SecurityConstant;
import cn.yeegro.nframe.uaa.server.provider.SmsCodeAuthenticationProvider;
import org.pf4j.Extension;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@Extension
public class NFSecurityConfigerModule implements NFISecurityConfigerModule {

    private NFIPluginManager pPluginManager;

    private static NFSecurityConfigerModule SingletonPtr=null;

    public static NFSecurityConfigerModule GetSingletonPtr()
    {
        if (null==SingletonPtr) {
            SingletonPtr=new NFSecurityConfigerModule();
            return SingletonPtr;
        }
        return SingletonPtr;
    }

    @Override
    public void WebSecurityConfiger(WebSecurity web) throws Exception {

        PermitUrlProperties permitUrlProperties= SpringUtils.getBean(PermitUrlProperties.class);

        web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security",
                "/swagger-ui.html", "/webjars/**", "/doc.html", "/login.html");
        web.ignoring().antMatchers("/js/**");
        web.ignoring().antMatchers("/css/**");
        web.ignoring().antMatchers("/health");
        // 忽略登录界面
        web.ignoring().antMatchers("/login.html");
        web.ignoring().antMatchers("/index.html");
        web.ignoring().antMatchers("/oauth/user/token");
        web.ignoring().antMatchers("/oauth/client/token");
        web.ignoring().antMatchers("/validata/code/**");
        web.ignoring().antMatchers("/sms/**");
        web.ignoring().antMatchers("/authentication/**");
        web.ignoring().antMatchers(permitUrlProperties.getIgnored());
    }

    @Override
    public void HttpSecurityConfiger(HttpSecurity http) throws Exception {

        AuthenticationSuccessHandler authenticationSuccessHandler= SpringUtils.getBean(AuthenticationSuccessHandler.class);
        AuthenticationFailureHandler authenticationFailureHandler= SpringUtils.getBean(AuthenticationFailureHandler.class);
        AuthenticationEntryPoint authenticationEntryPoint= SpringUtils.getBean(AuthenticationEntryPoint.class);
        NFIOauthLogoutHandler oauthLogoutHandler= SpringUtils.getBean(NFIOauthLogoutHandler.class);
        SmsCodeAuthenticationProvider smsCodeAuthenticationProvider= SpringUtils.getBean(SmsCodeAuthenticationProvider.class);
        http.csrf().disable();

        http.authorizeRequests()
                .anyRequest().authenticated();
        http.formLogin().loginPage(SecurityConstant.LOGIN_PAGE).loginProcessingUrl(SecurityConstant.LOGIN_PROCESSING_URL)
                .successHandler(authenticationSuccessHandler).failureHandler(authenticationFailureHandler);

        // 基于密码 等模式可以无session,不支持授权码模式
        if (authenticationEntryPoint != null) {
            http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        } else {
            // 授权码模式单独处理，需要session的支持，此模式可以支持所有oauth2的认证
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
        }

        http.logout().logoutSuccessUrl(SecurityConstant.LOGIN_PAGE)
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                .addLogoutHandler(oauthLogoutHandler).clearAuthentication(true);

        //注册到AuthenticationManager中去 增加支持SmsCodeAuthenticationToken
        http.authenticationProvider(smsCodeAuthenticationProvider);

        // http.logout().logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler);
        // 解决不允许显示在iframe的问题
        http.headers().frameOptions().disable();
        http.headers().cacheControl();
    }

    @Override
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        UserDetailsService userDetailsService= SpringUtils.getBean(UserDetailsService.class);
        PasswordEncoder passwordEncoder= SpringUtils.getBean(PasswordEncoder.class);
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    public void ClientWebSecurityConfiger(WebSecurity web) throws Exception {
        PermitUrlProperties permitUrlProperties= SpringUtils.getBean(PermitUrlProperties.class);
        web.ignoring().antMatchers(permitUrlProperties.getIgnored());
    }

    @Override
    public void ClientResourceServerSecurityConfigurer(ResourceServerSecurityConfigurer resources) throws Exception {

        TokenStore tokenStore= SpringUtils.getBean(TokenStore.class);
        AuthenticationEntryPoint authenticationEntryPoint= SpringUtils.getBean(AuthenticationEntryPoint.class);
        OAuth2WebSecurityExpressionHandler expressionHandler= SpringUtils.getBean(OAuth2WebSecurityExpressionHandler.class);
        OAuth2AccessDeniedHandler oAuth2AccessDeniedHandler= SpringUtils.getBean(OAuth2AccessDeniedHandler.class);
        if (tokenStore != null) {
            resources.tokenStore(tokenStore);
        }
        resources.stateless(true);
        // 自定义异常处理端口
        resources.authenticationEntryPoint(authenticationEntryPoint);
        resources.expressionHandler(expressionHandler);
        resources.accessDeniedHandler(oAuth2AccessDeniedHandler);
    }

    @Override
    public void ClientHttpSecurityConfiger(HttpSecurity http) throws Exception {
        NFIAuthorizeConfigManager authorizeConfigManager=SpringUtils.getBean(NFIAuthorizeConfigManager.class);
        http.csrf().disable();
        http.headers().frameOptions().disable();

        authorizeConfigManager.config(http.authorizeRequests());
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
