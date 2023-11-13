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
package me.zhengjie.modules.newSystem.newsPost.service.dto;

import lombok.Data;
import me.zhengjie.modules.newSystem.newsPost.domain.NewsPost;

import javax.persistence.Transient;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;

/**
* @website https://eladmin.vip
* @description /
* @author 曲志强
* @date 2023-11-18
**/
@Data
public class NewsPostDto implements Serializable {

    private Integer id;

    /** @对象id */
    private Integer sendId;

    /** 发言 */
    private String content;

    /** 新闻id */
    private Integer newsId;

    /** 父评论id */
    private Integer parentId;

    /** 用户id */
    private Integer userId;

    /** 创建时间 */
    private Timestamp createTime;

    /** 评论点赞 */
    private Integer postLikes;

    private List<NewsPost> newsPosts;

    private Integer postCounts;

    private String nickName;

    private String avatarName;

    private Boolean isLiked;
}