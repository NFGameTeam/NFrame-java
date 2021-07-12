package cn.yeegro.nframe.plugin.oauthcenter.logic.module;

import cn.yeegro.nframe.comm.code.module.NFIModule;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

public interface NFISecurityConfigerModule extends NFIModule {

    void WebSecurityConfiger(WebSecurity web)throws Exception;
    void HttpSecurityConfiger(HttpSecurity http)throws Exception;
    void globalUserDetails(AuthenticationManagerBuilder auth)throws Exception;

    void ClientWebSecurityConfiger(WebSecurity web)throws Exception;
    void ClientResourceServerSecurityConfigurer(ResourceServerSecurityConfigurer resources)throws Exception;
    void ClientHttpSecurityConfiger(HttpSecurity http)throws Exception;
}
