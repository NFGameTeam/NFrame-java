package cn.yeegro.nframe.plugin.oauthcenter.logic;

import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.common.utils.SpringUtils;
import cn.yeegro.nframe.plugin.oauthcenter.logic.handler.NFIOauthLogoutHandler;
import cn.yeegro.nframe.plugin.oauthcenter.logic.handler.OauthLogoutHandler;
import cn.yeegro.nframe.plugin.oauthcenter.logic.module.NFIHandlerConfigerModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pf4j.Extension;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.*;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Extension
public class NFHandlerConfigerModule implements NFIHandlerConfigerModule {


    private NFIPluginManager pPluginManager;

    private static NFHandlerConfigerModule SingletonPtr = null;

    public static NFHandlerConfigerModule GetSingletonPtr() {
        if (null == SingletonPtr) {
            SingletonPtr = new NFHandlerConfigerModule();
            return SingletonPtr;
        } else {
            return SingletonPtr;
        }
    }

    @Override
    public AuthenticationSuccessHandler loginSuccessHandler() {
        return new SavedRequestAwareAuthenticationSuccessHandler() {

            private RequestCache requestCache = new HttpSessionRequestCache();

            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                Authentication authentication) throws IOException, ServletException {

                super.onAuthenticationSuccess(request, response, authentication);
                return;

            }
        };
    }

    @Override
    public AuthenticationFailureHandler loginFailureHandler() {
        ObjectMapper objectMapper = SpringUtils.getBean(ObjectMapper.class);
        return new AuthenticationFailureHandler() {

            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                                AuthenticationException exception) throws IOException, ServletException {
                String msg = null;
                if (exception instanceof BadCredentialsException) {
                    msg = "密码错误";
                } else {
                    msg = exception.getMessage();
                }

                Map<String, String> rsp = new HashMap<>();

                response.setStatus(HttpStatus.UNAUTHORIZED.value());

                rsp.put("code", HttpStatus.UNAUTHORIZED.value() + "");
                rsp.put("msg", msg);

                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(objectMapper.writeValueAsString(rsp));
                response.getWriter().flush();
                response.getWriter().close();

            }
        };
    }

    @Override
    public WebResponseExceptionTranslator webResponseExceptionTranslator() {
        return new DefaultWebResponseExceptionTranslator() {

            public static final String BAD_MSG = "Bad credentials";

            @Override
            public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
                // e.printStackTrace();
                OAuth2Exception oAuth2Exception;
                if (e.getMessage() != null && BAD_MSG.equals( e.getMessage())) {
                    oAuth2Exception = new InvalidGrantException("用户名或密码错误", e);
                }else if (e instanceof InvalidGrantException) {
                    oAuth2Exception = new InvalidGrantException(e.getMessage(), e);
                }else if (e instanceof InternalAuthenticationServiceException) {
                    oAuth2Exception = new InvalidGrantException(e.getMessage(), e);
                } else if (e instanceof RedirectMismatchException) {
                    oAuth2Exception = new InvalidGrantException(e.getMessage(), e);
                } else if (e instanceof InvalidScopeException) {
                    oAuth2Exception = new InvalidGrantException(e.getMessage(), e);
                } else {
                    oAuth2Exception = new UnsupportedResponseTypeException("服务内部错误", e);
                }

                ResponseEntity<OAuth2Exception> response = super.translate(oAuth2Exception);
                ResponseEntity.status(oAuth2Exception.getHttpErrorCode());
                response.getBody().addAdditionalInformation("code", oAuth2Exception.getHttpErrorCode() + "");
                response.getBody().addAdditionalInformation("msg", oAuth2Exception.getMessage());

                return response;
            }

        };
    }

    @Override
    public OAuth2WebSecurityExpressionHandler oAuth2WebSecurityExpressionHandler() {
        ApplicationContext applicationContext=SpringUtils.getAppContext();
        OAuth2WebSecurityExpressionHandler expressionHandler = new OAuth2WebSecurityExpressionHandler();
        expressionHandler.setApplicationContext(applicationContext);
        return expressionHandler;
    }

    @Override
    public NFIOauthLogoutHandler oauthLogoutHandler() {
        return new OauthLogoutHandler();
    }

    @Override
    public AuthenticationEntryPoint authenticationEntryPoint() {
        ObjectMapper objectMapper = SpringUtils.getBean(ObjectMapper.class);
        return new AuthenticationEntryPoint() {

            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response,
                                 AuthenticationException authException) throws IOException, ServletException {

                Map<String ,String > rsp =new HashMap<>();

                response.setStatus(HttpStatus.UNAUTHORIZED.value() );

                rsp.put("code", HttpStatus.UNAUTHORIZED.value() + "") ;
                rsp.put("msg", authException.getMessage()) ;

                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(objectMapper.writeValueAsString(rsp));
                response.getWriter().flush();
                response.getWriter().close();

            }
        };
    }

    @Override
    public OAuth2AccessDeniedHandler oAuth2AccessDeniedHandler() {
        ObjectMapper objectMapper = SpringUtils.getBean(ObjectMapper.class);
        return new OAuth2AccessDeniedHandler(){

            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException authException) throws IOException, ServletException {

                Map<String ,String > rsp =new HashMap<>();
                response.setContentType("application/json;charset=UTF-8");

                response.setStatus(HttpStatus.UNAUTHORIZED.value() );

                rsp.put("code", HttpStatus.UNAUTHORIZED.value() + "") ;
                rsp.put("msg", authException.getMessage()) ;

                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(objectMapper.writeValueAsString(rsp));
                response.getWriter().flush();
                response.getWriter().close();

            }
        };
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
