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
import cn.yeegro.nframe.plugin.usercenter.logic.criteria.DictQueryCriteria;
import cn.yeegro.nframe.plugin.usercenter.logic.model.Dict;
import cn.yeegro.nframe.plugin.usercenter.logic.module.NFIDeptModule;
import cn.yeegro.nframe.plugin.usercenter.logic.module.NFIDictModule;
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
* @date 2019-04-10
*/
@RestController
@RequiredArgsConstructor
@Api(tags = "系统：字典管理")
@RequestMapping("/dict")
public class DictController {

    private NFIPluginManager pPluginManager;
    private NFIDictModule m_pDictModule;
    
    private static final String ENTITY_NAME = "dict";
    
    @ApiOperation("导出字典数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('dict:list')")
    public void download(HttpServletResponse response, DictQueryCriteria criteria) throws IOException {
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pDictModule = pPluginManager.FindModule(NFIDictModule.class);
        m_pDictModule.download(m_pDictModule.queryAll(criteria), response);
    }
    
    @ApiOperation("查询字典")
    @GetMapping(value = "/all")
    @PreAuthorize("@el.check('dict:list')")
    public ResponseEntity<Object> queryAll(){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pDictModule = pPluginManager.FindModule(NFIDictModule.class);
        return new ResponseEntity<>(m_pDictModule.queryAll(new DictQueryCriteria()), HttpStatus.OK);
    }
    
    @ApiOperation("查询字典")
    @GetMapping
    @PreAuthorize("@el.check('dict:list')")
    public ResponseEntity<Object> query(DictQueryCriteria resources, Pageable pageable){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pDictModule = pPluginManager.FindModule(NFIDictModule.class);
        return new ResponseEntity<>(m_pDictModule.queryAll(resources,pageable), HttpStatus.OK);
    }
    
    @ApiOperation("新增字典")
    @PostMapping
    @PreAuthorize("@el.check('dict:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Dict resources){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pDictModule = pPluginManager.FindModule(NFIDictModule.class);
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        m_pDictModule.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    
    @ApiOperation("修改字典")
    @PutMapping
    @PreAuthorize("@el.check('dict:edit')")
    public ResponseEntity<Object> update(@Validated(Dict.Update.class) @RequestBody Dict resources){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pDictModule = pPluginManager.FindModule(NFIDictModule.class);
        m_pDictModule.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @ApiOperation("删除字典")
    @DeleteMapping
    @PreAuthorize("@el.check('dict:del')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pDictModule = pPluginManager.FindModule(NFIDictModule.class);
        m_pDictModule.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}