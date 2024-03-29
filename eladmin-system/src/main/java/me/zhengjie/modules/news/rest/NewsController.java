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
package me.zhengjie.modules.news.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.annotation.Log;
import me.zhengjie.config.FileProperties;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.newSystem.newsUserLike.domain.NewsUserLike;
import me.zhengjie.modules.newSystem.newsUserLike.repository.newsUserLikeMapper;
import me.zhengjie.modules.newSystem.newsUserStar.domain.NewsUserStar;
import me.zhengjie.modules.newSystem.newsUserStar.repository.newsUserStarMapper;
import me.zhengjie.modules.news.domain.News;
import me.zhengjie.modules.news.repository.NewsMapper;
import me.zhengjie.modules.news.repository.NewsRepository;
import me.zhengjie.modules.news.service.NewsService;
import me.zhengjie.modules.news.service.dto.NewsDto;
import me.zhengjie.modules.news.service.dto.NewsQueryCriteria;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.RedisUtils;
import me.zhengjie.utils.SecurityUtils;
import me.zhengjie.utils.StringUtils;
import org.apache.ibatis.annotations.Result;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://eladmin.vip
* @author 李煊
* @date 2023-06-04
**/
@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "咨询管理")
@RequestMapping("/api/news")
public class NewsController {

    private final RedisUtils redisUtils;
    private final NewsService newsService;
    private final FileProperties properties;
    private final NewsMapper newsMapper;
    private final NewsRepository newsRepository;
    private final newsUserLikeMapper newsUserLikeMapper;
    private final newsUserStarMapper newsUserStarMapper;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    public void exportNews(HttpServletResponse response, NewsQueryCriteria criteria) throws IOException {
        newsService.download(newsService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询咨询")
    @ApiOperation("查询咨询")
    public ResponseEntity<Object> queryNews(NewsQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(newsService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @GetMapping("/test")
    @Log("测试")
    @ApiOperation("测试")
    public ResponseEntity<Object> test(@RequestParam(value = "page") Integer page,
                                       @RequestParam(value = "size") Integer size,
                                       @RequestParam(value = "sort") String sort,
                                       @RequestParam(value = "order")String order,
                                       @RequestParam(value = "type")Integer type,
                                       @RequestParam(value = "search") String search){
        Page<News> newsPage = new Page<>(page,size);
        IPage<NewsDto> iPage = newsMapper.findNewsWithComments(newsPage,sort,order,type,search);
        Integer userId = Math.toIntExact(SecurityUtils.getCurrentUserId());
        for (NewsDto list : iPage.getRecords()){
            if(redisUtils.hHasKey("NEWS_USER_LIKE",list.getNewsId()+"::"+userId)){
                Boolean isTrue = (Boolean) redisUtils.hget("NEWS_USER_LIKE",list.getNewsId()+"::"+userId);
                list.setIsLiked(isTrue);
            } else {
                list.setIsLiked(newsUserLikeMapper.selectOne(new QueryWrapper<NewsUserLike>()
                        .eq("user_id", userId)
                        .eq("news_id", list.getNewsId()))!=null);
            }
            list.setIsStared(newsUserStarMapper.selectOne(new QueryWrapper<NewsUserStar>()
                    .eq("user_id", userId)
                    .eq("news_id", list.getNewsId()))!=null);
        }
        return new ResponseEntity<>(iPage,HttpStatus.OK);
    }

    @GetMapping("/getNewsByStarRoom")
    @Log("测试")
    @ApiOperation("测试")
    public ResponseEntity<Object> getNewsByStarRoom(@RequestParam(value = "page") Integer page,
                                                    @RequestParam(value = "size") Integer size,
                                                    @RequestParam(value = "sort") String sort,
                                                    @RequestParam(value = "order")String order,
                                                    @RequestParam(value = "type")String type,
                                                    @RequestParam(value = "search") String search,
                                                    @RequestParam(value = "starRoomId")Integer starRoomId){
        Page<News> newsPage = new Page<>(page,size);
        Integer userId = Math.toIntExact(SecurityUtils.getCurrentUserId());
        QueryWrapper<News> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(search),"news_title",search);
        queryWrapper.eq(StringUtils.isNotBlank(type),"type",type);
        if(order.equals("desc")){
            queryWrapper.orderByDesc(sort);
        }else {
            queryWrapper.orderByAsc(sort);
        }
        IPage<NewsDto> iPage = newsMapper.getNewsByStarRoom(newsPage,starRoomId,queryWrapper);
        return new ResponseEntity<>(iPage,HttpStatus.OK);
    }

    @PostMapping ("/newsUserLike")
    public ResponseEntity<Object> newsLikeAdd(@RequestParam(value = "newsId")Integer newsId,
                                              @RequestParam(value = "isLiked") Boolean isLiked){
        int userId = Math.toIntExact(SecurityUtils.getCurrentUserId());
        if (!isLiked) {
            redisUtils.hset("NEWS_USER_LIKE",newsId+"::"+userId,true,-1);
            newsMapper.updateByNewsId1(newsId);
        } else {
            redisUtils.hset("NEWS_USER_LIKE",newsId+"::"+userId,false,-1);
            newsMapper.updateByNewsId2(newsId);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("query")
    @Log("查询咨询")
    @ApiOperation("查询咨询")
    @AnonymousAccess
    public ResponseEntity<Object> queryNews2(NewsQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(newsService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @GetMapping("id/{id}")
    @Log("查询咨询")
    @ApiOperation("查询咨询")
    @AnonymousAccess
    public ResponseEntity<Object> queryNewsId(@PathVariable int id){
        return new ResponseEntity<>(newsService.findById(id),HttpStatus.OK);
    }

    @ApiOperation("上传封面")
    @PostMapping(value = "/importPic")
    public ResponseEntity<Object> importNumber(@RequestParam MultipartFile file){
        // 验证文件上传的格式
        String excel = "png jpg";
        String fileType = FileUtil.getExtensionName(file.getOriginalFilename());
        if (fileType != null && !excel.contains(fileType)) {
            throw new BadRequestException("文件格式错误！, 仅支持 " + excel + " 格式");
        }
        File files = FileUtil.upload(file, properties.getPath().getPath());
        return new ResponseEntity<>(files.getPath(), HttpStatus.OK);
    }

    @ApiOperation("上传视频")
    @PostMapping(value = "/importMP4")
    public ResponseEntity<Object> importNumber2(@RequestParam MultipartFile file){
        // 验证文件上传的格式
        String excel = "mp4";
        String fileType = FileUtil.getExtensionName(file.getOriginalFilename());
        if (fileType != null && !excel.contains(fileType)) {
            throw new BadRequestException("文件格式错误！, 仅支持 " + excel + " 格式");
        }
        File files = FileUtil.upload(file, properties.getPath().getPath());
        return new ResponseEntity<>(files.getPath(), HttpStatus.OK);
    }

    @ApiOperation("上传文件")
    @PostMapping(value = "/import")
    public ResponseEntity<Object> importNumber3(@RequestParam MultipartFile file){
        // 验证文件上传的格式
        String excel = "pdf doc docx";

        String fileType = FileUtil.getExtensionName(file.getOriginalFilename());
        if (fileType != null && !excel.contains(fileType)) {
            throw new BadRequestException("文件格式错误！, 仅支持 " + excel + " 格式");
        }
        File files = FileUtil.upload(file, properties.getPath().getPath());
        return new ResponseEntity<>(files.getPath(), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增咨询")
    @ApiOperation("新增咨询")
    @PreAuthorize("@el.check('news:add')")
    public ResponseEntity<Object> createNews(@Validated @RequestBody News resources){
        log.info(resources.getNewsContent());
        return new ResponseEntity<>(newsService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改咨询")
    @ApiOperation("修改咨询")
    @PreAuthorize("@el.check('news:edit')")
    public ResponseEntity<Object> updateNews(@Validated @RequestBody News resources){
        newsService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除咨询")
    @ApiOperation("删除咨询")
    @PreAuthorize("@el.check('news:del')")
    public ResponseEntity<Object> deleteNews(@RequestBody Integer[] ids) {
        newsService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/like")
    @Log("查询咨询")
    @ApiOperation("查询咨询")
    @AnonymousAccess
    public ResponseEntity<Object> queryNews3(@RequestParam String like, Pageable pageable){
        Page<News> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<News> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("news_title",like);
        IPage<News> newsIPage = newsMapper.selectPage(page,queryWrapper);
        return new ResponseEntity<>(newsIPage.getRecords(),HttpStatus.OK);
    }
}