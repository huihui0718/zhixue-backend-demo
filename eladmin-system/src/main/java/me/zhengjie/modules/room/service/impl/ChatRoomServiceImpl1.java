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
package me.zhengjie.modules.room.service.impl;

import me.zhengjie.modules.room.domain.ChatRoom1;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.room.repository.ChatRoomRepository1;
import me.zhengjie.modules.room.service.ChatRoomService1;
import me.zhengjie.modules.room.service.dto.ChatRoomDto1;
import me.zhengjie.modules.room.service.dto.ChatRoomQueryCriteria1;
import me.zhengjie.modules.room.service.mapstruct.ChatRoomMapper1;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @website https://eladmin.vip
* @description 服务实现
* @author 曲志强
* @date 2023-10-31
**/
@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl1 implements ChatRoomService1 {

    private final ChatRoomRepository1 chatRoomRepository;
    private final ChatRoomMapper1 chatRoomMapper;

    @Override
    public Map<String,Object> queryAll(ChatRoomQueryCriteria1 criteria, Pageable pageable){
        Page<ChatRoom1> page = chatRoomRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(chatRoomMapper::toDto));
    }

    @Override
    public List<ChatRoomDto1> queryAll(ChatRoomQueryCriteria1 criteria){
        return chatRoomMapper.toDto(chatRoomRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ChatRoomDto1 findById(Integer id) {
        ChatRoom1 chatRoom = chatRoomRepository.findById(id).orElseGet(ChatRoom1::new);
        ValidationUtil.isNull(chatRoom.getId(),"ChatRoom","id",id);
        return chatRoomMapper.toDto(chatRoom);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ChatRoomDto1 create(ChatRoom1 resources) {
        return chatRoomMapper.toDto(chatRoomRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ChatRoom1 resources) {
        ChatRoom1 chatRoom = chatRoomRepository.findById(resources.getId()).orElseGet(ChatRoom1::new);
        ValidationUtil.isNull( chatRoom.getId(),"ChatRoom","id",resources.getId());
        chatRoom.copy(resources);
        chatRoomRepository.save(chatRoom);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer id : ids) {
            chatRoomRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ChatRoomDto1> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ChatRoomDto1 chatRoom : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" title",  chatRoom.getTitle());
            map.put(" createtime",  chatRoom.getCreatetime());
            map.put(" userId",  chatRoom.getUserId());
            map.put(" content",  chatRoom.getContent());
            map.put(" module",  chatRoom.getModule());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}