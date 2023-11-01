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

import me.zhengjie.annotation.Log;
import me.zhengjie.modules.chat.domain.ChatModule1;
import me.zhengjie.modules.chat.service.ChatModuleService1;
import me.zhengjie.modules.chat.service.dto.ChatModuleQueryCriteria1;
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
* @date 2023-11-01
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "模型管理")
@RequestMapping("/api/chatModule1")
public class ChatModuleController1 {

    private final ChatModuleService1 chatModuleService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('chatModule:list')")
    public void exportChatModule(HttpServletResponse response, ChatModuleQueryCriteria1 criteria) throws IOException {
        chatModuleService.download(chatModuleService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询模型")
    @ApiOperation("查询模型")
    @PreAuthorize("@el.check('chatModule:list')")
    public ResponseEntity<Object> queryChatModule(ChatModuleQueryCriteria1 criteria, Pageable pageable){
        return new ResponseEntity<>(chatModuleService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增模型")
    @ApiOperation("新增模型")
    @PreAuthorize("@el.check('chatModule:add')")
    public ResponseEntity<Object> createChatModule(@Validated @RequestBody ChatModule1 resources){
        return new ResponseEntity<>(chatModuleService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改模型")
    @ApiOperation("修改模型")
    @PreAuthorize("@el.check('chatModule:edit')")
    public ResponseEntity<Object> updateChatModule(@Validated @RequestBody ChatModule1 resources){
        chatModuleService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除模型")
    @ApiOperation("删除模型")
    @PreAuthorize("@el.check('chatModule:del')")
    public ResponseEntity<Object> deleteChatModule(@RequestBody Integer[] ids) {
        chatModuleService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}