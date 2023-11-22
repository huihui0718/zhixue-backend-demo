package me.zhengjie.modules.news.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.zhengjie.modules.newSystem.newsPost.domain.NewsPost;
import me.zhengjie.modules.news.domain.NewsComment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;

/**
 * @website https://eladmin.vip
 * @description /
 * @author 鏉庣厞
 * @date 2023-06-04
 **/
@Data
public class NewsDto implements Serializable {

    private Integer newsId;

    private String coverImg;

    private String isHot;

    private String newsDesc;

    private String newsPath;

    private String newsTitle;

    private Timestamp publishTime;

    private String state;

    private Integer type;

    private Timestamp updateTime;

    private String newsContent;

    private String newsLike;

    private String newsStar;

    private String userId;

    private List<NewsPost> newsComments;

    private Integer commentTotol;

    private Boolean isLiked;

    private Boolean isStared;

    private String nickName;

    private String avatarName;
}