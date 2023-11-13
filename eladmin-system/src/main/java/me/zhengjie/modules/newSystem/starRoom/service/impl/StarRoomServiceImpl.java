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
package me.zhengjie.modules.newSystem.starRoom.service.impl;

import me.zhengjie.modules.newSystem.starRoom.domain.StarRoom;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.newSystem.starRoom.repository.StarRoomRepository;
import me.zhengjie.modules.newSystem.starRoom.service.StarRoomService;
import me.zhengjie.modules.newSystem.starRoom.service.dto.StarRoomDto;
import me.zhengjie.modules.newSystem.starRoom.service.dto.StarRoomQueryCriteria;
import me.zhengjie.modules.newSystem.starRoom.service.mapstruct.StarRoomMapper;
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
public class StarRoomServiceImpl implements StarRoomService {

    private final StarRoomRepository starRoomRepository;
    private final StarRoomMapper starRoomMapper;

    @Override
    public Map<String,Object> queryAll(StarRoomQueryCriteria criteria, Pageable pageable){
        Page<StarRoom> page = starRoomRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(starRoomMapper::toDto));
    }

    @Override
    public List<StarRoomDto> queryAll(StarRoomQueryCriteria criteria){
        return starRoomMapper.toDto(starRoomRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public StarRoomDto findById(Integer starRoomId) {
        StarRoom starRoom = starRoomRepository.findById(starRoomId).orElseGet(StarRoom::new);
        ValidationUtil.isNull(starRoom.getStarRoomId(),"StarRoom","starRoomId",starRoomId);
        return starRoomMapper.toDto(starRoom);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StarRoomDto create(StarRoom resources) {
        return starRoomMapper.toDto(starRoomRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(StarRoom resources) {
        StarRoom starRoom = starRoomRepository.findById(resources.getStarRoomId()).orElseGet(StarRoom::new);
        ValidationUtil.isNull( starRoom.getStarRoomId(),"StarRoom","id",resources.getStarRoomId());
        starRoom.copy(resources);
        starRoomRepository.save(starRoom);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer starRoomId : ids) {
            starRoomRepository.deleteById(starRoomId);
        }
    }

    @Override
    public void download(List<StarRoomDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (StarRoomDto starRoom : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" name",  starRoom.getName());
            map.put(" userId",  starRoom.getUserId());
            map.put("1:推文,2:文件,3:视频", starRoom.getType());
            map.put("描述", starRoom.getContent());
            map.put(" creatrTime",  starRoom.getCreatrTime());
            map.put(" updateTime",  starRoom.getUpdateTime());
            map.put(" coverImg",  starRoom.getCoverImg());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}