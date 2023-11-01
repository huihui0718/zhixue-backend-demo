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
package me.zhengjie.modules.room.rest;

import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.room.domain.ChatRoom1;
import me.zhengjie.modules.room.service.ChatRoomService1;
import me.zhengjie.modules.room.service.dto.ChatRoomQueryCriteria1;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://eladmin.vip
* @author 曲志强
* @date 2023-10-31
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "房间管理")
@RequestMapping("/api/chatRoom1")
public class ChatRoomController1 {

    private final ChatRoomService1 chatRoomService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @AnonymousAccess
    public void exportChatRoom(HttpServletResponse response, ChatRoomQueryCriteria1 criteria) throws IOException {
        chatRoomService.download(chatRoomService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询房间")
    @ApiOperation("查询房间")
    @AnonymousAccess
    public ResponseEntity<Object> queryChatRoom(ChatRoomQueryCriteria1 criteria, Pageable pageable){

        criteria.setUserId(Math.toIntExact(SecurityUtils.getCurrentUserId()));
        return new ResponseEntity<>(chatRoomService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增房间")
    @ApiOperation("新增房间")
    @AnonymousAccess
    public ResponseEntity<Object> createChatRoom(@Validated @RequestBody ChatRoom1 resources){
        return new ResponseEntity<>(chatRoomService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改房间")
    @ApiOperation("修改房间")
    @AnonymousAccess
    public ResponseEntity<Object> updateChatRoom(@Validated @RequestBody ChatRoom1 resources){
        chatRoomService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除房间")
    @ApiOperation("删除房间")
    @AnonymousAccess
    public ResponseEntity<Object> deleteChatRoom(@RequestBody Integer[] ids) {
        chatRoomService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}