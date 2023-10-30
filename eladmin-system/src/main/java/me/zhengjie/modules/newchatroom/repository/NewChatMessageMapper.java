package me.zhengjie.modules.newchatroom.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.zhengjie.modules.chatroom.domain.ChatMessage;
import me.zhengjie.modules.newchatroom.domain.NewChatMessage;
import me.zhengjie.modules.newchatroom.domain.NewChatRoom;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

@Mapper
public interface NewChatMessageMapper extends BaseMapper<NewChatMessage> {
}
