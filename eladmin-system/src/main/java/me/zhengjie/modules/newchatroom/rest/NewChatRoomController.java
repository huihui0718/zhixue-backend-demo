package me.zhengjie.modules.newchatroom.rest;


import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.chatroom.domain.ChatRoom;
import me.zhengjie.modules.chatroom.service.ChatMessageService;
import me.zhengjie.modules.chatroom.service.ChatRoomService;
import me.zhengjie.modules.mnt.websocket.WebSocketServer;
import me.zhengjie.modules.newchatroom.domain.NewChatMessage;
import me.zhengjie.modules.newchatroom.domain.NewChatRoom;
import me.zhengjie.modules.newchatroom.service.NewChatMessageService;
import me.zhengjie.modules.newchatroom.service.NewChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api/newChatRoom")
public class NewChatRoomController {
    @Autowired
    NewChatRoomService newChatRoomService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private WebSocketServer webSocketServer;

    @GetMapping
    public ResponseEntity<Object> queryChat(Pageable pageable){

        return new ResponseEntity<>(newChatRoomService.findAll(pageable), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> createChat(@Validated @RequestBody NewChatRoom resources){
        return new ResponseEntity<>(newChatRoomService.create(resources),HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids){
        newChatRoomService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Object> updateById(@RequestBody NewChatRoom resources){
        newChatRoomService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @AnonymousAccess
    @GetMapping("/getOnlineUsers/{roomId}")
    public ResponseEntity<Object> getOnlineUsersNum(@PathVariable String roomId){
        return new  ResponseEntity<>(newChatRoomService.findOnlinUser(roomId),HttpStatus.OK);
    }

    @AnonymousAccess
    @GetMapping("/addUser/{roomId}")
    public ResponseEntity<Object> addUser(@PathVariable String roomId) {
        log.info("调用/chat.add成功");
        redisTemplate.opsForSet().add("roomId:"+roomId, ""+roomId);
        webSocketServer.sendOneMessage("1","这是来自addUser的测试");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
