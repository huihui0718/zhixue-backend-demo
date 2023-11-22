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
package me.zhengjie.modules.newSystem.newsHistory.service.impl;

import me.zhengjie.modules.newSystem.newsHistory.domain.NewsHistory;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.newSystem.newsHistory.repository.NewsHistoryRepository;
import me.zhengjie.modules.newSystem.newsHistory.service.NewsHistoryService;
import me.zhengjie.modules.newSystem.newsHistory.service.dto.NewsHistoryDto;
import me.zhengjie.modules.newSystem.newsHistory.service.dto.NewsHistoryQueryCriteria;
import me.zhengjie.modules.newSystem.newsHistory.service.mapstruct.NewsHistoryMapper;
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
* @date 2023-11-21
**/
@Service
@RequiredArgsConstructor
public class NewsHistoryServiceImpl implements NewsHistoryService {

    private final NewsHistoryRepository newsHistoryRepository;
    private final NewsHistoryMapper newsHistoryMapper;

    @Override
    public Map<String,Object> queryAll(NewsHistoryQueryCriteria criteria, Pageable pageable){
        Page<NewsHistory> page = newsHistoryRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(newsHistoryMapper::toDto));
    }

    @Override
    public List<NewsHistoryDto> queryAll(NewsHistoryQueryCriteria criteria){
        return newsHistoryMapper.toDto(newsHistoryRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public NewsHistoryDto findById(Integer id) {
        NewsHistory newsHistory = newsHistoryRepository.findById(id).orElseGet(NewsHistory::new);
        ValidationUtil.isNull(newsHistory.getId(),"NewsHistory","id",id);
        return newsHistoryMapper.toDto(newsHistory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public NewsHistoryDto create(NewsHistory resources) {
        return newsHistoryMapper.toDto(newsHistoryRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(NewsHistory resources) {
        NewsHistory newsHistory = newsHistoryRepository.findById(resources.getId()).orElseGet(NewsHistory::new);
        ValidationUtil.isNull( newsHistory.getId(),"NewsHistory","id",resources.getId());
        newsHistory.copy(resources);
        newsHistoryRepository.save(newsHistory);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer id : ids) {
            newsHistoryRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<NewsHistoryDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (NewsHistoryDto newsHistory : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" newsId",  newsHistory.getNewsId());
            map.put(" userId",  newsHistory.getUserId());
            map.put(" createTime",  newsHistory.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}