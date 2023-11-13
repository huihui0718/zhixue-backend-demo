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
package me.zhengjie.modules.newSystem.newsUserLike.rest;

import me.zhengjie.annotation.Log;
import me.zhengjie.modules.newSystem.newsUserLike.domain.NewsUserLike;
import me.zhengjie.modules.newSystem.newsUserLike.service.NewsUserLikeService;
import me.zhengjie.modules.newSystem.newsUserLike.service.dto.NewsUserLikeQueryCriteria;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://eladmin.vip
* @author 曲志强
* @date 2023-11-18
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "新闻点赞管理")
@RequestMapping("/api/newsUserLike")
public class NewsUserLikeController {

    private final NewsUserLikeService newsUserLikeService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('newsUserLike:list')")
    public void exportNewsUserLike(HttpServletResponse response, NewsUserLikeQueryCriteria criteria) throws IOException {
        newsUserLikeService.download(newsUserLikeService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询新闻点赞")
    @ApiOperation("查询新闻点赞")
    @PreAuthorize("@el.check('newsUserLike:list')")
    public ResponseEntity<Object> queryNewsUserLike(NewsUserLikeQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(newsUserLikeService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增新闻点赞")
    @ApiOperation("新增新闻点赞")
    @PreAuthorize("@el.check('newsUserLike:add')")
    public ResponseEntity<Object> createNewsUserLike(@Validated @RequestBody NewsUserLike resources){
        return new ResponseEntity<>(newsUserLikeService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改新闻点赞")
    @ApiOperation("修改新闻点赞")
    @PreAuthorize("@el.check('newsUserLike:edit')")
    public ResponseEntity<Object> updateNewsUserLike(@Validated @RequestBody NewsUserLike resources){
        newsUserLikeService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除新闻点赞")
    @ApiOperation("删除新闻点赞")
    @PreAuthorize("@el.check('newsUserLike:del')")
    public ResponseEntity<Object> deleteNewsUserLike(@RequestBody Integer[] ids) {
        newsUserLikeService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}