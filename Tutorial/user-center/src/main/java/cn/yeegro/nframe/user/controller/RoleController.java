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
package cn.yeegro.nframe.user.controller;

import cn.hutool.core.lang.Dict;
import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.comm.loader.NFPluginManager;
import cn.yeegro.nframe.common.exception.BadRequestException;
import cn.yeegro.nframe.plugin.usercenter.logic.criteria.SysRoleQueryCriteria;
import cn.yeegro.nframe.plugin.usercenter.logic.dto.RoleSmallDto;
import cn.yeegro.nframe.plugin.usercenter.logic.dto.SysRoleDto;
import cn.yeegro.nframe.plugin.usercenter.logic.model.SysRole;
import cn.yeegro.nframe.plugin.usercenter.logic.module.NFIRBACModule;
import cn.yeegro.nframe.plugin.usercenter.logic.module.NFIUserModule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Zheng Jie
 * @date 2018-12-03
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "系统：角色管理")
@RequestMapping("/roles")
public class RoleController {

    private NFIPluginManager pPluginManager;
    private NFIUserModule m_pSysUserModule;
    private NFIRBACModule m_pRBACModule;

    private static final String ENTITY_NAME = "role";

    @ApiOperation("获取单个role")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('roles:list')")
    public ResponseEntity<Object> query(@PathVariable Long id){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pRBACModule= pPluginManager.FindModule(NFIRBACModule.class);
        return new ResponseEntity<>(m_pRBACModule.RoleFindById(id), HttpStatus.OK);
    }

    @ApiOperation("导出角色数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('role:list')")
    public void download(HttpServletResponse response, SysRoleQueryCriteria criteria) throws IOException {
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pRBACModule= pPluginManager.FindModule(NFIRBACModule.class);
//        m_pRBACModule.download(m_pRBACModule.queryAll(criteria), response);
    }

    @ApiOperation("返回全部的角色")
    @GetMapping(value = "/all")
    @PreAuthorize("@el.check('roles:list','user:add','user:edit')")
    public ResponseEntity<Object> query(){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pRBACModule= pPluginManager.FindModule(NFIRBACModule.class);
        return new ResponseEntity<>(m_pRBACModule.queryAll(), HttpStatus.OK);
    }

    @ApiOperation("查询角色")
    @GetMapping
    @PreAuthorize("@el.check('roles:list')")
    public ResponseEntity<Object> query(SysRoleQueryCriteria criteria, Pageable pageable){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pRBACModule= pPluginManager.FindModule(NFIRBACModule.class);
        return new ResponseEntity<>(m_pRBACModule.queryAll(criteria,pageable), HttpStatus.OK);
    }

    @ApiOperation("获取用户级别")
    @GetMapping(value = "/level")
    public ResponseEntity<Object> getLevel(){
        return new ResponseEntity<>(Dict.create().set("level", getLevels(null)), HttpStatus.OK);
    }

    @ApiOperation("新增角色")
    @PostMapping
    @PreAuthorize("@el.check('roles:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody SysRole resources){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pRBACModule= pPluginManager.FindModule(NFIRBACModule.class);
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        getLevels(resources.getLevel());
        m_pRBACModule.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("修改角色")
    @PutMapping
    @PreAuthorize("@el.check('roles:edit')")
    public ResponseEntity<Object> update(@Validated(SysRole.Update.class) @RequestBody SysRole resources){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pRBACModule= pPluginManager.FindModule(NFIRBACModule.class);
        getLevels(resources.getLevel());
        m_pRBACModule.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("修改角色菜单")
    @PutMapping(value = "/menu")
    @PreAuthorize("@el.check('roles:edit')")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Object> updateMenu(@RequestBody SysRole resources){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pRBACModule= pPluginManager.FindModule(NFIRBACModule.class);
        SysRoleDto role = m_pRBACModule.RoleFindById(resources.getId());
        getLevels(role.getLevel());
        m_pRBACModule.updateMenu(resources,role);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("修改角色权限")
    @PutMapping(value = "/permission")
    @PreAuthorize("@el.check('roles:edit')")
    public ResponseEntity<Object> updatePermission(@RequestBody SysRole resources){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pRBACModule= pPluginManager.FindModule(NFIRBACModule.class);
        SysRoleDto role = m_pRBACModule.RoleFindById(resources.getId());
        getLevels(role.getLevel());
        m_pRBACModule.updatePermission(resources,role);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除角色")
    @DeleteMapping
    @PreAuthorize("@el.check('roles:del')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pRBACModule= pPluginManager.FindModule(NFIRBACModule.class);
        for (Long id : ids) {
            SysRoleDto role = m_pRBACModule.RoleFindById(id);
            getLevels(role.getLevel());
        }
        // 验证是否被用户关联
        m_pRBACModule.verification(ids);
        m_pRBACModule.deleteRoles(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 获取用户的角色级别
     * @return /
     */
    private int getLevels(Integer level){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pRBACModule= pPluginManager.FindModule(NFIRBACModule.class);
        m_pSysUserModule = pPluginManager.FindModule(NFIUserModule.class);
        List<Integer> levels = m_pRBACModule.findByUsersId(m_pSysUserModule.getLoginAppUser().getId()).stream().map(RoleSmallDto::getLevel).collect(Collectors.toList());
        int min = Collections.min(levels);
        if(level != null){
            if(level < min){
                throw new BadRequestException("权限不足，你的角色级别：" + min + "，低于操作的角色级别：" + level);
            }
        }
        return min;
    }
}
