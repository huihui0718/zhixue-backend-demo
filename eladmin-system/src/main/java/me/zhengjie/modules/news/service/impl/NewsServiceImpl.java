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
package me.zhengjie.modules.news.service.impl;

import me.zhengjie.modules.news.domain.News;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.news.repository.NewsRepository;
import me.zhengjie.modules.news.service.NewsService;
import me.zhengjie.modules.news.service.dto.NewsDto;
import me.zhengjie.modules.news.service.dto.NewsQueryCriteria;
import me.zhengjie.modules.news.service.mapstruct.NewsMapper;
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
* @date 2023-06-04
**/
@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;

    @Override
    public Map<String,Object> queryAll(NewsQueryCriteria criteria, Pageable pageable){
        Page<News> page = newsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(newsMapper::toDto));
    }

    @Override
    public List<NewsDto> queryAll(NewsQueryCriteria criteria){
        return newsMapper.toDto(newsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public NewsDto findById(Integer newsId) {
        News news = newsRepository.findById(newsId).orElseGet(News::new);
        ValidationUtil.isNull(news.getNewsId(),"News","newsId",newsId);
        return newsMapper.toDto(news);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public NewsDto create(News resources) {
        return newsMapper.toDto(newsRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(News resources) {
        News news = newsRepository.findById(resources.getNewsId()).orElseGet(News::new);
        ValidationUtil.isNull( news.getNewsId(),"News","id",resources.getNewsId());
        news.copy(resources);
        newsRepository.save(news);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer newsId : ids) {
            newsRepository.deleteById(newsId);
        }
    }

    @Override
    public void download(List<NewsDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (NewsDto news : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" coverImg",  news.getCoverImg());
            map.put(" isHot",  news.getIsHot());
            map.put(" newsDesc",  news.getNewsDesc());
            map.put(" newsPath",  news.getNewsPath());
            map.put(" newsTitle",  news.getNewsTitle());
            map.put(" publishTime",  news.getPublishTime());
            map.put(" state",  news.getState());
            map.put(" type",  news.getType());
            map.put(" updateTime",  news.getUpdateTime());
            map.put(" newsContent",  news.getNewsContent());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}