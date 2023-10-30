package me.zhengjie.modules.chatroom.rest;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.chatroom.domain.ChatRoom;
import me.zhengjie.modules.chatroom.service.ChatMessageService;
import me.zhengjie.modules.chatroom.service.ChatRoomService;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/chatroom")
public class ChatRoomController {

    @Autowired
    ChatRoomService chatRoomService;

    @Autowired
    ChatMessageService chatMessageService;

    @GetMapping
    public ResponseEntity<Object> queryChat(){
        Integer userId = Math.toIntExact(SecurityUtils.getCurrentUserId());
        return new ResponseEntity<>(chatRoomService.Retrieve(userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> createChat(@RequestBody ChatRoom resources){
        return new ResponseEntity<>(chatRoomService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Object> updateChat(@RequestBody ChatRoom resources){
        chatRoomService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteChat(@RequestBody ChatRoom resources) {
        chatRoomService.delete(resources);
        chatMessageService.delete(resources);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
