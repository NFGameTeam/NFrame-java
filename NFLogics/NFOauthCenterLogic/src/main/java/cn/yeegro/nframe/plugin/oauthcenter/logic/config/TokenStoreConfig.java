package cn.yeegro.nframe.plugin.oauthcenter.logic.config;

import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.comm.loader.NFPluginManager;
import cn.yeegro.nframe.common.utils.SpringUtils;
import cn.yeegro.nframe.plugin.oauthcenter.logic.module.NFITokenStoreConfigerModule;
import cn.yeegro.nframe.plugin.oauthcenter.logic.token.NFIRedisTemplateTokenStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;


/** 
* @author 作者 zoocee(改)
* @version 创建时间：2018年4月5日 下午19:52:21
* 类说明 
* redis存储token


*/
@Configuration
public class TokenStoreConfig {

	private NFIPluginManager pPluginManager;
	private NFITokenStoreConfigerModule m_pTokenStoreConfigerModule;

	TokenStoreConfig(ApplicationContext context) {
		// 在初始化AutoConfiguration时会自动传入ApplicationContext
		SpringUtils.setAppContext(context);
		pPluginManager = NFPluginManager.GetSingletonPtr();
		m_pTokenStoreConfigerModule = pPluginManager.FindModule(NFITokenStoreConfigerModule.class);
	}
	
	
	@Bean
	@ConditionalOnProperty(prefix="security.oauth2.token.store",name="type" ,havingValue="jdbc" ,matchIfMissing=false)
	public JdbcTokenStore jdbcTokenStore(){
 
return m_pTokenStoreConfigerModule.jdbcTokenStore();
	}
	
	@Bean
	@ConditionalOnProperty(prefix="security.oauth2.token.store",name="type" ,havingValue="redis" ,matchIfMissing=true)
	public NFIRedisTemplateTokenStore redisTokenStore(RedisConnectionFactory connectionFactory){
		return m_pTokenStoreConfigerModule.redisTokenStore(connectionFactory);
	}
	
	//使用jwt替换原有的uuid生成token方式
	@Configuration
	@ConditionalOnProperty(prefix="security.oauth2.token.store",name="type" ,havingValue="jwt" ,matchIfMissing=false)
	public static class JWTTokenConfig {

		private NFIPluginManager pPluginManager;
		private NFITokenStoreConfigerModule m_pTokenStoreConfigerModule;

		JWTTokenConfig(ApplicationContext context) {
			// 在初始化AutoConfiguration时会自动传入ApplicationContext
			SpringUtils.setAppContext(context);
			pPluginManager = NFPluginManager.GetSingletonPtr();
			m_pTokenStoreConfigerModule = pPluginManager.FindModule(NFITokenStoreConfigerModule.class);
		}

		@Bean
		public JwtTokenStore jwtTokenStore(){
			return m_pTokenStoreConfigerModule.jwtTokenStore();
		}
		
		@Bean
		public JwtAccessTokenConverter jwtAccessTokenConverter(){
			return m_pTokenStoreConfigerModule.jwtAccessTokenConverter() ;
		}
	}
	
}
