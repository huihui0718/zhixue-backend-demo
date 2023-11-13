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
package me.zhengjie.modules.newSystem.newsUserStar.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.newSystem.newsUserStar.domain.NewsUserStar;
import me.zhengjie.modules.newSystem.newsUserStar.repository.NewsUserStarRepository;
import me.zhengjie.modules.newSystem.newsUserStar.repository.newsUserStarMapper;
import me.zhengjie.modules.newSystem.newsUserStar.service.NewsUserStarService;
import me.zhengjie.modules.newSystem.newsUserStar.service.dto.NewsUserStarQueryCriteria;
import me.zhengjie.modules.news.repository.NewsMapper;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import java.sql.Timestamp;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://eladmin.vip
* @author 曲志强
* @date 2023-11-18
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "新闻收藏管理")
@RequestMapping("/api/newsUserStar")
public class NewsUserStarController {

    private final NewsUserStarRepository newsUserStarRepository;
    private final NewsUserStarService newsUserStarService;
    private final newsUserStarMapper newsUserStarMapper;
    private final NewsMapper newsMapper;

    @PostMapping("/addStar")
    @Log("新增新闻收藏")
    @ApiOperation("新增新闻收藏")
    public ResponseEntity<Object> addStar(@RequestParam(value = "newsId") Integer newsId,
                                          @RequestParam(value = "starRoomId") Integer starRoomId){
        NewsUserStar newsUserStar = new NewsUserStar();
        newsUserStar.setStarRoomId(starRoomId);
        newsUserStar.setNewsId(newsId);
        newsUserStar.setUserId(Math.toIntExact(SecurityUtils.getCurrentUserId()));
        newsUserStar.setCreateTime(new Timestamp(System.currentTimeMillis()));
        newsMapper.updateStarByNewsId1(newsId);
        return new ResponseEntity<>(newsUserStarService.create(newsUserStar),HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteStar")
    @Log("新增新闻收藏")
    @ApiOperation("新增新闻收藏")
    public ResponseEntity<Object> deleteStar(@RequestParam(value = "newsId") Integer newsId,
                                             @RequestParam(value = "starRoomId") Integer starRoomId){
        NewsUserStar newsUserStar = newsUserStarMapper.selectOne(new QueryWrapper<NewsUserStar>()
                .eq("news_id",newsId)
                .eq("star_room_id",starRoomId));
        newsUserStarRepository.delete(newsUserStar);
        newsMapper.updateStarByNewsId1(newsId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('newsUserStar:list')")
    public void exportNewsUserStar(HttpServletResponse response, NewsUserStarQueryCriteria criteria) throws IOException {
        newsUserStarService.download(newsUserStarService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询新闻收藏")
    @ApiOperation("查询新闻收藏")
    @PreAuthorize("@el.check('newsUserStar:list')")
    public ResponseEntity<Object> queryNewsUserStar(NewsUserStarQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(newsUserStarService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增新闻收藏")
    @ApiOperation("新增新闻收藏")
    @PreAuthorize("@el.check('newsUserStar:add')")
    public ResponseEntity<Object> createNewsUserStar(@Validated @RequestBody NewsUserStar resources){
        return new ResponseEntity<>(newsUserStarService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改新闻收藏")
    @ApiOperation("修改新闻收藏")
    @PreAuthorize("@el.check('newsUserStar:edit')")
    public ResponseEntity<Object> updateNewsUserStar(@Validated @RequestBody NewsUserStar resources){
        newsUserStarService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除新闻收藏")
    @ApiOperation("删除新闻收藏")
    @PreAuthorize("@el.check('newsUserStar:del')")
    public ResponseEntity<Object> deleteNewsUserStar(@RequestBody Integer[] ids) {
        newsUserStarService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}