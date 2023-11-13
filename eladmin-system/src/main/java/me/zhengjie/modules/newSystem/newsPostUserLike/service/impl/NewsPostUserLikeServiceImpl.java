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
package me.zhengjie.modules.newSystem.newsPostUserLike.service.impl;

import me.zhengjie.modules.newSystem.newsPostUserLike.domain.NewsPostUserLike;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.newSystem.newsPostUserLike.repository.NewsPostUserLikeRepository;
import me.zhengjie.modules.newSystem.newsPostUserLike.service.NewsPostUserLikeService;
import me.zhengjie.modules.newSystem.newsPostUserLike.service.dto.NewsPostUserLikeDto;
import me.zhengjie.modules.newSystem.newsPostUserLike.service.dto.NewsPostUserLikeQueryCriteria;
import me.zhengjie.modules.newSystem.newsPostUserLike.service.mapstruct.NewsPostUserLikeMapper;
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
* @date 2023-11-20
**/
@Service
@RequiredArgsConstructor
public class NewsPostUserLikeServiceImpl implements NewsPostUserLikeService {

    private final NewsPostUserLikeRepository newsPostUserLikeRepository;
    private final NewsPostUserLikeMapper newsPostUserLikeMapper;

    @Override
    public Map<String,Object> queryAll(NewsPostUserLikeQueryCriteria criteria, Pageable pageable){
        Page<NewsPostUserLike> page = newsPostUserLikeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(newsPostUserLikeMapper::toDto));
    }

    @Override
    public List<NewsPostUserLikeDto> queryAll(NewsPostUserLikeQueryCriteria criteria){
        return newsPostUserLikeMapper.toDto(newsPostUserLikeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public NewsPostUserLikeDto findById(Integer id) {
        NewsPostUserLike newsPostUserLike = newsPostUserLikeRepository.findById(id).orElseGet(NewsPostUserLike::new);
        ValidationUtil.isNull(newsPostUserLike.getId(),"NewsPostUserLike","id",id);
        return newsPostUserLikeMapper.toDto(newsPostUserLike);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public NewsPostUserLikeDto create(NewsPostUserLike resources) {
        return newsPostUserLikeMapper.toDto(newsPostUserLikeRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(NewsPostUserLike resources) {
        NewsPostUserLike newsPostUserLike = newsPostUserLikeRepository.findById(resources.getId()).orElseGet(NewsPostUserLike::new);
        ValidationUtil.isNull( newsPostUserLike.getId(),"NewsPostUserLike","id",resources.getId());
        newsPostUserLike.copy(resources);
        newsPostUserLikeRepository.save(newsPostUserLike);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer id : ids) {
            newsPostUserLikeRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<NewsPostUserLikeDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (NewsPostUserLikeDto newsPostUserLike : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" userId",  newsPostUserLike.getUserId());
            map.put(" postid",  newsPostUserLike.getPostid());
            map.put(" creatrTime",  newsPostUserLike.getCreatrTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}