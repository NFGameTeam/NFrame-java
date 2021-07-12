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
import cn.yeegro.nframe.plugin.usercenter.logic.criteria.DictDetailQueryCriteria;
import cn.yeegro.nframe.plugin.usercenter.logic.dto.DictDetailDto;
import cn.yeegro.nframe.plugin.usercenter.logic.model.DictDetail;
import cn.yeegro.nframe.plugin.usercenter.logic.module.NFIDictModule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author Zheng Jie
* @date 2019-04-10
*/
@RestController
@RequiredArgsConstructor
@Api(tags = "系统：字典详情管理")
@RequestMapping("/dictDetail")
public class DictDetailController {

    private NFIPluginManager pPluginManager;
    private NFIDictModule m_pDictModule;
    private static final String ENTITY_NAME = "dictDetail";

    @ApiOperation("查询字典详情")
    @GetMapping
    public ResponseEntity<Object> query(DictDetailQueryCriteria criteria,
                                        @PageableDefault(sort = {"dictSort"}, direction = Sort.Direction.ASC) Pageable pageable){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pDictModule = pPluginManager.FindModule(NFIDictModule.class);
        return new ResponseEntity<>(m_pDictModule.queryAll(criteria,pageable), HttpStatus.OK);
    }

    @ApiOperation("查询多个字典详情")
    @GetMapping(value = "/map")
    public ResponseEntity<Object> getDictDetailMaps(@RequestParam String dictName){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pDictModule = pPluginManager.FindModule(NFIDictModule.class);
        String[] names = dictName.split("[,，]");
        Map<String, List<DictDetailDto>> dictMap = new HashMap<>(16);
        for (String name : names) {
            dictMap.put(name, m_pDictModule.getDictByName(name));
        }
        return new ResponseEntity<>(dictMap, HttpStatus.OK);
    }

    @ApiOperation("新增字典详情")
    @PostMapping
    @PreAuthorize("@el.check('dict:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody DictDetail resources){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pDictModule = pPluginManager.FindModule(NFIDictModule.class);
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        m_pDictModule.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("修改字典详情")
    @PutMapping
    @PreAuthorize("@el.check('dict:edit')")
    public ResponseEntity<Object> update(@Validated(DictDetail.Update.class) @RequestBody DictDetail resources){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pDictModule = pPluginManager.FindModule(NFIDictModule.class);
        m_pDictModule.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除字典详情")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('dict:del')")
    public ResponseEntity<Object> delete(@PathVariable Long id){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pDictModule = pPluginManager.FindModule(NFIDictModule.class);
        m_pDictModule.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}