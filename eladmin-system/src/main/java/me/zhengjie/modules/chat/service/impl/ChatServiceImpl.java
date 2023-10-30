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
package me.zhengjie.modules.chat.service.impl;

import me.zhengjie.modules.chat.domain.Chat;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.chat.repository.ChatRepository;
import me.zhengjie.modules.chat.service.ChatService;
import me.zhengjie.modules.chat.service.dto.ChatDto;
import me.zhengjie.modules.chat.service.dto.ChatQueryCriteria;
import me.zhengjie.modules.chat.service.mapstruct.ChatMapper;
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
* @author 李煊
* @date 2023-06-07
**/
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;

    @Override
    public Map<String,Object> queryAll(ChatQueryCriteria criteria, Pageable pageable){
        Page<Chat> page = chatRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(chatMapper::toDto));
    }

    @Override
    public List<ChatDto> queryAll(ChatQueryCriteria criteria){
        return chatMapper.toDto(chatRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ChatDto findById(Integer Id) {
        Chat chat = chatRepository.findById(Id).orElseGet(Chat::new);
        ValidationUtil.isNull(chat.getId(),"Chat","Id",Id);
        return chatMapper.toDto(chat);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ChatDto create(Chat resources) {
        return chatMapper.toDto(chatRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Chat resources) {
        Chat chat = chatRepository.findById(resources.getId()).orElseGet(Chat::new);
        ValidationUtil.isNull( chat.getId(),"Chat","id",resources.getId());
        chat.copy(resources);
        chatRepository.save(chat);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer Id : ids) {
            chatRepository.deleteById(Id);
        }
    }

    @Override
    public void download(List<ChatDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ChatDto chat : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("年月日", chat.getTimestamp());
            map.put(" content",  chat.getContent());
            map.put("user_id", chat.getSenderId());
            map.put("时分", chat.getDate());
            map.put("0:提问 1:回答", chat.getType());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}