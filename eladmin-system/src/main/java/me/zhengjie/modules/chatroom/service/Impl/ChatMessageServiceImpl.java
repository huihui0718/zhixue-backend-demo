package me.zhengjie.modules.chatroom.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.modules.chatroom.domain.ChatMessage;
import me.zhengjie.modules.chatroom.domain.ChatRoom;
import me.zhengjie.modules.chatroom.repository.ChatMessageMapper;
import me.zhengjie.modules.chatroom.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    @Autowired
    ChatMessageMapper chatMessageMapper;

    @Override
    public int delete(ChatRoom chatRoom){
        Integer roomId = chatRoom.getId();
        return chatMessageMapper.deleteByRoomId(roomId);
    }

    @Override
    public IPage<ChatMessage> findByRoomIdAndUserIdWithPage(Integer roomId, Integer userId, Page<ChatMessage> page) {
        return chatMessageMapper.findByRoomIdAndUserIdWithPage(roomId, userId, page);
    }

    @Override
    public List<ChatMessage> findByRoomIdWithLike(Integer roomId, String search, String sortBy, String sortOrder, Pageable pageable) {
        Page<ChatMessage> chatMessagePage = new Page<>(pageable.getPageNumber(),pageable.getPageSize());
        IPage<ChatMessage> chatMessageIPage = chatMessageMapper.findByRoomId(roomId,sortBy,sortOrder,search,chatMessagePage);
        return chatMessageIPage.getRecords();
    }

    @Override
    public Map<String, Object> findById(Integer id,Integer roomId,Pageable pageable) {
        QueryWrapper<ChatMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("room_id",roomId)
                .orderByDesc("id");
        List<ChatMessage> chatMessageList = chatMessageMapper.selectList(queryWrapper);
        int targetMessageIndex = 0;
        for (int i = 0; i < chatMessageList.size(); i++) {
            if (Objects.equals(chatMessageList.get(i).getId(), id)) {
                targetMessageIndex = i;
                break;
            }
        }
        int targetMessagePage = targetMessageIndex / pageable.getPageSize() + 1;
        Page<ChatMessage> page = new Page<>(targetMessagePage, pageable.getPageSize());
        IPage<ChatMessage> chatMessageIPage = chatMessageMapper.selectPage(page,queryWrapper);
        List<ChatMessage> chatMessageList2 = chatMessageIPage.getRecords();
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("list",chatMessageList2);
        objectMap.put("pageNumber",targetMessagePage);
        return objectMap;
    }

    @Override
    public List<ChatMessage> findByTime(Integer roomId, String time,Pageable pageable) {
        Page<ChatMessage> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<ChatMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("timestamp",time)
                .orderByDesc("id");
        IPage<ChatMessage> chatMessageIPage = chatMessageMapper.selectPage(page,queryWrapper);
        return chatMessageIPage.getRecords();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ChatMessage create(ChatMessage resources) {
        chatMessageMapper.insert(resources);
        return resources;
    }
}
