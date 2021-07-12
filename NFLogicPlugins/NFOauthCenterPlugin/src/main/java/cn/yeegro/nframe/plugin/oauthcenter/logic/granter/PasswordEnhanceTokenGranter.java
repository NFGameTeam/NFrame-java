package cn.yeegro.nframe.plugin.oauthcenter.logic.granter;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.yeegro.nframe.common.utils.SpringUtils;
import cn.yeegro.nframe.plugin.oauthcenter.logic.service.ValidateCodeService;
import org.apache.commons.collections.MapUtils;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import cn.yeegro.nframe.common.utils.StringUtil;

/**
 * 程序名 : PasswordEnhanceTokenGranter
 * 建立日期: 2018-09-09
 * 作者 : someday
 * 描述 : 密码增强模式
 * 备注 : version20180909001
 * 序号 	       日期 		        修改人 		         修改原因
 */
public class PasswordEnhanceTokenGranter extends AbstractTokenGranter {

	private static final String GRANT_TYPE = "password";

	private AuthenticationManager authenticationManager;

	private ValidateCodeService validateCodeService ;

	public PasswordEnhanceTokenGranter(
			AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
		this(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
	}

	protected PasswordEnhanceTokenGranter( AuthorizationServerTokenServices tokenServices,
			ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, String grantType) {
		super(tokenServices, clientDetailsService, requestFactory, grantType);
		this.validateCodeService=new ValidateCodeService();
		this.authenticationManager= SpringUtils.getBean(AuthenticationManager.class);
	}

	@Override
	protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {

		Map<String, String> parameters = new LinkedHashMap<String, String>(tokenRequest.getRequestParameters());
		String username = parameters.get("username");
		String password = parameters.get("password");
		
		//终端
		String deviceId  = MapUtils.getString(parameters, "deviceId") ;
		//验证码
		String validCode =  MapUtils.getString(parameters, "validCode") ;
		
		
		//校验图形验证码
		if(StringUtil.isNotBlank(deviceId) || StringUtil.isNotEmpty(validCode)){
			try {
				validateCodeService.validate(deviceId, validCode);
			} catch (Exception e) {
				throw new InvalidGrantException(e.getMessage());
			}
		}
		
		
		// Protect from downstream leaks of password
		parameters.remove("password");
		parameters.remove("deviceId");
		parameters.remove("validCode");
		

		Authentication userAuth = new UsernamePasswordAuthenticationToken(username, password);
		((AbstractAuthenticationToken) userAuth).setDetails(parameters);
		try {
			userAuth = authenticationManager.authenticate(userAuth);
		}catch (AccountStatusException ase) {
			throw new InvalidGrantException(ase.getMessage());
		}catch (BadCredentialsException e) {
			throw new InvalidGrantException(e.getMessage());
		}
		if (userAuth == null || !userAuth.isAuthenticated()) {
			throw new InvalidGrantException("Could not authenticate user: " + username);
		}
		
		OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);		
		return new OAuth2Authentication(storedOAuth2Request, userAuth);
	}

}