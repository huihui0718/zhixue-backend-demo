package me.zhengjie.modules.chatroom.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;



@Data
@Table(name="chat_module")
public class ChatModule implements Serializable {
    @TableId(type = IdType.AUTO)
    @Column(name = "`id`")
    private Integer id;

    @Column(name = "`module_name`")
    @ApiModelProperty(value = "moduleName")
    private String moduleName;

    @Column(name = "`module_content`")
    @ApiModelProperty(value = "moduleContent")
    private String moduleContent;

    @Column(name = "`module_url`")
    @ApiModelProperty(value = "moduleUrl")
    private String moduleUrl;

    @Column(name = "`create_by`")
    @ApiModelProperty(value = "createBy")
    private Integer createBy;

    @Column(name = "`create_time`")
    @ApiModelProperty(value = "createTime")
    private Timestamp createTime;

    @Column(name = "`update_by`")
    @ApiModelProperty(value = "updateBy")
    private Integer updateBy;

    @Column(name = "`update_time`")
    @ApiModelProperty(value = "updateTime")
    private Timestamp updateTime;

    @Column(name = "`path`")
    @ApiModelProperty(value = "path")
    private String path;

    @Column(name = "`path_name`")
    @ApiModelProperty(value = "pathName")
    private String pathName;
}

