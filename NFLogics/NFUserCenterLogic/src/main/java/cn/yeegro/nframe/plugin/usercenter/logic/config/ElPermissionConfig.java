/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package cn.yeegro.nframe.plugin.usercenter.logic.config;

import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.comm.loader.NFPluginManager;
import cn.yeegro.nframe.plugin.usercenter.logic.module.NFIUserModule;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Zheng Jie
 */
@Service(value = "el")
public class ElPermissionConfig {

    private NFIPluginManager pPluginManager;
    private NFIUserModule m_pSysUserModule;

    public Boolean check(String ...permissions){

        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pSysUserModule = pPluginManager.FindModule(NFIUserModule.class);

        // 获取当前用户的所有权限
        List<String> elPermissions = m_pSysUserModule.getLoginAppUser().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        // 判断当前用户的所有权限是否包含接口上定义的权限
        Boolean IsHas=elPermissions.contains("ROLE_admin") || Arrays.stream(permissions).anyMatch(elPermissions::contains);
        return IsHas;
    }
}
