package me.zhengjie.modules.news.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.apache.ibatis.annotations.Mapper;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Data
@Table(name="news_comment")
public class NewsComment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    private Integer Id;

    @Column(name = "`send_id`")
    private Integer sendId;

    @Column(name = "`content`")
    private String content;

    @Column(name = "`create_time`")
    private Timestamp createTime;

    @Column(name = "`post_likes`")
    private Integer postLikes;

    @Column(name = "`news_id`")
    private Integer newsId;

    @Column(name = "'parent_id'")
    private Integer parentId;

    @Column(name = "'user_id'")
    private Integer userId;
}
