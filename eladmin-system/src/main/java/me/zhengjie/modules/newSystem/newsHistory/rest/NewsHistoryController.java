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
package me.zhengjie.modules.newSystem.newsHistory.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.annotation.Log;
import me.zhengjie.annotation.Query;
import me.zhengjie.modules.newSystem.newsHistory.domain.NewsHistory;
import me.zhengjie.modules.newSystem.newsHistory.repository.newsHistoryMapper;
import me.zhengjie.modules.newSystem.newsHistory.service.NewsHistoryService;
import me.zhengjie.modules.newSystem.newsHistory.service.dto.NewsHistoryQueryCriteria;
import me.zhengjie.modules.news.domain.News;
import me.zhengjie.modules.news.repository.NewsMapper;
import me.zhengjie.utils.RedisUtils;
import me.zhengjie.utils.SecurityUtils;
import org.apache.ibatis.annotations.Result;
import org.checkerframework.checker.units.qual.N;
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
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://eladmin.vip
* @author 曲志强
* @date 2023-11-21
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "浏览信息管理")
@RequestMapping("/api/newsHistory")
public class NewsHistoryController {

    private final RedisUtils redisUtils;
    private final NewsMapper newsMapper;
    private final newsHistoryMapper newsHistoryMapper;
    private final NewsHistoryService newsHistoryService;

    @PostMapping("/addHistoryByRedis")
    public ResponseEntity<Object> addHistoryByRedis(@RequestParam(value = "newsId") Integer newsId){
        int userId = Math.toIntExact(SecurityUtils.getCurrentUserId());
        redisUtils.zset("NEWS_HISTORY:"+userId,newsId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getHistoryByRedis")
    public ResponseEntity<Object> getHistoryByRedis(@RequestParam(value = "page") Integer page,
                                                    @RequestParam(value = "size") Integer size){
        int userId = Math.toIntExact(SecurityUtils.getCurrentUserId());
        Set<Integer> ids;
        List<News> newsList = null;
        QueryWrapper<NewsHistory> newsHistoryQueryWrapper = new QueryWrapper<>();
//        newsHistoryQueryWrapper.between(start!=null&&end!=null, "create",start,end);
        newsHistoryQueryWrapper.orderByDesc("create_time");
        List<News> newsList1 = newsHistoryMapper.getNewsByHistory(new Page<>(page,size),userId,newsHistoryQueryWrapper).getRecords();
        if (redisUtils.hasKey("NEWS_HISTORY:"+userId)&&page==0) {
            Set<Object> newsHistories = redisUtils.getHistory("NEWS_HISTORY:"+userId);
            ids = newsHistories.stream()
                    .map(obj -> Integer.parseInt(obj.toString()))
                    .collect(Collectors.toSet());
            newsList = newsMapper.selectBatchIds(ids);
            newsList1.removeIf(news -> ids.contains(news.getNewsId()));
        }
        if (newsList!=null) {
            newsList.addAll(newsList1);
            return new ResponseEntity<>(newsList,HttpStatus.OK);
        }
        return new ResponseEntity<>(newsList1,HttpStatus.OK);
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('newsHistory:list')")
    public void exportNewsHistory(HttpServletResponse response, NewsHistoryQueryCriteria criteria) throws IOException {
        newsHistoryService.download(newsHistoryService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询浏览信息")
    @ApiOperation("查询浏览信息")
    @PreAuthorize("@el.check('newsHistory:list')")
    public ResponseEntity<Object> queryNewsHistory(NewsHistoryQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(newsHistoryService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增浏览信息")
    @ApiOperation("新增浏览信息")
    @PreAuthorize("@el.check('newsHistory:add')")
    public ResponseEntity<Object> createNewsHistory(@Validated @RequestBody NewsHistory resources){
        return new ResponseEntity<>(newsHistoryService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改浏览信息")
    @ApiOperation("修改浏览信息")
    @PreAuthorize("@el.check('newsHistory:edit')")
    public ResponseEntity<Object> updateNewsHistory(@Validated @RequestBody NewsHistory resources){
        newsHistoryService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除浏览信息")
    @ApiOperation("删除浏览信息")
    @PreAuthorize("@el.check('newsHistory:del')")
    public ResponseEntity<Object> deleteNewsHistory(@RequestBody Integer[] ids) {
        newsHistoryService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}