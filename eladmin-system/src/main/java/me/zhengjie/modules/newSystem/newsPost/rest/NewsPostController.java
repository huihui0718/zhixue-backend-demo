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
package me.zhengjie.modules.newSystem.newsPost.rest;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.reactivex.Single;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.newSystem.newsPost.domain.NewsPost;
import me.zhengjie.modules.newSystem.newsPost.repository.newsPostMapper;
import me.zhengjie.modules.newSystem.newsPost.service.NewsPostService;
import me.zhengjie.modules.newSystem.newsPost.service.dto.NewsPostDto;
import me.zhengjie.modules.newSystem.newsPost.service.dto.NewsPostQueryCriteria;
import me.zhengjie.modules.newSystem.newsPostUserLike.domain.NewsPostUserLike;
import me.zhengjie.modules.newSystem.newsPostUserLike.repository.newsPostUserLikeMapper;
import me.zhengjie.modules.newSystem.newsUserLike.domain.NewsUserLike;
import me.zhengjie.modules.newSystem.newsUserStar.domain.NewsUserStar;
import me.zhengjie.modules.news.domain.News;
import me.zhengjie.modules.news.service.dto.NewsDto;
import me.zhengjie.utils.RedisUtils;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.data.domain.PageImpl;
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
import java.util.List;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://eladmin.vip
* @author 曲志强
* @date 2023-11-18
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "新闻评论管理")
@RequestMapping("/api/newsPost")
public class NewsPostController {

    private final NewsPostService newsPostService;
    private final newsPostMapper newsPostMapper;
    private final RedisUtils redisUtils;
    private final newsPostUserLikeMapper newsPostUserLikeMapper;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('newsPost:list')")
    public void exportNewsPost(HttpServletResponse response, NewsPostQueryCriteria criteria) throws IOException {
        newsPostService.download(newsPostService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询新闻评论")
    @ApiOperation("查询新闻评论")
    @PreAuthorize("@el.check('newsPost:list')")
    public ResponseEntity<Object> queryNewsPost(NewsPostQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(newsPostService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增新闻评论")
    @ApiOperation("新增新闻评论")
    @PreAuthorize("@el.check('newsPost:add')")
    public ResponseEntity<Object> createNewsPost(@Validated @RequestBody NewsPost resources){
        return new ResponseEntity<>(newsPostService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改新闻评论")
    @ApiOperation("修改新闻评论")
    @PreAuthorize("@el.check('newsPost:edit')")
    public ResponseEntity<Object> updateNewsPost(@Validated @RequestBody NewsPost resources){
        newsPostService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除新闻评论")
    @ApiOperation("删除新闻评论")
    @PreAuthorize("@el.check('newsPost:del')")
    public ResponseEntity<Object> deleteNewsPost(@RequestBody Integer[] ids) {
        newsPostService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 根据父评论ID查找评论
    @GetMapping(value = "/parent")
    @Log("查找父评论id")
    public ResponseEntity<Object> getNewsPostByParentId(@RequestParam(value = "page") Integer page,
                                                        @RequestParam(value = "size") Integer size,
                                                        @RequestParam(value = "parentId") Integer parentId) {
        IPage<NewsPost> iPage = newsPostMapper.selectPage(
                new Page<NewsPost>(page,size),
                new QueryWrapper<NewsPost>().eq("parent_id",parentId));
        return new ResponseEntity<>(iPage,HttpStatus.OK);
    }

    // 分页查找该文章的评论包括子评论
    @GetMapping(value = "/newsId")
    @Log("查找父评论id")
    public ResponseEntity<Object> getNewsPost(@RequestParam(value = "page") Integer page,
                                              @RequestParam(value = "size") Integer size,
                                              @RequestParam(value = "newsId") Integer newsId) {
        IPage<NewsPost> iPage = newsPostMapper.getPostByParentId(newsId,new Page<NewsPost>(page,size));
        Integer userId = Math.toIntExact(SecurityUtils.getCurrentUserId());
        for (NewsPost list : iPage.getRecords()){
            if(redisUtils.hHasKey("NEWS_POST_USER_LIKE",list.getNewsId()+"::"+userId)){
                Boolean isTrue = (Boolean) redisUtils.hget("NEWS_POST_USER_LIKE",list.getNewsId()+"::"+userId);
                list.setIsLiked(isTrue);
            } else {
                list.setIsLiked(newsPostUserLikeMapper.selectOne(new QueryWrapper<NewsPostUserLike>()
                        .eq("user_id", userId)
                        .eq("id", list.getId()))!=null);
            }
//            for(NewsPost list1 : list.getNewsPosts()){
//                if(redisUtils.hHasKey("NEWS_POST_USER_LIKE",list1.getNewsId()+"::"+userId)){
//                    Boolean isTrue = (Boolean) redisUtils.hget("NEWS_POST_USER_LIKE",list1.getNewsId()+"::"+userId);
//                    list1.setIsLiked(isTrue);
//                } else {
//                    list1.setIsLiked(newsPostUserLikeMapper.selectOne(new QueryWrapper<NewsPostUserLike>()
//                            .eq("user_id", userId)
//                            .eq("id", list.getId()))!=null);
//                }
//            }
        }
        return new ResponseEntity<>(iPage,HttpStatus.OK);
    }

    // 点赞
    @PostMapping ("/newsUserLike")
    public ResponseEntity<Object> newsLikeAdd(@RequestParam(value = "postId")Integer postId,
                                              @RequestParam(value = "isLiked") Boolean isLiked){
        int userId = Math.toIntExact(SecurityUtils.getCurrentUserId());
        if (!isLiked) {
            redisUtils.hset("NEWS_POST_USER_LIKE",postId+"::"+userId,true,-1);
            newsPostMapper.updateByPostId(postId);
        } else {
            redisUtils.hset("NEWS_POST_USER_LIKE",postId+"::"+userId,false,-1);
            newsPostMapper.updateByPostId(postId);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 增加评论
    @PostMapping(value = "/addNewsPost")
    public ResponseEntity<Object> addNewsPostByParentId(@RequestBody NewsPost newsPost) {
        newsPost.setCreateTime(new Timestamp(System.currentTimeMillis()));
        newsPost.setUserId(Math.toIntExact(SecurityUtils.getCurrentUserId()));
        NewsPostDto newsPostDto = newsPostService.create(newsPost);
        return new ResponseEntity<>(newsPostDto,HttpStatus.OK);
    }
}