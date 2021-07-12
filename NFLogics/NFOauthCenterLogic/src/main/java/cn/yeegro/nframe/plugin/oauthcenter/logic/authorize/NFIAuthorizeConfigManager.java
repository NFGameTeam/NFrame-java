package cn.yeegro.nframe.plugin.oauthcenter.logic.authorize;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * @author 作者 zoocee(改)
 * @version 创建时间：2018年2月1日 下午9:47:00
 * 类说明


 */
public interface NFIAuthorizeConfigManager {

	/**
	 * @param config
	 */
	void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config);
}
