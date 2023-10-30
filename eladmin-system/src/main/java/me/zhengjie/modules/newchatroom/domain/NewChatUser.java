package me.zhengjie.modules.newchatroom.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Table(name="new_chat_room")
public class NewChatUser  {

    @TableId(type = IdType.AUTO)
    @Column(name = "`id`")
    private Integer id;

    @Column(name = "`sender_id`")
    private Integer senderId;

    @Column(name = "`to_user_id`")
    private String toUserId;

    @Column(name = "`time`")
    private Timestamp time;

    @Column(name = "content")
    private String content;

}