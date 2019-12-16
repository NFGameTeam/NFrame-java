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
package org.jsets.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.jsets.shiro.cache.CacheDelegator;
import org.jsets.shiro.config.ShiroProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.base.Strings;

/**
 * 保持账号唯一用户登陆
 * 
 * author wangjie (https://github.com/wj596)
 * @date 2016年6月31日
 * 
 */
public class KeepOneUserFilter extends JsetsAccessControlFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(KeepOneUserFilter.class);
	
	private  ShiroProperties properties;
	private  SessionManager sessionManager;
	private  CacheDelegator cacheDelegate;

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		if(!this.properties.isKeepOneEnabled()) return true;
		return false;
	}

	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		Subject subject = getSubject(request, response);
		if (!subject.isAuthenticated() && !subject.isRemembered()) {
			return this.respondLogin(request, response);
		}
		String account = (String) subject.getPrincipal();
		String loginedSessionId = this.cacheDelegate.getKeepUser(account);
		Session currentSession = subject.getSession();
		String currentSessionId = (String) currentSession.getId();
		
		if(currentSessionId.equals(loginedSessionId)) {
			return true;
		} else if (Strings.isNullOrEmpty(loginedSessionId)){
			this.cacheDelegate.putKeepUser(account, currentSessionId);
        	return true;
		} else if (null==currentSession.getAttribute(ShiroProperties.ATTRIBUTE_SESSION_KICKOUT)) {
			this.cacheDelegate.putKeepUser(account, currentSessionId);
			try{
				Session loginedSession = this.sessionManager.getSession(new DefaultSessionKey(loginedSessionId));
				if(null != loginedSession){
					loginedSession.setAttribute(ShiroProperties.ATTRIBUTE_SESSION_KICKOUT,Boolean.TRUE);
				}
			} catch(SessionException e){
				LOGGER.warn(e.getMessage());
			}
		}
        if (null!=currentSession.getAttribute(ShiroProperties.ATTRIBUTE_SESSION_KICKOUT)) {
        	subject.logout();
			return this.respondRedirect(request, response,this.properties.getKickoutUrl());
        }

		return true;
	}

	public void setProperties(ShiroProperties properties) {
		this.properties = properties;
	}
	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}
	public void setCacheDelegate(CacheDelegator cacheDelegate) {
		this.cacheDelegate = cacheDelegate;
	}
}