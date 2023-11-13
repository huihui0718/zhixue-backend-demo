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
package me.zhengjie.modules.news.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

import me.zhengjie.modules.newSystem.newsPost.domain.NewsPost;
import org.hibernate.annotations.*;
import java.sql.Timestamp;
import java.io.Serializable;


@Entity
@Data
@Table(name="news")
public class News implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`news_id`")
    @ApiModelProperty(value = "newsId")
    private Integer newsId;

    @Column(name = "`cover_img`")
    @ApiModelProperty(value = "coverImg")
    private String coverImg;

    @Column(name = "`is_hot`")
    @ApiModelProperty(value = "isHot")
    private String isHot;

    @Column(name = "`news_desc`")
    @ApiModelProperty(value = "newsDesc")
    private String newsDesc;

    @Column(name = "`news_path`")
    @ApiModelProperty(value = "newsPath")
    private String newsPath;

    @Column(name = "`news_title`")
    @ApiModelProperty(value = "newsTitle")
    private String newsTitle;

    @Column(name = "`publish_time`")
    @CreationTimestamp
    @ApiModelProperty(value = "publishTime")
    private Timestamp publishTime;

    @Column(name = "`state`")
    @ApiModelProperty(value = "state")
    private String state;

    @Column(name = "`type`")
    @ApiModelProperty(value = "type")
    private Integer type;

    @Column(name = "`update_time`")
    @UpdateTimestamp
    @ApiModelProperty(value = "updateTime")
    private Timestamp updateTime;

    @Column(name = "`news_content`")
    @ApiModelProperty(value = "newsContent")
    private String newsContent;

    @Column(name = "`news_like`")
    private String newsLike;

    @Column(name = "`news_star`")
    private String newsStar;

    @Column(name = "`user_id`")
    private String userId;

    @Transient
    @TableField(exist = false)
    private List<NewsPost> newsComments;

    @Transient
    @TableField(exist = false)
    private Integer commentCount;

    @Transient
    @TableField(exist = false)
    private Boolean isLiked;

    @Transient
    @TableField(exist = false)
    private Boolean isStared;

    @Transient
    @TableField(exist = false)
    private String nickName;

    @Transient
    @TableField(exist = false)
    private String avatarName;

    public void copy(News source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
