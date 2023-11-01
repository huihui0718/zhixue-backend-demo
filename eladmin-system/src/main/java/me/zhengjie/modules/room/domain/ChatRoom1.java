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
package me.zhengjie.modules.room.domain;

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
@Table(name="chat_room")
public class ChatRoom1 implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "id")
    private Integer id;

    @Column(name = "`title`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "title")
    private String title;

    @Column(name = "`createtime`")
    @CreationTimestamp
    @ApiModelProperty(value = "createtime")
    private Timestamp createtime;

    @Column(name = "`user_id`")
    @ApiModelProperty(value = "userId")
    private Integer userId;

    @Column(name = "`content`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "content")
    private String content;

    @Column(name = "`module`")
    @ApiModelProperty(value = "module")
    private Integer module;

    @Column(name = "`update_time`")
    @ApiModelProperty(value = "updateTime")
    private Timestamp updateTime;

    @Column(name = "`module_name`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "moduleName")
    private String moduleName;

    public void copy(ChatRoom1 source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
