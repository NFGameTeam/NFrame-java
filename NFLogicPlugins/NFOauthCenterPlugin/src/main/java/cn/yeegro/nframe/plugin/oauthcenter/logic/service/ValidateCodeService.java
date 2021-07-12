package cn.yeegro.nframe.plugin.oauthcenter.logic.service;

import javax.servlet.http.HttpServletRequest;

import cn.yeegro.nframe.common.utils.SpringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;


/**
 * @author zlt
 * @date 2018/12/10
 */
@SuppressWarnings("all")
public class ValidateCodeService implements NFIValidateCodeService {

	/**
	 * 保存用户验证码，和randomStr绑定
	 * @param deviceId
	 *            客户端生成
	 * @param imageCode
	 *            验证码信息
	 */
	public void saveImageCode(String deviceId, String imageCode) {

		RedisTemplate<String, Object> redisTemplate= SpringUtils.getBean(RedisTemplate.class);

		String text = imageCode.toLowerCase().toString();

		redisTemplate.execute(new RedisCallback<String>() {

			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {

				// redis info
				connection.set(buildKey(deviceId).getBytes(), imageCode.getBytes());
				connection.expire(buildKey(deviceId).getBytes(), 60L*5L);
				connection.close();

				return "";
			}
		});

	}

	/**
	 * 获取验证码
	 * @param deviceId
	 *            前端唯一标识/手机号
	 */
	public String getCode(String deviceId)  {
		RedisTemplate<String, Object> redisTemplate= SpringUtils.getBean(RedisTemplate.class);
		String code = "" ;
		try {
			code = redisTemplate.execute(new RedisCallback<String>() {

				@Override
				public String doInRedis(RedisConnection connection) throws DataAccessException {

					// redis info
					byte[] temp = "".getBytes();
					temp = connection.get(buildKey(deviceId).getBytes()) ;
					connection.close();

					return new String(temp);
				}
			});
		} catch (Exception e) {
			throw new AuthenticationException("验证码不存在"){};
		}
		
		return code ;

	}

	/**
	 * 删除验证码
	 * @param deviceId
	 *            前端唯一标识/手机号
	 */
	public void remove(String deviceId) {
		RedisTemplate<String, Object> redisTemplate= SpringUtils.getBean(RedisTemplate.class);
		redisTemplate.execute(new RedisCallback<String>() {

			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {

				// redis info
				connection.del(buildKey(deviceId).getBytes());
				connection.close();

				return "";
			}
		});
	}

	/**
	 * 验证验证码
	 */
	public void validate(HttpServletRequest request) {
		String deviceId = request.getParameter("deviceId");
		if (StringUtils.isBlank(deviceId)) {
			throw new AuthenticationException("请在请求参数中携带deviceId参数"){};
		}
		String code = this.getCode(deviceId);
		String codeInRequest;
		try {
			codeInRequest = ServletRequestUtils.getStringParameter(request, "validCode");
		} catch (ServletRequestBindingException e) {
			throw new AuthenticationException ("获取验证码的值失败"){};
		}
		if (StringUtils.isBlank(codeInRequest)) {
			throw new AuthenticationException ("请填写验证码"){};
		}

		if (code == null) {
			throw new AuthenticationException ("验证码不存在或已过期"){};
		}

		if (!StringUtils.equalsIgnoreCase(code, codeInRequest)) {
			throw new AuthenticationException ("验证码不正确"){};
		}

		this.remove(deviceId);
	}

	private String buildKey(String deviceId) {
		return "DEFAULT_CODE_KEY:" + deviceId;
	}

	public void validate(String deviceId, String validCode) {
		if (StringUtils.isBlank(deviceId)) {
			throw new AuthenticationException("请在请求参数中携带deviceId参数"){};
		}
		String code = this.getCode(deviceId);
		 
		if (StringUtils.isBlank(validCode)) {
			throw new AuthenticationException ("请填写验证码"){};
		}

		if (code == null) {
			throw new AuthenticationException ("验证码不存在或已过期"){};
		}

		if (!StringUtils.equalsIgnoreCase(code, validCode)) {
			throw new AuthenticationException ("验证码不正确"){};
		}

		this.remove(deviceId);
	}
}
