package me.zhengjie.modules.newchatroom.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "sys_user")
public class ChatUser {
    @TableId(type = IdType.AUTO)
    @Column(name = "`user_id`")
    private Integer userId;

    @Column(name = "`nickname`")
    private String nickname;

    @Column(name = "`avatar_path`")
    private String avatarPath;
}
