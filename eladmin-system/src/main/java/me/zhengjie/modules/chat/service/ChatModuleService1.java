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
package me.zhengjie.modules.chat.service;

import me.zhengjie.modules.chat.domain.ChatModule1;
import me.zhengjie.modules.chat.service.dto.ChatModuleDto1;
import me.zhengjie.modules.chat.service.dto.ChatModuleQueryCriteria1;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://eladmin.vip
* @description 服务接口
* @author 曲志强
* @date 2023-11-01
**/
public interface ChatModuleService1 {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(ChatModuleQueryCriteria1 criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<ChatModuleDto>
    */
    List<ChatModuleDto1> queryAll(ChatModuleQueryCriteria1 criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return ChatModuleDto
     */
    ChatModuleDto1 findById(Integer id);

    /**
    * 创建
    * @param resources /
    * @return ChatModuleDto
    */
    ChatModuleDto1 create(ChatModule1 resources);

    /**
    * 编辑
    * @param resources /
    */
    void update(ChatModule1 resources);

    /**
    * 多选删除
    * @param ids /
    */
    void deleteAll(Integer[] ids);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<ChatModuleDto1> all, HttpServletResponse response) throws IOException;
}