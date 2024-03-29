package me.zhengjie.modules.chatroom.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.zhengjie.modules.chat.domain.Chat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ChatMapper extends BaseMapper<Chat> {
    @Select("select c.* " +
            "from chat c " +
            "where room_id = #{roomId} and user_id =#{userId} " +
            "order by _id desc " +
            "LIMIT 20")
    List<Chat> findByRoomId(@Param("roomId") Integer roomId,@Param("userId") Integer userId);
}
