package me.zhengjie.modules.news.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class NewsCommentDto implements Serializable {

    private Integer Id;

    private String content;

    private Timestamp createTime;

    private Integer likes;

    private Integer newsId;

}

