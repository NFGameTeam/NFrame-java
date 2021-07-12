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

import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.comm.loader.NFPluginManager;
import cn.yeegro.nframe.common.exception.BadRequestException;
import cn.yeegro.nframe.plugin.usercenter.logic.criteria.JobQueryCriteria;
import cn.yeegro.nframe.plugin.usercenter.logic.model.Job;
import cn.yeegro.nframe.plugin.usercenter.logic.module.NFIDictModule;
import cn.yeegro.nframe.plugin.usercenter.logic.module.NFIJobModule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
* @author Zheng Jie
* @date 2019-03-29
*/
@RestController
@RequiredArgsConstructor
@Api(tags = "系统：岗位管理")
@RequestMapping("/job")
public class JobController {

    private NFIPluginManager pPluginManager;
    private NFIJobModule m_pJobModule;
    private static final String ENTITY_NAME = "job";

    @ApiOperation("导出岗位数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('job:list')")
    public void download(HttpServletResponse response, JobQueryCriteria criteria) throws IOException {
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pJobModule = pPluginManager.FindModule(NFIJobModule.class);
        m_pJobModule.download(m_pJobModule.queryAll(criteria), response);
    }

    @ApiOperation("查询岗位")
    @GetMapping
    @PreAuthorize("@el.check('job:list','user:list')")
    public ResponseEntity<Object> query(JobQueryCriteria criteria, Pageable pageable){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pJobModule = pPluginManager.FindModule(NFIJobModule.class);
        return new ResponseEntity<>(m_pJobModule.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @ApiOperation("新增岗位")
    @PostMapping
    @PreAuthorize("@el.check('job:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Job resources){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pJobModule = pPluginManager.FindModule(NFIJobModule.class);
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        m_pJobModule.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("修改岗位")
    @PutMapping
    @PreAuthorize("@el.check('job:edit')")
    public ResponseEntity<Object> update(@Validated(Job.Update.class) @RequestBody Job resources){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pJobModule = pPluginManager.FindModule(NFIJobModule.class);
        m_pJobModule.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除岗位")
    @DeleteMapping
    @PreAuthorize("@el.check('job:del')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pJobModule = pPluginManager.FindModule(NFIJobModule.class);
        // 验证是否被用户关联
        m_pJobModule.verification(ids);
        m_pJobModule.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}