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
package me.zhengjie.modules.newSystem.newsPost.service.impl;

import me.zhengjie.modules.newSystem.newsPost.domain.NewsPost;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.newSystem.newsPost.repository.NewsPostRepository;
import me.zhengjie.modules.newSystem.newsPost.service.NewsPostService;
import me.zhengjie.modules.newSystem.newsPost.service.dto.NewsPostDto;
import me.zhengjie.modules.newSystem.newsPost.service.dto.NewsPostQueryCriteria;
import me.zhengjie.modules.newSystem.newsPost.service.mapstruct.NewsPostMapper;
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
* @date 2023-11-18
**/
@Service
@RequiredArgsConstructor
public class NewsPostServiceImpl implements NewsPostService {

    private final NewsPostRepository newsPostRepository;
    private final NewsPostMapper newsPostMapper;

    @Override
    public Map<String,Object> queryAll(NewsPostQueryCriteria criteria, Pageable pageable){
        Page<NewsPost> page = newsPostRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(newsPostMapper::toDto));
    }

    @Override
    public List<NewsPostDto> queryAll(NewsPostQueryCriteria criteria){
        return newsPostMapper.toDto(newsPostRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public NewsPostDto findById(Integer id) {
        NewsPost newsPost = newsPostRepository.findById(id).orElseGet(NewsPost::new);
        ValidationUtil.isNull(newsPost.getId(),"NewsPost","id",id);
        return newsPostMapper.toDto(newsPost);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public NewsPostDto create(NewsPost resources) {
        return newsPostMapper.toDto(newsPostRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(NewsPost resources) {
        NewsPost newsPost = newsPostRepository.findById(resources.getId()).orElseGet(NewsPost::new);
        ValidationUtil.isNull( newsPost.getId(),"NewsPost","id",resources.getId());
        newsPost.copy(resources);
        newsPostRepository.save(newsPost);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer id : ids) {
            newsPostRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<NewsPostDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (NewsPostDto newsPost : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("@对象id", newsPost.getSendId());
            map.put("发言", newsPost.getContent());
            map.put("新闻id", newsPost.getNewsId());
            map.put("父评论id", newsPost.getParentId());
            map.put("用户id", newsPost.getUserId());
            map.put("创建时间", newsPost.getCreateTime());
            map.put("评论点赞", newsPost.getPostLikes());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}