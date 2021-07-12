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
package cn.yeegro.nframe.plugin.usercenter.logic.repository;

import cn.yeegro.nframe.plugin.usercenter.logic.model.SysPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
* @website https://docs.auauz.net
* @author 刘斌
* @date 2020-07-27
**/
public interface SysPermissionRepository extends JpaRepository<SysPermission, Long>, JpaSpecificationExecutor<SysPermission> {


    @Query(value = "SELECT p.* FROM sys_roles_perms r,sys_permission p WHERE r.role_id=?1 AND p.perm_id=r.perm_id ",nativeQuery = true)
    List<SysPermission> queryByRoleId(Long id);

    @Query(value = "SELECT p.* FROM sys_auths a,sys_permission p WHERE a.user_id=?1 AND p.perm_id=a.perm_id and a.expiration_time>now()",nativeQuery = true)
    List<SysPermission> queryByUserId(long userid);
}