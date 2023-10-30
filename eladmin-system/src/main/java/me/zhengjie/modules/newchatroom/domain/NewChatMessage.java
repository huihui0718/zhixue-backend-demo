package me.zhengjie.modules.newchatroom.domain;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.zhengjie.modules.chatroom.domain.ChatMessage;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.sql.Timestamp;

@Data
@Table(name="new_chat_room")
public class NewChatMessage {

    @TableId(type = IdType.AUTO)
    @Column(name = "`id`")
    private Integer id;

    @Column(name = "`timestamp`")
    private Timestamp timestamp;

    @Column(name = "`content`")
    private String content;

    @Column(name = "`sender_id`")
    private Integer senderId;

    @Column(name = "'room_id'")
    private Integer roomId;

    @Column(name = "'path'")
    private String path;

    @Column(name = "'path_name'")
    private String pathName;

    @Column(name = "'like'")
    private Integer like;

    @Column(name = "'type'")
    private String type;

}
