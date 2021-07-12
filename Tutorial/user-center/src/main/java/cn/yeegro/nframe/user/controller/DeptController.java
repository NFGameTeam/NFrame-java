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
import cn.yeegro.nframe.common.exception.BadRequestException;
import cn.yeegro.nframe.common.utils.PageUtil;
import cn.yeegro.nframe.plugin.usercenter.logic.criteria.DeptQueryCriteria;
import cn.yeegro.nframe.plugin.usercenter.logic.dto.DeptDto;
import cn.yeegro.nframe.plugin.usercenter.logic.model.Dept;
import cn.yeegro.nframe.plugin.usercenter.logic.module.NFIDeptModule;
import cn.yeegro.nframe.plugin.usercenter.logic.module.NFIRBACModule;
import cn.yeegro.nframe.plugin.usercenter.logic.module.NFIUserModule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
* @author Zheng Jie
* @date 2019-03-25
*/
@RestController
@RequiredArgsConstructor
@Api(tags = "系统：部门管理")
@RequestMapping("/dept")
public class DeptController {

    private NFIPluginManager pPluginManager;
    private NFIDeptModule m_pDeptModule;
    private NFIRBACModule m_pRBACModule;
    private static final String ENTITY_NAME = "dept";

    @ApiOperation("导出部门数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('dept:list')")
    public void download(HttpServletResponse response, DeptQueryCriteria criteria) throws Exception {
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pDeptModule = pPluginManager.FindModule(NFIDeptModule.class);
        m_pDeptModule.download(m_pDeptModule.queryAll(criteria, false), response);
    }

    @ApiOperation("查询部门")
    @GetMapping
    @PreAuthorize("@el.check('user:list','dept:list')")
    public ResponseEntity<Object> query(DeptQueryCriteria criteria) throws Exception {
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pDeptModule = pPluginManager.FindModule(NFIDeptModule.class);
        List<DeptDto> deptDtos = m_pDeptModule.queryAll(criteria, true);
        return new ResponseEntity<>(PageUtil.toPage(deptDtos, deptDtos.size()), HttpStatus.OK);
    }

    @ApiOperation("查询部门:根据ID获取同级与上级数据")
    @PostMapping("/superior")
    @PreAuthorize("@el.check('user:list','dept:list')")
    public ResponseEntity<Object> getSuperior(@RequestBody List<Long> ids) {
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pDeptModule = pPluginManager.FindModule(NFIDeptModule.class);
        Set<DeptDto> deptDtos  = new LinkedHashSet<>();
        for (Long id : ids) {
            DeptDto deptDto = m_pDeptModule.findById(id);
            List<DeptDto> depts = m_pDeptModule.getSuperior(deptDto, new ArrayList<>());
            deptDtos.addAll(depts);
        }
        return new ResponseEntity<>(m_pDeptModule.buildTree(new ArrayList<>(deptDtos)), HttpStatus.OK);
    }

    @ApiOperation("新增部门")
    @PostMapping
    @PreAuthorize("@el.check('dept:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Dept resources){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pDeptModule = pPluginManager.FindModule(NFIDeptModule.class);
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        m_pDeptModule.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("修改部门")
    @PutMapping
    @PreAuthorize("@el.check('dept:edit')")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Object> update(@Validated(Dept.Update.class) @RequestBody Dept resources){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pDeptModule = pPluginManager.FindModule(NFIDeptModule.class);
        m_pDeptModule.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除部门")
    @DeleteMapping
    @PreAuthorize("@el.check('dept:del')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pDeptModule = pPluginManager.FindModule(NFIDeptModule.class);
        Set<DeptDto> deptDtos = new HashSet<>();
        for (Long id : ids) {
            List<Dept> deptList = m_pDeptModule.findByPid(id);
            deptDtos.add(m_pDeptModule.findById(id));
            if(CollectionUtil.isNotEmpty(deptList)){
                deptDtos = m_pDeptModule.getDeleteDepts(deptList, deptDtos);
            }
        }
        // 验证是否被角色或用户关联
        m_pDeptModule.verification(deptDtos);
        m_pDeptModule.delete(deptDtos);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}