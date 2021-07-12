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

import cn.hutool.core.collection.CollectionUtil;
import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.comm.loader.NFPluginManager;
import cn.yeegro.nframe.plugin.usercenter.logic.criteria.SysPermissionQueryCriteria;
import cn.yeegro.nframe.plugin.usercenter.logic.dto.SysPermissionDto;
import cn.yeegro.nframe.plugin.usercenter.logic.dto.SysRoleDto;
import cn.yeegro.nframe.plugin.usercenter.logic.model.SysPermission;
import cn.yeegro.nframe.plugin.usercenter.logic.module.NFIRBACModule;
import cn.yeegro.nframe.plugin.usercenter.logic.module.NFIUserModule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
* @website https://docs.auauz.net
* @author 刘斌
* @date 2020-07-27
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "权限表管理")
@RequestMapping("/permissions")
public class SysPermissionController {

    private NFIPluginManager pPluginManager;
    private NFIUserModule m_pSysUserModule;
    private NFIRBACModule m_pRBACModule;


    @ApiOperation("返回全部的菜单")
    @GetMapping(value = "/lazy")
    @PreAuthorize("@el.check()")
    public ResponseEntity<Object> lazy(@RequestParam Long pid){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pRBACModule= pPluginManager.FindModule(NFIRBACModule.class);
        SysPermissionQueryCriteria permissionQueryCriteria=new SysPermissionQueryCriteria();

        return new ResponseEntity<>(m_pRBACModule.queryAll(permissionQueryCriteria), HttpStatus.OK);
    }

    @ApiOperation("查询权限:根据ID获取同级与上级数据")
    @PostMapping("/superior")
    @PreAuthorize("@el.check()")
    public ResponseEntity<Object> getSuperior(@RequestBody List<Long> ids) {
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pRBACModule= pPluginManager.FindModule(NFIRBACModule.class);
        List<SysPermissionDto> permissionDtos = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(ids)){
            for (Long id : ids) {
                SysRoleDto sysRoleDto = m_pRBACModule.RoleFindById(id);

                if (sysRoleDto.getId()==1L)
                {
                    permissionDtos=m_pRBACModule.queryAll(new SysPermissionQueryCriteria());
                    return new ResponseEntity<>(permissionDtos, HttpStatus.OK);
                }
                else{
                    permissionDtos.addAll(m_pRBACModule.queryByRoleId(id));
                }
            }
        }
        return new ResponseEntity<>(permissionDtos, HttpStatus.OK);
    }

//    @PostMapping(value = "/auth")
//    @Log("修改权限表")
//    @ApiOperation("修改权限表")
//    @PreAuthorize("@el.check('permission:edit')")
//    public ResponseEntity<Object> auth(@Validated @RequestBody SysAuths resources){
//        pPluginManager = NFPluginManager.GetSingletonPtr();
//        m_pRBACModule= pPluginManager.FindModule(NFIRBACModule.class);
//        authsService.create(resources);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }


    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('sysPermission:list')")
    public void download(HttpServletResponse response, SysPermissionQueryCriteria criteria) throws IOException {
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pRBACModule= pPluginManager.FindModule(NFIRBACModule.class);
//        m_pRBACModule.download(m_pRBACModule.queryAll(criteria), response);
    }

    @GetMapping
    @ApiOperation("查询权限表")
    @PreAuthorize("@el.check('sysPermission:list')")
    public ResponseEntity<Object> query(SysPermissionQueryCriteria criteria, Pageable pageable){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pRBACModule= pPluginManager.FindModule(NFIRBACModule.class);
        return new ResponseEntity<>(m_pRBACModule.queryAll(criteria,pageable), HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation("新增权限表")
    @PreAuthorize("@el.check('sysPermission:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody SysPermission resources){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pRBACModule= pPluginManager.FindModule(NFIRBACModule.class);
        return new ResponseEntity<>(m_pRBACModule.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @ApiOperation("修改权限表")
    @PreAuthorize("@el.check('sysPermission:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody SysPermission resources){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pRBACModule= pPluginManager.FindModule(NFIRBACModule.class);
        m_pRBACModule.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除权限表")
    @PreAuthorize("@el.check('sysPermission:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pRBACModule= pPluginManager.FindModule(NFIRBACModule.class);
        m_pRBACModule.deleteAllPermission(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}