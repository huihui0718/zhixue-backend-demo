package me.zhengjie.modules.chatroom.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.sql.Timestamp;



@Data
@Table(name="chat_room")
public class ChatRoom implements Serializable {
    @TableId(type = IdType.AUTO)
    @Column(name = "`id`")
    private Integer id;

    @Column(name = "`createtime`")
    @ApiModelProperty(value = "年月日")
    private Timestamp createtime;

    @Column(name = "`title`")
    @ApiModelProperty(value = "title")
    private String title;

    @Column(name = "`user_id`")
    @ApiModelProperty(value = "user_id")
    private Integer userId;

    @Column(name = "`content`")
    @ApiModelProperty(value = "content")
    private String content;

    @Column(name = "`module`")
    @ApiModelProperty(value = "module")
    private Integer module;

    @Column(name = "`update_time`")
    @ApiModelProperty(value = "updateTime")
    private Timestamp updateTime;

    @Column(name = "`module_name`")
    @ApiModelProperty(value = "moduleName")
    private String moduleName;

    private String pathName;
}
