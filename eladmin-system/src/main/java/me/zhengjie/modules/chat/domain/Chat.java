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
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://eladmin.vip
* @description /
* @author 李煊
* @date 2023-06-07
**/
@Entity
@Data
@Table(name="chat")
public class Chat implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`_id`")
    @ApiModelProperty(value = "id")
    private Integer id;

    @Column(name = "`timestamp`")
    @ApiModelProperty(value = "年月日")
    private Timestamp timestamp;

    @Column(name = "`content`")
    @ApiModelProperty(value = "content")
    private String content;

    @Column(name = "`sender_id`")
    @ApiModelProperty(value = "user_id")
    private Integer senderId;

    @Column(name = "`date`")
    @ApiModelProperty(value = "时分")
    private Timestamp date;

    @Column(name = "`type`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "0:提问 1:回答")
    private Integer type;

    @Column(name = "`user_id`")
    @ApiModelProperty(value = "用户Id")
    private Integer userId;

    @Column(name = "`pid`")
    @ApiModelProperty(value = "用户Id")
    private Integer pid;

    @Column(name = "`room_id`")
    @ApiModelProperty(value = "房间Id")
    private Integer roomId;

    @Column(name = "`chat_hot`")
    @ApiModelProperty(value = "1:热门")
    private Integer chatHot;

    @Column(name = "`chat_like`")
    @ApiModelProperty(value = "1:喜欢")
    private Integer chatLike;

    @Column(name = "`chat_type`")
    private String chatType;

    @Column(name = "`path`")
    private String path;

    @Column(name = "`path_name`")
    private String pathName;

    public void copy(Chat source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
