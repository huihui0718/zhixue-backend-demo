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

import me.zhengjie.modules.chat.domain.ChatModule1;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.chat.repository.ChatModuleRepository1;
import me.zhengjie.modules.chat.service.ChatModuleService1;
import me.zhengjie.modules.chat.service.dto.ChatModuleDto1;
import me.zhengjie.modules.chat.service.dto.ChatModuleQueryCriteria1;
import me.zhengjie.modules.chat.service.mapstruct.ChatModuleMapper1;
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
* @date 2023-11-01
**/
@Service
@RequiredArgsConstructor
public class ChatModuleServiceImpl1 implements ChatModuleService1 {

    private final ChatModuleRepository1 chatModuleRepository;
    private final ChatModuleMapper1 chatModuleMapper;

    @Override
    public Map<String,Object> queryAll(ChatModuleQueryCriteria1 criteria, Pageable pageable){
        Page<ChatModule1> page = chatModuleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(chatModuleMapper::toDto));
    }

    @Override
    public List<ChatModuleDto1> queryAll(ChatModuleQueryCriteria1 criteria){
        return chatModuleMapper.toDto(chatModuleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ChatModuleDto1 findById(Integer id) {
        ChatModule1 chatModule = chatModuleRepository.findById(id).orElseGet(ChatModule1::new);
        ValidationUtil.isNull(chatModule.getId(),"ChatModule","id",id);
        return chatModuleMapper.toDto(chatModule);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ChatModuleDto1 create(ChatModule1 resources) {
        return chatModuleMapper.toDto(chatModuleRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ChatModule1 resources) {
        ChatModule1 chatModule = chatModuleRepository.findById(resources.getId()).orElseGet(ChatModule1::new);
        ValidationUtil.isNull( chatModule.getId(),"ChatModule","id",resources.getId());
        chatModule.copy(resources);
        chatModuleRepository.save(chatModule);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer id : ids) {
            chatModuleRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ChatModuleDto1> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ChatModuleDto1 chatModule : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("模型名字", chatModule.getModuleName());
            map.put("模型描述", chatModule.getModuleContent());
            map.put("模型url", chatModule.getModuleUrl());
            map.put("创作者", chatModule.getCreateBy());
            map.put("创作时间", chatModule.getCreateTime());
            map.put("修改者", chatModule.getUpdateBy());
            map.put("修改时间", chatModule.getUpdateTime());
            map.put("图片地址", chatModule.getPath());
            map.put("图片名", chatModule.getPathName());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}