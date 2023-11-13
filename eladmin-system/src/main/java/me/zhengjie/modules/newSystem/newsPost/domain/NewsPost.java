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
package me.zhengjie.modules.newSystem.newsPost.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;

/**
* @website https://eladmin.vip
* @description /
* @author 曲志强
* @date 2023-11-18
**/
@Entity
@Data
@Table(name="news_post")
public class NewsPost implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "id")
    private Integer id;

    @Column(name = "`send_id`")
    @ApiModelProperty(value = "@对象id")
    private Integer sendId;

    @Column(name = "`content`")
    @ApiModelProperty(value = "发言")
    private String content;

    @Column(name = "`news_id`")
    @ApiModelProperty(value = "新闻id")
    private Integer newsId;

    @Column(name = "`parent_id`")
    @ApiModelProperty(value = "父评论id")
    private Integer parentId;

    @Column(name = "`user_id`")
    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @Column(name = "`create_time`")
    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @Column(name = "`post_likes`")
    @ApiModelProperty(value = "评论点赞")
    private Integer postLikes;

    @Transient
    @TableField(exist = false)
    private List<NewsPost> newsPosts;

    @Transient
    @TableField(exist = false)
    private Integer postCounts;

    @Transient
    @TableField(exist = false)
    private Boolean isLiked;

    @Transient
    @TableField(exist = false)
    private String nickName;

    @Transient
    @TableField(exist = false)
    private String avatarName;

    public void copy(NewsPost source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
