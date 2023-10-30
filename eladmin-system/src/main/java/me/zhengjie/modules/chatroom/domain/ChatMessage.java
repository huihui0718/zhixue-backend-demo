
package me.zhengjie.modules.chatroom.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
 * @website https://eladmin.vip
 * @description /
 * @author 李煊
 * @date 2023-06-07
 **/

@Data
@Table(name="chat_message")
public class ChatMessage implements Serializable {

    @TableId(type = IdType.AUTO)
    @Column(name = "`id`")
    private Integer id;

    @Column(name = "`timestamp`")
    @ApiModelProperty(value = "年月日")
    private Timestamp timestamp;

    @Column(name = "`content`")
    @ApiModelProperty(value = "content")
    private String content;

    @Column(name = "`sender_id`")
    @ApiModelProperty(value = "user_id")
    private Integer senderId;

    @Column(name = "`date`")
    @ApiModelProperty(value = "时分")
    private Timestamp date;

    @Column(name = "`type`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "0:提问 1:回答")
    private Integer type;

    @Column(name = "`user_id`")
    private Integer userId;
    private Integer pid;

    @Column(name = "'room_id'")
    private Integer roomId;

    public void copy(ChatMessage source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
