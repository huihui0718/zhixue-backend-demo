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
package me.zhengjie.modules.newSystem.newsUserStar.service.impl;

import me.zhengjie.modules.newSystem.newsUserStar.domain.NewsUserStar;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.newSystem.newsUserStar.repository.NewsUserStarRepository;
import me.zhengjie.modules.newSystem.newsUserStar.service.NewsUserStarService;
import me.zhengjie.modules.newSystem.newsUserStar.service.dto.NewsUserStarDto;
import me.zhengjie.modules.newSystem.newsUserStar.service.dto.NewsUserStarQueryCriteria;
import me.zhengjie.modules.newSystem.newsUserStar.service.mapstruct.NewsUserStarMapper;
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
public class NewsUserStarServiceImpl implements NewsUserStarService {

    private final NewsUserStarRepository newsUserStarRepository;
    private final NewsUserStarMapper newsUserStarMapper;

    @Override
    public Map<String,Object> queryAll(NewsUserStarQueryCriteria criteria, Pageable pageable){
        Page<NewsUserStar> page = newsUserStarRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(newsUserStarMapper::toDto));
    }

    @Override
    public List<NewsUserStarDto> queryAll(NewsUserStarQueryCriteria criteria){
        return newsUserStarMapper.toDto(newsUserStarRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public NewsUserStarDto findById(Integer id) {
        NewsUserStar newsUserStar = newsUserStarRepository.findById(id).orElseGet(NewsUserStar::new);
        ValidationUtil.isNull(newsUserStar.getId(),"NewsUserStar","id",id);
        return newsUserStarMapper.toDto(newsUserStar);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public NewsUserStarDto create(NewsUserStar resources) {
        return newsUserStarMapper.toDto(newsUserStarRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(NewsUserStar resources) {
        NewsUserStar newsUserStar = newsUserStarRepository.findById(resources.getId()).orElseGet(NewsUserStar::new);
        ValidationUtil.isNull( newsUserStar.getId(),"NewsUserStar","id",resources.getId());
        newsUserStar.copy(resources);
        newsUserStarRepository.save(newsUserStar);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer id : ids) {
            newsUserStarRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<NewsUserStarDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (NewsUserStarDto newsUserStar : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" newsId",  newsUserStar.getNewsId());
            map.put(" userId",  newsUserStar.getUserId());
            map.put(" createTime",  newsUserStar.getCreateTime());
            map.put(" starRoomId",  newsUserStar.getStarRoomId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}