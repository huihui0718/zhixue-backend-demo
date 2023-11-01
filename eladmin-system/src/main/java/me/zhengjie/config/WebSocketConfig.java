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
package me.zhengjie.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author ZhangHouYing
 * @date 2019-08-24 15:44
 */

@Configuration
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

//	@Override
//	public void registerStompEndpoints(StompEndpointRegistry registry) {
//		/*注册STOMP协议的节点(endpoint),并映射指定的url,
//		 * 添加一个访问端点“/endpointMark”,客户端打开双通道时需要的url,
//		 * 允许所有的域名跨域访问，指定使用SockJS协议。*/
//		registry.addEndpoint("/ws")
//				.setAllowedOrigins("*")
//				.withSockJS();
//	}
//
//	@Override
//	public void configureMessageBroker(MessageBrokerRegistry registry) {
//		/*配置一个消息代理
//		 * mass 负责群聊
//		 * queue 单聊*/
//		registry.enableSimpleBroker(
//				"/mass","/queue");
//
//		//一对一的用户，请求发到/queue
//		registry.setUserDestinationPrefix("/queue");
//	}

	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}

}

