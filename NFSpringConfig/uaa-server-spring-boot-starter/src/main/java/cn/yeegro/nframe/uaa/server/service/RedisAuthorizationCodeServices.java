package cn.yeegro.nframe.uaa.server.service;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;

/**
 * @author 刘斌（改）
 * @version 创建时间：2017年11月12日 上午22:57:51
 * JdbcAuthorizationCodeServices替换
 */
public class RedisAuthorizationCodeServices extends RandomValueAuthorizationCodeServices {

	private RedisTemplate<String,Object> redisTemplate ;

	
	public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	/**
	 * 替换JdbcAuthorizationCodeServices的存储策略
	 * 将存储code到redis，并设置过期时间，10分钟<br>
	 */
	@Override
	protected void store(String code, OAuth2Authentication authentication) {
		
		redisTemplate.opsForValue().set(redisKey(code), authentication, 10, TimeUnit.MINUTES);
		
		 
	}

	@Override
	protected OAuth2Authentication remove(final String code) {
		 
		String codeKey =redisKey(code) ;
			
		OAuth2Authentication token = (OAuth2Authentication) redisTemplate.opsForValue().get(codeKey) ;
			
		this.redisTemplate.delete(codeKey); 

		return token;
	}

	/**
	 * redis中 code key的前缀
	 * 
	 * @param code
	 * @return
	 */
	private String redisKey(String code) {
		return "oauth:code:" + code;
	}
}
