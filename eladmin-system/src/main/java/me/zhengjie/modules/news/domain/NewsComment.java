package me.zhengjie.modules.news.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Table(name="news_comment")
public class NewsComment implements Serializable {
    @TableId(type = IdType.AUTO)
    @Column(name = "`id`")
    private Integer Id;

    @Column(name = "`content`")
    private String content;

    @Column(name = "`createtime`")
    private Timestamp createTime;

    @Column(name = "`likes`")
    private Integer likes;

    @Column(name = "`news_id`")
    private Integer newsId;

    @Column(name = "'parent_id'")
    private Integer parentId;

    @Column(name = "'sender_id'")
    private Integer senderId;

    private Integer commentCount;
}
