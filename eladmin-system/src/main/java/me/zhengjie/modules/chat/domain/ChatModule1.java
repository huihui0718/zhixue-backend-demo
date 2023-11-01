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
package me.zhengjie.modules.chat.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://eladmin.vip
* @description /
* @author 曲志强
* @date 2023-11-01
**/
@Entity
@Data
@Table(name="chat_module")
public class ChatModule1 implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "id")
    private Integer id;

    @Column(name = "`module_name`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "模型名字")
    private String moduleName;

    @Column(name = "`module_content`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "模型描述")
    private String moduleContent;

    @Column(name = "`module_url`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "模型url")
    private String moduleUrl;

    @Column(name = "`create_by`")
    @ApiModelProperty(value = "创作者")
    private Integer createBy;

    @Column(name = "`create_time`")
    @CreationTimestamp
    @ApiModelProperty(value = "创作时间")
    private Timestamp createTime;

    @Column(name = "`update_by`")
    @ApiModelProperty(value = "修改者")
    private Integer updateBy;

    @Column(name = "`update_time`")
    @UpdateTimestamp
    @ApiModelProperty(value = "修改时间")
    private Timestamp updateTime;

    @Column(name = "`path`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "图片地址")
    private String path;

    @Column(name = "`path_name`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "图片名")
    private String pathName;

    public void copy(ChatModule1 source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
