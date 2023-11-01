package me.zhengjie.modules.news.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.io.Serializable;

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
}