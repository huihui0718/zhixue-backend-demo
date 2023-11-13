package me.zhengjie.modules.chatroom.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.chatroom.domain.ChatMessage;
import me.zhengjie.modules.chatroom.repository.ChatMessageMapper;
import me.zhengjie.modules.chatroom.service.ChatMessageService;
import me.zhengjie.util.RestTemplateUtils;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatmessage")
public class ChatMessageController {

    @Value("${glm.url}")
    private String glmUrl;
    private final ChatMessageService chatMessageService;
    private final ChatMessageMapper chatMessageMapper;

    @GetMapping("/page/{roomId}")
    @Log("查询聊天")
    @ApiOperation("查询聊天")
    public ResponseEntity<Object> findByRoomIdAndUserIdWithPage(@PathVariable String roomId,
                                                                Pageable pageable) {
        String[] sort =  pageable.getSort().toString().split(":");
        Sort sort1 = pageable.getSort();
        Page<ChatMessage> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<ChatMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("room_id",roomId)
                .orderBy(true,sort[1].contains("ASC"),sort[0]);
        IPage<ChatMessage> chatMessageIPage = chatMessageMapper.selectPage(page,queryWrapper);
        List<ChatMessage> chatMessageList = chatMessageIPage.getRecords();
        return new ResponseEntity<>(chatMessageList, HttpStatus.OK);
    }

    @GetMapping("/Test/{roomId}")
    @Log("查询聊天")
    @ApiOperation("查询聊天")
    public ResponseEntity<Object> testGetMapping(@PathVariable("roomId") String roomId,
                                                 @RequestParam("search") String search,
                                                 @RequestParam("sort") String sort,
                                                 Pageable pageable) {
        Map<String,Object> map = new HashMap<>();
        map.put("roomId",roomId);
        map.put("search",search);
        map.put("sort",sort);
        map.put("number",pageable.getPageNumber());
        map.put("size",pageable.getPageSize());
        log.info(pageable.toString());
        return new ResponseEntity<>(map,HttpStatus.OK);
    }

    @GetMapping("/search/{roomId}")
    public ResponseEntity<Object> findByRoomIdWithLike(@PathVariable Integer roomId,
                                                       @RequestParam String search,
                                                       @RequestParam String sortBy,
                                                       @RequestParam String sortOrder,
                                                       Pageable pageable) {
        return new ResponseEntity<>(chatMessageService.findByRoomIdWithLike(roomId,search,sortBy,sortOrder,pageable), HttpStatus.OK);
    }

    @GetMapping("/{roomId}")
    @Log("查询聊天")
    @ApiOperation("查询聊天")
    public ResponseEntity<Object> findByTime(@PathVariable Integer roomId,@RequestParam String time,Pageable pageable) {
        return new ResponseEntity<>(chatMessageService.findByTime(roomId,time,pageable), HttpStatus.OK);
    }

    @GetMapping("/findByRoomId/{roomId}")
    @Log("查询聊天")
    @ApiOperation("查询聊天")
    public ResponseEntity<Object> findByRoomId(@PathVariable Integer roomId) {
        QueryWrapper<ChatMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("room_id",roomId);
        return new ResponseEntity<>(chatMessageMapper.selectList(queryWrapper), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable Integer id,@RequestParam Integer roomId,Pageable pageable) {
        return new ResponseEntity<>(chatMessageService.findById(id,roomId,pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增聊天")
    @ApiOperation("新增聊天")
    public ResponseEntity<Object> createChat1(HttpServletRequest request, @Validated @RequestBody ChatMessage resources){
        resources.setDate(new Timestamp(System.currentTimeMillis()));
        resources.setPid(0);
        resources.setType(0);
        resources.setUserId(Math.toIntExact(SecurityUtils.getCurrentUserId()));
        resources.setSenderId(Math.toIntExact(SecurityUtils.getCurrentUserId()));
        ChatMessage chatMessage = chatMessageService.create(resources);
        //调用api获取回答
        chatMessage.setType(1);
        ChatMessage response = chatMessageService.create(chatMessage);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @PostMapping(value = "/grm")
    @Log("新增聊天")
    @ApiOperation("新增聊天")
    public ResponseEntity<Object> createChat(HttpServletRequest request, @Validated @RequestBody ChatMessage resources){
        resources.setDate(new Timestamp(System.currentTimeMillis()));
        resources.setPid(0);
        resources.setType(0);
        resources.setUserId(Math.toIntExact(SecurityUtils.getCurrentUserId()));
        resources.setSenderId(Math.toIntExact(SecurityUtils.getCurrentUserId()));
        ChatMessage chatMessage = chatMessageService.create(resources);
        Integer pid = chatMessage.getId();
        Integer roomid = chatMessage.getRoomId();
        //调用api获取回答
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("prompt", resources.getContent());
        List<ArrayList> history = new ArrayList<>();
//        history.add(new ArrayList<>());
        requestBody.put("history",history);
        Map post = RestTemplateUtils.post(glmUrl, requestBody, Map.class,request);
        //开始回复
        ChatMessage chat = new ChatMessage();
        chat.setPid(pid);
        chat.setDate(new Timestamp(System.currentTimeMillis()));
        chat.setContent(post.get("response").toString());
        chat.setSenderId(0);
        chat.setType(1);
        chat.setRoomId(roomid);
        chat.setUserId(Math.toIntExact(SecurityUtils.getCurrentUserId()));
        ChatMessage response = chatMessageService.create(chat);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }
}
