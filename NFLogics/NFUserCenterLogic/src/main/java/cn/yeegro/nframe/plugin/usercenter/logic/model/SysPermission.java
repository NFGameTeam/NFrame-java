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
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
* @website https://docs.auauz.net
* @description /
* @author 刘斌
* @date 2020-07-27
**/
@Entity
@Data
@Table(name="sys_permission")
public class SysPermission implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "perm_id")
    @ApiModelProperty(value = "权限ID")
    private Long permId;

    @Column(name = "name")
    @ApiModelProperty(value = "名称")
    private String name;

    @Column(name = "code")
    @ApiModelProperty(value = "权限代码")
    private String code;

    @Column(name = "desc_c")
    @ApiModelProperty(value = "描述")
    private String descC;

    public void copy(SysPermission source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}