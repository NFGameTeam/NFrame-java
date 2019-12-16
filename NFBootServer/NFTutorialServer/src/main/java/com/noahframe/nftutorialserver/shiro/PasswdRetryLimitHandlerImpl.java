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

import com.noahframe.loader.NFPluginManager;
import com.noahframe.nfcore.iface.module.NFIDubboModule;
import com.noahframe.plugins.logic.mall.database.iface.UserService;
import com.noahframe.plugins.logic.mall.database.model.TbUser;
import org.jsets.shiro.handler.PasswdRetryLimitHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 密码错误次数超限处理器实现集成自PasswdRetryLimitHandler
 * 此处演示锁定用户
 * 
 * @author wangjie (https://github.com/wj596)
 * @date 2016年9月15日
 */ 
@Service
public class PasswdRetryLimitHandlerImpl implements PasswdRetryLimitHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PasswdRetryLimitHandlerImpl.class);

    protected NFPluginManager pPluginManager = null;
    protected NFIDubboModule pDubboModule=null;

    private UserService userService;
    
    @Override
    public void handle(String account) {
        //数据库动态权限
        pPluginManager = NFPluginManager.GetSingletonPtr();
        if (pPluginManager==null)
            return ;
        pDubboModule=pPluginManager.FindModule(NFIDubboModule.class);
        if (pDubboModule==null)
            return ;
        userService=pDubboModule.GetErferenceService("userService");
        TbUser user= userService.getUserByUsername(account);
        user.setState(99);
        userService.updateUser(user);
        LOGGER.warn("账号："+account+"密码错误超过5次，已锁定");
    }
}