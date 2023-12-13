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
package me.zhengjie.modules.newSystem.starRoom.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
* @author 曲志强
* @date 2023-11-20
**/
@Entity
@Data
@Table(name="star_room")
public class StarRoom implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`star_room_id`")
    @ApiModelProperty(value = "starRoomId")
    private Integer starRoomId;

    @Column(name = "`name`")
    @ApiModelProperty(value = "name")
    private String name;

    @Column(name = "`user_id`")
    @ApiModelProperty(value = "userId")
    private Integer userId;

    @Column(name = "`type`")
    @ApiModelProperty(value = "1:推文,2:文件,3:视频")
    private Integer type;

    @Column(name = "`content`")
    @ApiModelProperty(value = "描述")
    private String content;

    @Column(name = "`creatr_time`")
    @ApiModelProperty(value = "creatrTime")
    private Timestamp creatrTime;

    @Column(name = "`update_time`")
    @ApiModelProperty(value = "updateTime")
    private Timestamp updateTime;

    @Column(name = "`cover_img`")
    @ApiModelProperty(value = "coverImg")
    private String coverImg;

    @Transient
    @TableField(exist = false)
    private Integer starCount;

    public void copy(StarRoom source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
