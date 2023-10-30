package me.zhengjie.modules.newchatroom.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Table(name="new_chat_room_user")
public class NewChatRoomUser  {

    @TableId(type = IdType.AUTO)
    @Column(name = "`id`")
    private Integer id;

    @Column(name = "`create_time`")
    private Timestamp createTime;

    @Column(name = "`user_id`")
    private Integer userId;

    @Column(name = "`room_id`")
    private Integer roomId;

    @Column(name = "create_by")
    private Integer createBy;

}