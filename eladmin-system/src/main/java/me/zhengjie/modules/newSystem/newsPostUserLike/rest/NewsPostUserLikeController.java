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
package me.zhengjie.modules.newSystem.newsPostUserLike.rest;

import me.zhengjie.annotation.Log;
import me.zhengjie.modules.newSystem.newsPostUserLike.domain.NewsPostUserLike;
import me.zhengjie.modules.newSystem.newsPostUserLike.service.NewsPostUserLikeService;
import me.zhengjie.modules.newSystem.newsPostUserLike.service.dto.NewsPostUserLikeQueryCriteria;
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
* @date 2023-11-20
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "评论点赞管理")
@RequestMapping("/api/newsPostUserLike")
public class NewsPostUserLikeController {

    private final NewsPostUserLikeService newsPostUserLikeService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('newsPostUserLike:list')")
    public void exportNewsPostUserLike(HttpServletResponse response, NewsPostUserLikeQueryCriteria criteria) throws IOException {
        newsPostUserLikeService.download(newsPostUserLikeService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询评论点赞")
    @ApiOperation("查询评论点赞")
    @PreAuthorize("@el.check('newsPostUserLike:list')")
    public ResponseEntity<Object> queryNewsPostUserLike(NewsPostUserLikeQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(newsPostUserLikeService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增评论点赞")
    @ApiOperation("新增评论点赞")
    @PreAuthorize("@el.check('newsPostUserLike:add')")
    public ResponseEntity<Object> createNewsPostUserLike(@Validated @RequestBody NewsPostUserLike resources){
        return new ResponseEntity<>(newsPostUserLikeService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改评论点赞")
    @ApiOperation("修改评论点赞")
    @PreAuthorize("@el.check('newsPostUserLike:edit')")
    public ResponseEntity<Object> updateNewsPostUserLike(@Validated @RequestBody NewsPostUserLike resources){
        newsPostUserLikeService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除评论点赞")
    @ApiOperation("删除评论点赞")
    @PreAuthorize("@el.check('newsPostUserLike:del')")
    public ResponseEntity<Object> deleteNewsPostUserLike(@RequestBody Integer[] ids) {
        newsPostUserLikeService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}