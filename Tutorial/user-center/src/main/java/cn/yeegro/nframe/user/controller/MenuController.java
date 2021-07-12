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
import cn.yeegro.nframe.plugin.usercenter.logic.auth.details.LoginAppUser;
import cn.yeegro.nframe.plugin.usercenter.logic.criteria.MenuQueryCriteria;
import cn.yeegro.nframe.plugin.usercenter.logic.dto.MenuDto;
import cn.yeegro.nframe.plugin.usercenter.logic.mapstruct.MenuMapper;
import cn.yeegro.nframe.plugin.usercenter.logic.model.Menu;
import cn.yeegro.nframe.plugin.usercenter.logic.module.NFIMenuModule;
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
 * @date 2018-12-03
 */

@RestController
@RequiredArgsConstructor
@Api(tags = "系统：菜单管理")
@RequestMapping("/menus")
public class MenuController {

    private NFIPluginManager pPluginManager;
    private NFIMenuModule m_pMenuModule;
    private NFIUserModule m_pSysUserModule;

    private final MenuMapper menuMapper;

    private static final String ENTITY_NAME = "menu";

    @ApiOperation("导出菜单数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('menu:list')")
    public void download(HttpServletResponse response, MenuQueryCriteria criteria) throws Exception {

        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pMenuModule = pPluginManager.FindModule(NFIMenuModule.class);

       // m_pMenuModule.download(m_pMenuModule.queryAll(criteria, false), response);
    }

    @GetMapping(value = "/build")
    @ApiOperation("获取前端所需菜单")
    public ResponseEntity<Object> buildMenus(){

        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pMenuModule = pPluginManager.FindModule(NFIMenuModule.class);
        m_pSysUserModule = pPluginManager.FindModule(NFIUserModule.class);
        LoginAppUser loginUser = m_pSysUserModule.getLoginAppUser();
        List<MenuDto> menuDtoList = m_pMenuModule.findByUser(loginUser.getId());
        List<MenuDto> menuDtos = m_pMenuModule.buildTree(menuDtoList);
        return new ResponseEntity<>(m_pMenuModule.buildMenus(menuDtos), HttpStatus.OK);
    }

    @ApiOperation("返回全部的菜单")
    @GetMapping(value = "/lazy")
    @PreAuthorize("@el.check('menu:list','roles:list')")
    public ResponseEntity<Object> query(@RequestParam Long pid){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pMenuModule = pPluginManager.FindModule(NFIMenuModule.class);
        return new ResponseEntity<>(m_pMenuModule.getMenus(pid), HttpStatus.OK);
    }

    @ApiOperation("查询菜单")
    @GetMapping
    @PreAuthorize("@el.check('menu:list')")
    public ResponseEntity<Object> query(MenuQueryCriteria criteria) throws Exception {
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pMenuModule = pPluginManager.FindModule(NFIMenuModule.class);
        List<MenuDto> menuDtoList = m_pMenuModule.queryAll(criteria, true);
        return new ResponseEntity<>(PageUtil.toPage(menuDtoList, menuDtoList.size()), HttpStatus.OK);
    }

    @ApiOperation("查询菜单:根据ID获取同级与上级数据")
    @PostMapping("/superior")
    @PreAuthorize("@el.check('menu:list')")
    public ResponseEntity<Object> getSuperior(@RequestBody List<Long> ids) {
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pMenuModule = pPluginManager.FindModule(NFIMenuModule.class);
        Set<MenuDto> menuDtos = new LinkedHashSet<>();
        if(CollectionUtil.isNotEmpty(ids)){
            for (Long id : ids) {
                MenuDto menuDto = m_pMenuModule.findById(id);
                menuDtos.addAll(m_pMenuModule.getSuperior(menuDto, new ArrayList<>()));
            }
            return new ResponseEntity<>(m_pMenuModule.buildTree(new ArrayList<>(menuDtos)), HttpStatus.OK);
        }
        return new ResponseEntity<>(m_pMenuModule.getMenus(null), HttpStatus.OK);
    }

    @ApiOperation("新增菜单")
    @PostMapping
    @PreAuthorize("@el.check('menu:add')")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Object> create(@Validated @RequestBody Menu resources){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pMenuModule = pPluginManager.FindModule(NFIMenuModule.class);
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        m_pMenuModule.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("修改菜单")
    @PutMapping
    @PreAuthorize("@el.check('menu:edit')")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Object> update(@Validated(Menu.Update.class) @RequestBody Menu resources){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pMenuModule = pPluginManager.FindModule(NFIMenuModule.class);
        m_pMenuModule.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除菜单")
    @DeleteMapping
    @PreAuthorize("@el.check('menu:del')")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pMenuModule = pPluginManager.FindModule(NFIMenuModule.class);
        Set<Menu> menuSet = new HashSet<>();
        for (Long id : ids) {
            List<MenuDto> menuList = m_pMenuModule.getMenus(id);
            menuSet.add(m_pMenuModule.findOne(id));
            menuSet = m_pMenuModule.getDeleteMenus(menuMapper.toEntity(menuList), menuSet);
        }
        m_pMenuModule.delete(menuSet);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
