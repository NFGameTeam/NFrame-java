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
package com.noahframe.nftutorialserver.shiro;


import com.google.common.collect.Sets;
import com.noahframe.loader.NFPluginManager;
import com.noahframe.nfcore.iface.module.NFIDubboModule;
import com.noahframe.plugins.logic.mall.database.iface.UserService;
import com.noahframe.plugins.logic.mall.database.model.TbUser;
import org.apache.shiro.authc.AuthenticationException;
import org.jsets.shiro.model.Account;
import org.jsets.shiro.service.ShiroAccountProvider;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * 账号信息提供者实现
 * 
 * @author wangjie (https://github.com/wj596) 
 * @date 2016年6月24日 下午2:55:15
 */ 
@Service
public class AccountProviderImpl implements ShiroAccountProvider {


	protected NFPluginManager pPluginManager = null;
	protected NFIDubboModule pDubboModule=null;

	private UserService userService;
	@Override
	public Account loadAccount(String account) throws AuthenticationException {

		//数据库动态权限
		pPluginManager = NFPluginManager.GetSingletonPtr();
		if (pPluginManager==null)
			return null;
		pDubboModule=pPluginManager.FindModule(NFIDubboModule.class);
		if (pDubboModule==null)
			return null;
		userService=pDubboModule.GetErferenceService("userService");
		TbUser user = userService.getUserByUsername(account);
		// 用户不存在
		if(null == user){
			throw new AuthenticationException("账号或密码错误");
		}

		UserEntity pUser=new UserEntity();
		pUser.setId(user.getId().toString());
		pUser.setAccount(user.getUsername());
		pUser.setUserName(user.getUsername());
		pUser.setStatus(user.getState());
		pUser.setPassword(user.getPassword());
		pUser.setCreateTime(user.getCreated());
		pUser.setPhone(user.getPhone());
		pUser.setEmail(user.getEmail());
		return pUser;
	}
	
	
	/** 
	 * 加载用户持有的角色
	 */
	@Override
	public Set<String> loadRoles(String account) {
		return null;
	}
	
	
	/**
	 * 
	 * 系统采用  基于角色的权限访问控制(RBAC)策略
	 * 所谓的权限通常可以理解为用户所能操作的资源，如（user:add、user:delete）
	 * 此方法未实现
	 */ 
	@Override
	public Set<String> loadPermissions(String account) {
		return null;
	}
}