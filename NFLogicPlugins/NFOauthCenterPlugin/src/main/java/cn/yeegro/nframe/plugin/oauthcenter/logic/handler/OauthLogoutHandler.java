package cn.yeegro.nframe.plugin.oauthcenter.logic.handler;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.yeegro.nframe.common.utils.SpringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.util.Assert;

import cn.yeegro.nframe.plugin.oauthcenter.logic.constant.UaaConstant;
import cn.yeegro.nframe.common.utils.StringUtil;

/**
 * @author keets
 * @date 2017/10/17
 */
public class OauthLogoutHandler  implements NFIOauthLogoutHandler {

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

		TokenStore tokenStore= SpringUtils.getBean(TokenStore.class);

		Assert.notNull(tokenStore, "tokenStore must be set");
		String token = extractToken(request);
		if(StringUtil.isNotBlank(token)){
			OAuth2AccessToken existingAccessToken = tokenStore.readAccessToken(token);
			OAuth2RefreshToken refreshToken;
			if (existingAccessToken != null) {
				if (existingAccessToken.getRefreshToken() != null) {
					//log.info("remove refreshToken!", existingAccessToken.getRefreshToken());
					refreshToken = existingAccessToken.getRefreshToken();
					tokenStore.removeRefreshToken(refreshToken);
				}
				//log.info("remove existingAccessToken!", existingAccessToken);
				tokenStore.removeAccessToken(existingAccessToken);
			}
			return;
		}
	}

	protected String extractToken(HttpServletRequest request) {
		// first check the header...
		String token = extractHeaderToken(request);

		// bearer type allows a request parameter as well
		if (token == null) {
			//log.debug("Token not found in headers. Trying request parameters.");
			token = request.getParameter(OAuth2AccessToken.ACCESS_TOKEN);
			if (token == null) {
			//	log.debug("Token not found in request parameters.  Not an OAuth2 request.");
			} else {
				request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_TYPE, OAuth2AccessToken.BEARER_TYPE);
			}
		}

		return token;
	}

	protected String extractHeaderToken(HttpServletRequest request) {
		Enumeration<String> headers = request.getHeaders(UaaConstant.AUTHORIZATION);
		while (headers.hasMoreElements()) { // typically there is only one (most
											// servers enforce that)
			String value = headers.nextElement();
			if ((value.toLowerCase().startsWith(OAuth2AccessToken.BEARER_TYPE.toLowerCase()))) {
				String authHeaderValue = value.substring(OAuth2AccessToken.BEARER_TYPE.length()).trim();
				// Add this here for the auth details later. Would be better to
				// change the signature of this method.
				request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_TYPE,
						value.substring(0, OAuth2AccessToken.BEARER_TYPE.length()).trim());
				int commaIndex = authHeaderValue.indexOf(',');
				if (commaIndex > 0) {
					authHeaderValue = authHeaderValue.substring(0, commaIndex);
				}
				return authHeaderValue;
			}
		}

		return null;
	}

}
