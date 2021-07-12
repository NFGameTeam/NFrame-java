package cn.yeegro.nframe.uaa.server.config;

import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.comm.loader.NFPluginManager;
import cn.yeegro.nframe.common.utils.SpringUtils;
import cn.yeegro.nframe.plugin.oauthcenter.logic.module.NFISecurityConfigerModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import cn.yeegro.nframe.common.auth.props.PermitUrlProperties;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * spring security配置
 * 
 * @author 刘斌（改）
 * @version 创建时间：2017年11月12日 上午22:57:51 2017年10月16日
 * 在WebSecurityConfigurerAdapter不拦截oauth要开放的资源


 */
@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(PermitUrlProperties.class)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private NFIPluginManager pPluginManager;
	private NFISecurityConfigerModule m_pSecurityConfigerModule;


	SecurityConfig(ApplicationContext context) {
		// 在初始化AutoConfiguration时会自动传入ApplicationContext
		SpringUtils.setAppContext(context);
		pPluginManager= NFPluginManager.GetSingletonPtr();
		m_pSecurityConfigerModule=pPluginManager.FindModule(NFISecurityConfigerModule.class);
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		m_pSecurityConfigerModule.WebSecurityConfiger(web);
	}
	/**
	 * 认证管理
	 * 
	 * @return 认证管理对象
	 * @throws Exception
	 *             认证异常信息
	 */
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		m_pSecurityConfigerModule.HttpSecurityConfiger(http);

	}

	/**
	 * 全局用户信息
	 * 
	 * @param auth
	 *            认证管理
	 * @throws Exception
	 *             用户认证异常信息
	 */
	@Autowired
	public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
		m_pSecurityConfigerModule.globalUserDetails(auth);
	}


}
