package cn.yeegro.nframe.uaa.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * @author 作者 zoocee(改)
 * @version 创建时间：2018年7月18日 上午22:57:51 开启session共享 blog:
 *          https://blog.51cto.com/13005375 code:
 *          https://gitee.com/owenwangwen/open-capacity-platform
 */
@EnableRedisHttpSession
public class SessionConfig {
	@Bean
	public CookieSerializer httpSessionIdResolver() {
		DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
		// 取消仅限同一站点设置
		cookieSerializer.setSameSite(null);
		return cookieSerializer;
	}
}
