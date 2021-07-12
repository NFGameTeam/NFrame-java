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
package cn.yeegro.nframe.plugin.usercenter.logic.model;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.yeegro.nframe.common.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
* @website https://docs.auauz.net
* @description /
* @author 刘斌
* @date 2020-07-27
**/
@Entity
@Data
@Table(name="sys_role")
public class SysRole extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    @ApiModelProperty(value = "ID")
    private Long id;

    @Column(name = "code",unique = true,nullable = false)
    @NotBlank
    @ApiModelProperty(value = "角色code")
    private String code;

    @Column(name = "name",unique = true,nullable = false)
    @NotBlank
    @ApiModelProperty(value = "名称")
    private String name;

    @Column(name = "level")
    @ApiModelProperty(value = "角色级别")
    private Integer level;

    @Column(name = "description")
    @ApiModelProperty(value = "描述")
    private String description;

    @Column(name = "data_scope")
    @ApiModelProperty(value = "数据权限")
    private String dataScope;

    @ManyToMany
    @JoinTable(name = "sys_roles_perms",
            joinColumns = {@JoinColumn(name = "role_id",referencedColumnName = "role_id")},
            inverseJoinColumns = {@JoinColumn(name = "perm_id",referencedColumnName = "perm_id")})
    @ApiModelProperty(value = "权限", hidden = true)
    private Set<SysPermission> permissions;

    @ManyToMany
    @JoinTable(name = "sys_roles_menus",
            joinColumns = {@JoinColumn(name = "role_id",referencedColumnName = "role_id")},
            inverseJoinColumns = {@JoinColumn(name = "menu_id",referencedColumnName = "menu_id")})
    @ApiModelProperty(value = "菜单", hidden = true)
    private Set<Menu> menus;

    @ManyToMany
    @JoinTable(name = "sys_roles_depts",
            joinColumns = {@JoinColumn(name = "role_id",referencedColumnName = "role_id")},
            inverseJoinColumns = {@JoinColumn(name = "dept_id",referencedColumnName = "dept_id")})
    @ApiModelProperty(value = "部门", hidden = true)
    private Set<Dept> depts;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SysRole role = (SysRole) o;
        return Objects.equals(id, role.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void copy(SysRole source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}