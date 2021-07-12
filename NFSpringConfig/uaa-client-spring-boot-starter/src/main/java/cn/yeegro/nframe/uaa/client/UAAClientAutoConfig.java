package cn.yeegro.nframe.uaa.client;

import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.comm.loader.NFPluginManager;
import cn.yeegro.nframe.common.utils.SpringUtils;
import cn.yeegro.nframe.plugin.oauthcenter.logic.module.NFISecurityConfigerModule;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

import cn.yeegro.nframe.common.auth.props.PermitUrlProperties;
import cn.yeegro.nframe.plugin.oauthcenter.logic.feign.FeignInterceptorConfig;
import cn.yeegro.nframe.common.feign.GlobalFeignConfig;
import cn.yeegro.nframe.common.rest.RestTemplateConfig;

/**
 * @author 作者 zoocee(改)
 * @version 创建时间：2017年11月12日 上午22:57:51


 */
@Component
@Configuration
@EnableResourceServer
@SuppressWarnings("all") 
@AutoConfigureAfter(TokenStore.class)
@EnableConfigurationProperties(PermitUrlProperties.class)
@Import({RestTemplateConfig.class,FeignInterceptorConfig.class})
@EnableFeignClients(defaultConfiguration= GlobalFeignConfig.class)
//开启spring security 注解
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class UAAClientAutoConfig extends ResourceServerConfigurerAdapter {

	private NFIPluginManager pPluginManager;
	private NFISecurityConfigerModule m_pSecurityConfigerModule;

	UAAClientAutoConfig(ApplicationContext context) {
		// 在初始化AutoConfiguration时会自动传入ApplicationContext
		SpringUtils.setAppContext(context);
		pPluginManager= NFPluginManager.GetSingletonPtr();
		m_pSecurityConfigerModule=pPluginManager.FindModule(NFISecurityConfigerModule.class);
	}

	public void configure(WebSecurity web) throws Exception {
		m_pSecurityConfigerModule.ClientWebSecurityConfiger(web);
	}
	

	
	 

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {

		m_pSecurityConfigerModule.ClientResourceServerSecurityConfigurer(resources);

	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		m_pSecurityConfigerModule.ClientHttpSecurityConfiger(http);
	}

}
