package me.zhengjie.modules.newchatroom.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.modules.newchatroom.domain.ChatUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;


@Mapper
public interface ChatUserMapper extends BaseMapper<ChatUser> {
    @Select("select u.id,u.nickname,avatar_path" +
            "from sys_user u")
    IPage<ChatUser> findAll(Page<ChatUser> chatUserPage);

    @Select("SELECT * FROM sys_user WHERE user_id IN (${userIds})")
    Set<ChatUser> selectUsersByIds(@Param("userIds") Set<ChatUser> userIds);
}
