package me.zhengjie.modules.newchatroom.rest;


import cn.hutool.json.JSONUtil;
import cn.hutool.json.ObjectMapper;
import com.alibaba.druid.support.json.JSONUtils;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.mnt.websocket.WebSocketServer;
import me.zhengjie.modules.newchatroom.domain.NewChatMessage;
import me.zhengjie.modules.newchatroom.service.NewChatMessageService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/newChatRoomMessage")
public class NewChatMessageController {

    private final NewChatMessageService newChatMessageService;
    private final WebSocketServer webSocketServer;

    @PostMapping
    public ResponseEntity<Object> addMessage(@RequestBody NewChatMessage newChatMessage)
    {
        String roomId = newChatMessage.getRoomId() + "";
        Set<String> sidset =webSocketServer.getUsersInRoom(roomId);
        newChatMessageService.create(newChatMessage);
        String jsonMessage = JSONUtil.toJsonStr(newChatMessage);
        if (sidset != null) {
            webSocketServer.sendMoreMessage(sidset, jsonMessage);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/findByRoomId/{roomId}")
    public ResponseEntity<Object> findByRoomId(@PathVariable String roomId,
                                               Pageable pageable)
    {
        return new ResponseEntity<>(newChatMessageService.findAll(roomId,pageable),HttpStatus.OK);
    }

    @PostMapping(value = "/addImage/{roomId}")
    public ResponseEntity<Object> addImage(@RequestParam MultipartFile chatroomimage,
                                           @PathVariable String roomId) {
        NewChatMessage newChatMessage = newChatMessageService.createImage(chatroomimage,roomId);
        if (!chatroomimage.isEmpty()) {
            Set<String> sidset =webSocketServer.getUsersInRoom(roomId);
            String jsonMessage = JSONUtil.toJsonStr(newChatMessage);
            webSocketServer.sendMoreMessage(sidset,jsonMessage);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new BadRequestException("照片为空!");
        }
    }

    @GetMapping("/findByType/{roomId}")
    public ResponseEntity<Object> findByType(@PathVariable String roomId,
                                               @RequestParam String type,
                                               Pageable pageable)
    {
        return new ResponseEntity<>(newChatMessageService.findByType(roomId,type,pageable),HttpStatus.OK);
    }
}
