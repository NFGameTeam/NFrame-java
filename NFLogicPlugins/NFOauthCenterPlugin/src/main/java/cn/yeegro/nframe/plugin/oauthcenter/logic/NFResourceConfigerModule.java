package cn.yeegro.nframe.plugin.oauthcenter.logic;

import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.comm.loader.NFPluginManager;
import cn.yeegro.nframe.common.auth.props.PermitUrlProperties;
import cn.yeegro.nframe.plugin.oauthcenter.logic.constant.UaaConstant;
import cn.yeegro.nframe.common.utils.SpringUtils;
import cn.yeegro.nframe.plugin.oauthcenter.logic.module.NFIResourceConfigerModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pf4j.Extension;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.AntPathMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@Extension
public class NFResourceConfigerModule implements NFIResourceConfigerModule {

    private NFIPluginManager pPluginManager;

    private static NFResourceConfigerModule SingletonPtr = null;

    public static NFResourceConfigerModule GetSingletonPtr() {
        if (null == SingletonPtr) {
            SingletonPtr = new NFResourceConfigerModule();
            return SingletonPtr;
        } else {
            return SingletonPtr;
        }
    }

    NFResourceConfigerModule(){
        pPluginManager = NFPluginManager.GetSingletonPtr();
    }

    @Override
    public void WebSecurityConfiger(WebSecurity web) {
        web.ignoring().antMatchers("/health");
        web.ignoring().antMatchers("/oauth/user/token");
        web.ignoring().antMatchers("/oauth/client/token");
    }

    @Override
    public void ResourceConfigurer(ResourceServerSecurityConfigurer resources) throws Exception {

        TokenStore m_tokenStore = SpringUtils.getBean(TokenStore.class);
        OAuth2WebSecurityExpressionHandler m_expressionHandler = SpringUtils.getBean(OAuth2WebSecurityExpressionHandler.class);
        ObjectMapper m_objectMapper = SpringUtils.getBean(ObjectMapper.class);
        if (m_tokenStore != null) {
            resources.tokenStore(m_tokenStore);
        }
        resources.stateless(true);
        resources.expressionHandler(m_expressionHandler);
        // 自定义异常处理端口
        resources.authenticationEntryPoint(new AuthenticationEntryPoint() {

            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response,
                                 AuthenticationException authException) throws IOException, ServletException {

                Map<String, String> rsp = new HashMap<>();

                response.setStatus(HttpStatus.UNAUTHORIZED.value());

                rsp.put("code", HttpStatus.UNAUTHORIZED.value() + "");
                rsp.put("msg", authException.getMessage());

                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(m_objectMapper.writeValueAsString(rsp));
                response.getWriter().flush();
                response.getWriter().close();

            }
        });
        resources.accessDeniedHandler(new OAuth2AccessDeniedHandler() {

            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException authException) throws IOException, ServletException {

                Map<String, String> rsp = new HashMap<>();
                response.setContentType("application/json;charset=UTF-8");

                response.setStatus(HttpStatus.UNAUTHORIZED.value());

                rsp.put("code", HttpStatus.UNAUTHORIZED.value() + "");
                rsp.put("msg", authException.getMessage());

                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(m_objectMapper.writeValueAsString(rsp));
                response.getWriter().flush();
                response.getWriter().close();

            }
        });
    }

    @Override
    public void HttpSecurityConfiger(HttpSecurity http) throws Exception {

        PermitUrlProperties m_permitUrlProperties = SpringUtils.getBean(PermitUrlProperties.class);

        http.requestMatcher(
                /**
                 * 判断来源请求是否包含oauth2授权信息
                 */
                new RequestMatcher() {
                    private AntPathMatcher antPathMatcher = new AntPathMatcher();

                    @Override
                    public boolean matches(HttpServletRequest request) {
                        // 请求参数中包含access_token参数
                        if (request.getParameter(OAuth2AccessToken.ACCESS_TOKEN) != null) {
                            return true;
                        }

                        // 头部的Authorization值以Bearer开头
                        String auth = request.getHeader(UaaConstant.AUTHORIZATION);
                        if (auth != null) {
                            if (auth.startsWith(OAuth2AccessToken.BEARER_TYPE)) {
                                return true;
                            }
                        }

                        // 认证中心url特殊处理，返回true的，不会跳转login.html页面
                        if (antPathMatcher.match(request.getRequestURI(), "/api-auth/oauth/userinfo")) {
                            return true;
                        }
                        if (antPathMatcher.match(request.getRequestURI(), "/api-auth/oauth/remove/token")) {
                            return true;
                        }
                        if (antPathMatcher.match(request.getRequestURI(), "/api-auth/oauth/get/token")) {
                            return true;
                        }
                        if (antPathMatcher.match(request.getRequestURI(), "/api-auth/oauth/refresh/token")) {
                            return true;
                        }

                        if (antPathMatcher.match(request.getRequestURI(), "/api-auth/oauth/token/list")) {
                            return true;
                        }

                        if (antPathMatcher.match("/**/clients/**", request.getRequestURI())) {
                            return true;
                        }

                        if (antPathMatcher.match("/**/services/**", request.getRequestURI())) {
                            return true;
                        }
                        if (antPathMatcher.match("/**/redis/**", request.getRequestURI())) {
                            return true;
                        }

                        return false;
                    }
                }

        ).authorizeRequests().antMatchers(m_permitUrlProperties.getIgnored()).permitAll()
                .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
                .anyRequest()
                .authenticated();
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
