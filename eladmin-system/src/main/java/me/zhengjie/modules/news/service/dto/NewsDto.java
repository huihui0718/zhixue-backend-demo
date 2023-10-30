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
package me.zhengjie.modules.news.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://eladmin.vip
* @description /
* @author 李煊
* @date 2023-06-04
**/
@Data
public class NewsDto implements Serializable {

    private Integer newsId;

    private String coverImg;

    private String isHot;

    private String newsDesc;

    private String newsPath;

    private String newsTitle;

    private Timestamp publishTime;

    private String state;

    private Integer type;

    private Timestamp updateTime;

    private String newsContent;
}