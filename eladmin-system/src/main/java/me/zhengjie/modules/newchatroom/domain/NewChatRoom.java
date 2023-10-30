package me.zhengjie.modules.newchatroom.domain;



import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Table(name="new_chat_room")
public class NewChatRoom  {

    @TableId(type = IdType.AUTO)
    @Column(name = "`id`")
    private Integer id;

    @Column(name = "`createtime`")
    private Timestamp createtime;

    @Column(name = "`title`")
    private String title;

    @Column(name = "`user_id`")
    private Integer userId;

    @Column(name = "isopen")
    private Integer isopen;

}
