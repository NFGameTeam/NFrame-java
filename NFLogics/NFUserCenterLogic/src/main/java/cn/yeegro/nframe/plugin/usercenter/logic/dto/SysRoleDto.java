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
package cn.yeegro.nframe.plugin.usercenter.logic.dto;

import cn.yeegro.nframe.plugin.usercenter.logic.model.Dept;
import cn.yeegro.nframe.plugin.usercenter.logic.model.Menu;
import cn.yeegro.nframe.plugin.usercenter.logic.model.SysPermission;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

/**
* @website https://docs.auauz.net
* @description /
* @author 刘斌
* @date 2020-07-27
**/
@Data
public class SysRoleDto implements Serializable {

    /** ID */
    private Long id;

    /** 名称 */
    private String name;

    /** 角色级别 */
    private Integer level;

    /** 描述 */
    private String description;

    /** 数据权限 */
    private String dataScope;

    /** 创建者 */
    private String createBy;

    /** 更新者 */
    private String updateBy;

    /** 创建日期 */
    private Timestamp createTime;

    /** 更新时间 */
    private Timestamp updateTime;

    private Set<SysPermission> permissions;

    private Set<Menu> menus;

    private Set<Dept> depts;
}