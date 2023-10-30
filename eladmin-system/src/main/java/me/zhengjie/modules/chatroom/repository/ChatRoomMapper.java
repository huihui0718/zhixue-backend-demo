package me.zhengjie.modules.chatroom.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.zhengjie.modules.chatroom.domain.ChatMessage;
import me.zhengjie.modules.chatroom.domain.ChatRoom;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ChatRoomMapper extends BaseMapper<ChatRoom> {

    @Select("SELECT * FROM chat_message WHERE user_id = #{userId}")
    List<ChatRoom> findById(Integer userId);


}
