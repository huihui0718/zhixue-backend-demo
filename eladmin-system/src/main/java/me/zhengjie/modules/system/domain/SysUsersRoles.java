package me.zhengjie.modules.system.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Table(name="sys_users_roles")
public class SysUsersRoles implements Serializable {
    private  Integer userId;
    private  Integer roleId;
}

