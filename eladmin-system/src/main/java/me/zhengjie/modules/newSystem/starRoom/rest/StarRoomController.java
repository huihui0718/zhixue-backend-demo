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
package me.zhengjie.modules.newSystem.starRoom.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.models.auth.In;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.newSystem.starRoom.domain.StarRoom;
import me.zhengjie.modules.newSystem.starRoom.repository.starRoomMapper;
import me.zhengjie.modules.newSystem.starRoom.service.StarRoomService;
import me.zhengjie.modules.newSystem.starRoom.service.dto.StarRoomQueryCriteria;
import me.zhengjie.utils.SecurityUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import java.security.Security;
import java.sql.Timestamp;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://eladmin.vip
* @author 曲志强
* @date 2023-11-20
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "点赞房间管理")
@RequestMapping("/api/starRoom")
public class StarRoomController {

    private final StarRoomService starRoomService;
    private final starRoomMapper starRoomMapper;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('starRoom:list')")
    public void exportStarRoom(HttpServletResponse response, StarRoomQueryCriteria criteria) throws IOException {
        starRoomService.download(starRoomService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询点赞房间")
    @ApiOperation("查询点赞房间")
    @PreAuthorize("@el.check('starRoom:list')")
    public ResponseEntity<Object> queryStarRoom(StarRoomQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(starRoomService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @GetMapping("/byPage")
    @Log("查询点赞房间")
    @ApiOperation("查询点赞房间")
    public ResponseEntity<Object> getStarRoomByPage(@RequestParam(value = "page") Integer page,
                                                    @RequestParam(value = "size") Integer size,
                                                    @RequestParam(value = "sort") String sort,
                                                    @RequestParam(value = "order") String order){
        Page<StarRoom> starRooms = new Page<>(page,size);
        Integer userId = Math.toIntExact(SecurityUtils.getCurrentUserId());
        IPage<StarRoom> starRoomIPage = starRoomMapper.getStarRoomByPage(starRooms,sort,order,userId);
        return new ResponseEntity<>(starRoomIPage,HttpStatus.OK);
    }

    @PostMapping
    @Log("新增点赞房间")
    @ApiOperation("新增点赞房间")
    @PreAuthorize("@el.check('starRoom:add')")
    public ResponseEntity<Object> createStarRoom(@Validated @RequestBody StarRoom resources){
        resources.setCreatrTime(new Timestamp(System.currentTimeMillis()));
        return new ResponseEntity<>(starRoomService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改点赞房间")
    @ApiOperation("修改点赞房间")
    @PreAuthorize("@el.check('starRoom:edit')")
    public ResponseEntity<Object> updateStarRoom(@Validated @RequestBody StarRoom resources){
        resources.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        starRoomService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除点赞房间")
    @ApiOperation("删除点赞房间")
    @PreAuthorize("@el.check('starRoom:del')")
    public ResponseEntity<Object> deleteStarRoom(@RequestBody Integer[] ids) {
        starRoomService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}