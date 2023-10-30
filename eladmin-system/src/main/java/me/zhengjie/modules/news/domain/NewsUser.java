package me.zhengjie.modules.news.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Table(name="news_user")
public class NewsUser implements Serializable {
    @TableId(type = IdType.AUTO)
    @Column(name = "`id`")
    private Integer Id;

    @Column(name = "`news_id`")
    private String newsId;

    @Column(name = "`user_id`")
    private Integer userId;

    @Column(name = "`create_time`")
    private Timestamp createTime;

}