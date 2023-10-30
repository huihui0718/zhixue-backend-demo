package me.zhengjie.modules.newchatroom.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Table(name="new_chat_comment")
public class NewChatComment {

    @TableId(type = IdType.AUTO)
    @Column(name = "`id`")
    private Integer id;

    @Column(name = "`content`")
    private String content;

    @Column(name = "`to`")
    private Integer to;

    @Column(name = "`sender_id`")
    private Integer senderId;

    @Column(name = "'parents_id'")
    private Integer parentsId;

    @Column(name = "'like'")
    private Integer like;

    @Column(name = "`createtime`")
    private Timestamp timestamp;

    @Column(name = "'room_id'")
    private Integer roomId;

    @Column(name = "'type'")
    private String type;
}
