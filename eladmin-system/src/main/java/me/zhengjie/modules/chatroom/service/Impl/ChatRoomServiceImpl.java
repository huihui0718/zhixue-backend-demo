package me.zhengjie.modules.chatroom.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.chat.domain.Chat;
import me.zhengjie.modules.chatroom.domain.ChatRoom;
import me.zhengjie.modules.chatroom.repository.ChatMapper;
import me.zhengjie.modules.chatroom.repository.ChatModuleMapper;
import me.zhengjie.modules.chatroom.repository.ChatRoomMapper;
import me.zhengjie.modules.chatroom.service.ChatRoomService;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {


    private final ChatRoomMapper chatRoomMapper;
    private final ChatModuleMapper chatModuleMapper;
    private final ChatRoomMapper chatMessageMapper;
    private final ChatMapper chatMapper;

    @Override
    public ChatRoom create(ChatRoom chatRoom)
    {
        chatRoom.setUserId(Math.toIntExact(SecurityUtils.getCurrentUserId()));
        chatRoom.setCreatetime(new Timestamp(System.currentTimeMillis()));
        chatRoomMapper.insert(chatRoom);
//        Chat chat = new Chat();
//        chat.setRoomId(chatRoom.getId());
//        chat.setType(1);
//        chat.setDate(new Timestamp(System.currentTimeMillis()));
//        chat.setUserId(Math.toIntExact(SecurityUtils.getCurrentUserId()));
//        chat.setContent("欢迎使用知学chat，点击下方聊天框，输入消息就可以和我聊天啦~~~");
//        chatMapper.insert(chat);
        return chatRoom;
    }

    @Override
    public List<ChatRoom> Retrieve(Integer userId){
        QueryWrapper<ChatRoom> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        List<ChatRoom> chatRooms = chatRoomMapper.findRoomById(userId);
        return chatRooms;
    }

    @Override
    public int delete(ChatRoom chatRoom){
        Integer roomId = chatRoom.getId();
        return chatRoomMapper.deleteById(roomId);
    }

    @Override
    public int update(ChatRoom chatRoom){
        return chatRoomMapper.updateById(chatRoom);
    }
}
