package me.zhengjie.modules.newchatroom.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.zhengjie.modules.chatroom.domain.ChatRoom;
import me.zhengjie.modules.newchatroom.domain.NewChatRoom;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NewChatRoomMapper extends BaseMapper<NewChatRoom> {

    /**/
    @Select("SELECT * FROM new_chat_message WHERE user_id = #{userId}")
    List<NewChatRoom> findById(Integer userId);
}
