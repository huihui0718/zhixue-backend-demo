/*
*  Copyright 2019-2020 Zheng Jie
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/
package me.zhengjie.modules.chat.rest;

import lombok.extern.slf4j.Slf4j;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.annotation.Log;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.chat.domain.Chat;
import me.zhengjie.modules.chat.service.ChatService;
import me.zhengjie.modules.chat.service.dto.ChatDto;
import me.zhengjie.modules.chat.service.dto.ChatQueryCriteria;
import me.zhengjie.modules.chatroom.domain.ChatModule;
import me.zhengjie.modules.chatroom.repository.ChatModuleMapper;
import me.zhengjie.util.RestTemplateUtils;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://eladmin.vip
* @author 李煊
* @date 2023-06-07
**/
@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "聊天管理")
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;
    private final ChatModuleMapper chatModuleMapper;

//    private final

    @Value("${glm.url}")
    private String glmUrl;



    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @AnonymousAccess
    public void exportChat(HttpServletResponse response, ChatQueryCriteria criteria) throws IOException {
        chatService.download(chatService.queryAll(criteria), response);
    }

    @GetMapping("/page/{roomId}")
    @Log("查询聊天page")
    @ApiOperation("查询聊天page")
    @AnonymousAccess
    public ResponseEntity<Object> queryChat(@PathVariable Integer roomId, ChatQueryCriteria criteria, Pageable pageable){
        criteria.setUserId(Math.toIntExact(SecurityUtils.getCurrentUserId()));
        criteria.setRoomId(roomId);
        Map<String, Object> stringObjectMap = chatService.queryAll(criteria, pageable);
        Object content = stringObjectMap.get("content");
        List<ChatDto> list = (List<ChatDto>) content;
        stringObjectMap.put("content",list.stream().sorted((o1, o2) -> o1.getId()-o2.getId()).collect(Collectors.toList()));
        return new ResponseEntity<>(stringObjectMap,HttpStatus.OK);
    }

    @GetMapping
    @Log("查询聊天")
    @ApiOperation("查询聊天")
    @PreAuthorize("@el.check('chat:list')")
    public ResponseEntity<Object> queryChat(ChatQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(chatService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增聊天")
    @ApiOperation("新增聊天")
    @AnonymousAccess
    public ResponseEntity<Object> createChat(HttpServletRequest request,
                                             @Validated @RequestBody Chat resources,
                                             @RequestParam("module") Integer module){
        resources.setDate(new Timestamp(System.currentTimeMillis()));
        resources.setPid(0);
        resources.setType(0);
        resources.setUserId(Math.toIntExact(SecurityUtils.getCurrentUserId()));
        resources.setSenderId(Math.toIntExact(SecurityUtils.getCurrentUserId()));
        resources.setChatType("TEXT");
        resources.setChatLike(0);
        ChatDto chatDto = chatService.create(resources);
        Integer pid = chatDto.getId();
        ChatModule chatModule = chatModuleMapper.selectById(module);
//        调用api获取回答post.get("response").toString()
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("prompt", resources.getContent());
        List<ArrayList> history = new ArrayList<>();
        //history.add(new ArrayList<>());
        requestBody.put("history",history);
        try{
            Map post = RestTemplateUtils.post(chatModule.getModuleUrl(), requestBody, Map.class,request);
            Chat chat = new Chat();
            chat.setPid(pid);
            chat.setDate(new Timestamp(System.currentTimeMillis()));
            chat.setContent(post.get("response").toString());
            chat.setRoomId(resources.getRoomId());
            chat.setSenderId(0);
            chat.setType(1);
            chat.setChatLike(0);
            chat.setChatType("TEXT");
            chat.setUserId(Math.toIntExact(SecurityUtils.getCurrentUserId()));
            ChatDto response = chatService.create(chat);
            return new ResponseEntity<>(response,HttpStatus.CREATED);
        }catch (Exception e) {
            throw new BadRequestException("请求超时");
        }
    }

    @PutMapping
    @Log("修改聊天")
    @ApiOperation("修改聊天")
    @AnonymousAccess
    public ResponseEntity<Object> updateChat(@Validated @RequestBody Chat resources){
        chatService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除聊天")
    @ApiOperation("删除聊天")
    @AnonymousAccess
    public ResponseEntity<Object> deleteChat(@RequestBody Integer[] ids) {
        chatService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}