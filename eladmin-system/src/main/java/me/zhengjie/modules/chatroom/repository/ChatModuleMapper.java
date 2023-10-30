package me.zhengjie.modules.chatroom.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.zhengjie.modules.chatroom.domain.ChatModule;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatModuleMapper extends BaseMapper<ChatModule> {
}
