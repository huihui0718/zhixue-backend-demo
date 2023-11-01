package me.zhengjie.modules.chatroom.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.modules.chatroom.domain.ChatMessage;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

@Mapper
public interface ChatMessageMapper extends BaseMapper<ChatMessage> {

    @Delete("delete from chat_message where room_id=#{roomId}")
    int deleteByRoomId(Integer roomId);

    IPage<ChatMessage> findAll(Page<ChatMessage> page, @Param(Constants.WRAPPER) QueryWrapper<ChatMessage> wrapper);

    @Select("SELECT * FROM chat_message WHERE room_id = #{roomId} AND user_id = #{userId} ORDER BY create_time DESC")
    IPage<ChatMessage> findByRoomIdAndUserIdWithPage(Integer roomId, Integer userId, Page<ChatMessage> page);

    void deleteByCreateTime(LocalDateTime oneMonthAgo);

    @Select("SELECT * " +
            "FROM chat_message   " +
            "WHERE room_id = #{roomId} AND content LIKE CONCAT('%', #{like}, '%') " +
            "ORDER BY ${sortBy} ${sortOrder}")
    IPage<ChatMessage> findByRoomId(@Param("roomId") Integer roomId,
                                    @Param("sortBy") String sortBy,
                                    @Param("sortOrder") String sortOrder,
                                    @Param("like") String like,
                                    Page<ChatMessage> page);
}
