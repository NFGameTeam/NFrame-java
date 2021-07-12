package cn.yeegro.nframe.uaa.client.authorize.provider;

import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.comm.loader.NFPluginManager;
import cn.yeegro.nframe.plugin.oauthcenter.logic.module.NFIAutoConfigerModule;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

import cn.yeegro.nframe.common.auth.props.PermitUrlProperties;
import cn.yeegro.nframe.plugin.oauthcenter.logic.authorize.NFIAuthorizeConfigProvider;

/**
 * @author 作者 zoocee(改)
 * @version 创建时间：2018年1月31日 下午9:11:36 类说明 白名单


 */
@Component
@Order(Integer.MAX_VALUE - 1)
@EnableConfigurationProperties(PermitUrlProperties.class)
public class AuthAuthorizeConfigProvider implements NFIAuthorizeConfigProvider {

	private NFIPluginManager pPluginManager;
	private NFIAutoConfigerModule m_pAutoConfigerModule;

	AuthAuthorizeConfigProvider() {
		pPluginManager= NFPluginManager.GetSingletonPtr();
		m_pAutoConfigerModule=pPluginManager.FindModule(NFIAutoConfigerModule.class);
	}

	@Override
	public boolean config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
		return m_pAutoConfigerModule.AuthAuthorizeConfigProvider(config);
	}

}
