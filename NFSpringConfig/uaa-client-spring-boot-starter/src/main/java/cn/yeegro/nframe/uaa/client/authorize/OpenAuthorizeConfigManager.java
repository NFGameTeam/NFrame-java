package cn.yeegro.nframe.uaa.client.authorize;

import java.util.List;

import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.comm.loader.NFPluginManager;
import cn.yeegro.nframe.plugin.oauthcenter.logic.authorize.NFIAuthorizeConfigManager;
import cn.yeegro.nframe.plugin.oauthcenter.logic.authorize.NFIAuthorizeConfigProvider;
import cn.yeegro.nframe.plugin.oauthcenter.logic.module.NFIAutoConfigerModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;


/** 
* @author 作者 zoocee(改)
* @version 创建时间：2018年2月1日 下午9:50:27 

*
* RbacService适用于zuul网关应用API分配，需要在api-gateway中实现接口
*/
@Component
@SuppressWarnings("all")
public class OpenAuthorizeConfigManager implements NFIAuthorizeConfigManager {

	@Autowired
	private List<NFIAuthorizeConfigProvider> authorizeConfigProviders;

	 
    @Value("${spring.application.name:}")
	private String applicationName;

	private NFIPluginManager pPluginManager;
	private NFIAutoConfigerModule m_pAutoConfigerModule;

	OpenAuthorizeConfigManager() {
		pPluginManager= NFPluginManager.GetSingletonPtr();
		m_pAutoConfigerModule=pPluginManager.FindModule(NFIAutoConfigerModule.class);
	}

	 
	@Override
	public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {

		m_pAutoConfigerModule.OpenAuthorizeConfigManager(config,authorizeConfigProviders,applicationName);
		 
		
		 
		
	}

}
