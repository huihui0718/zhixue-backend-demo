package me.zhengjie.modules.newchatroom.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.mnt.websocket.WebSocketServer;
import me.zhengjie.modules.newchatroom.domain.ChatUser;
import me.zhengjie.modules.newchatroom.domain.NewChatRoom;
import me.zhengjie.modules.newchatroom.repository.ChatUserMapper;
import me.zhengjie.modules.newchatroom.repository.NewChatMessageMapper;
import me.zhengjie.modules.newchatroom.repository.NewChatRoomMapper;
import me.zhengjie.modules.newchatroom.service.NewChatRoomService;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.amqp.core.ExchangeBuilder.directExchange;

@Service
@RequiredArgsConstructor
public class NewChatRoomServiceImpl implements NewChatRoomService {

    private final NewChatRoomMapper newChatRoomMapper;
    private final ChatUserMapper chatUserMapper;
    private final WebSocketServer webSocketServer;

    @Override
    public List<NewChatRoom> findbyid(Integer userid) {

        return newChatRoomMapper.findById(userid);
    }

    @Override
    public NewChatRoom create(NewChatRoom resources) {
        resources.setUserId(Math.toIntExact(SecurityUtils.getCurrentUserId()));
        resources.setIsopen(1);
        resources.setCreatetime(new Timestamp(System.currentTimeMillis()));
        newChatRoomMapper.insert(resources);
        return resources;
    }

    @Override
    public void updateById(NewChatRoom resources) {
        newChatRoomMapper.updateById(resources);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer id:ids){
            newChatRoomMapper.deleteById(id);
        }
    }

    @Override
    public List<NewChatRoom> findAll(Pageable pageable) {
        Page<NewChatRoom> newChatRoomPage = new Page<>(pageable.getPageNumber(),pageable.getPageSize());
        IPage<NewChatRoom> newChatRoomIPage = newChatRoomMapper.selectPage(newChatRoomPage,null);
        return newChatRoomIPage.getRecords();
    }

    @Override
    public List<ChatUser> findOnlinUser(String roomId) {
        Set<String> usersInRoom = webSocketServer.getUsersInRoom(roomId);
        List<Integer> userIdList = usersInRoom.stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());
        return chatUserMapper.selectBatchIds(userIdList);
    }
}
