/*
 * Copyright 2017-2018 the original author(https://github.com/wj596)
 * 
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */
package org.jsets.shiro.filter.stateless;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.jsets.shiro.config.MessageConfig;
import org.jsets.shiro.util.Commons;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 基于JWT(JSON WEB TOKEN)的无状态过滤器--认证
 * 
 * @author wangjie (https://github.com/wj596)
 * @date 2016年6月31日
 */
public class JwtAuthcFilter extends StatelessFilter{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AccessControlFilter.class);

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		if (null != getSubject(request, response) 
				&& getSubject(request, response).isAuthenticated()) {
			return true;
		}
		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		if(isJwtSubmission(request)){
			AuthenticationToken token = createJwtToken(request, response);
			try {
				Subject subject = getSubject(request, response);
				subject.login(token);
				return true;
			} catch (AuthenticationException e) {
				LOGGER.error(request.getRemoteHost()+" JWT认证  "+e.getMessage());
				Commons.restFailed(WebUtils.toHttp(response)
						,MessageConfig.REST_CODE_AUTH_UNAUTHORIZED,e.getMessage());
			} 
		}
		Commons.restFailed(WebUtils.toHttp(response)
										,MessageConfig.REST_CODE_AUTH_UNAUTHORIZED
										,MessageConfig.REST_MESSAGE_AUTH_UNAUTHORIZED);
		return false;
	}
}
