package me.zhengjie.modules.newchatroom.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.zhengjie.modules.newchatroom.domain.NewChatUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NewChatUserMapper extends BaseMapper<NewChatUser> {
}
