package me.zhengjie.modules.chatroom.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.modules.chat.domain.Chat;
import me.zhengjie.modules.chat.service.dto.ChatDto;
import me.zhengjie.modules.chat.service.dto.ChatQueryCriteria;
import me.zhengjie.modules.chatroom.domain.ChatMessage;
import me.zhengjie.modules.chatroom.domain.ChatRoom;
import me.zhengjie.modules.chatroom.service.dto.ChatMessageQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ChatMessageService {


    ChatMessage create(ChatMessage resources);

    int delete(ChatRoom chatRoom);

    IPage<ChatMessage> findByRoomIdAndUserIdWithPage(Integer roomId, Integer userId, Page<ChatMessage> page);

    List<ChatMessage> findByRoomIdWithLike(Integer roomId, String search, String sortBy, String sortOrder, Pageable pageable);

    Map<String,Object> findById(Integer id,Integer roomId,Pageable pageable);

    List<ChatMessage> findByTime(Integer roomId, String time,Pageable pageable);
}
