package cn.yeegro.nframe.plugin.oauthcenter.logic.module;

import cn.yeegro.nframe.comm.code.module.NFIModule;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

public interface NFIResourceConfigerModule extends NFIModule {

    void WebSecurityConfiger(WebSecurity web);

    void ResourceConfigurer(ResourceServerSecurityConfigurer resources)throws Exception;

    void HttpSecurityConfiger(HttpSecurity http)throws Exception;
}
