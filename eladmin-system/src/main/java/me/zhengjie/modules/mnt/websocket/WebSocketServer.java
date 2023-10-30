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
package me.zhengjie.modules.mnt.websocket;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.utils.RedisUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
/**
 * @author ZhangHouYing
 * @date 2019-08-10 15:46
 */
@ServerEndpoint("/webSocket/{sid}/{roomId}")//ws://127.0.0.1:18000/webSocket/
@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketServer {

	private Session session;
	private String sid="";
	private String roomId="";
	private Set<String> rooms = new HashSet<>();
	//private final RedisTemplate<String,String> redisTemplate;
	private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();
	private static ConcurrentHashMap<String,Set<String>> roomPool = new ConcurrentHashMap<>();
	private static ConcurrentHashMap<String,Session> sessionPool = new ConcurrentHashMap<String,Session>();

	/**
	 * 连接建立成功调用的方法
	 * */
	@OnOpen
	public void onOpen(Session session, @PathParam("sid") String sid, @PathParam("roomId")String roomId) {

		//如果存在就先删除一个，防止重复推送消息
		for (WebSocketServer webSocket:webSocketSet) {
			if (webSocket.sid.equals(sid)) {
				log.info("已经存在一个sid："+sid+"的用户");
				webSocketSet.remove(webSocket);
			}
		}
		webSocketSet.add(this);
		sessionPool.put(sid, session);
		addUserToRoom(roomId,sid);
		log.info("sid："+sid+"的用户"+"加入了roomId:"+roomId);
		this.sid=sid;
		this.roomId=roomId;
		this.session = session;
		log.info("sid:" + sid+ " webSocket连接成功");
		//System.out.println(getUsersInRoom(this.roomId));
	}

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose() {
		webSocketSet.remove(this);
		sessionPool.remove(this.sid);
		removeUserFromRoom(this.roomId,this.sid);
		log.info("sid:" + sid+ " webSocket连接断开");
	}

	/**
	 * 收到客户端消息后调用的方法
	 * @param message 客户端发送过来的消息*/
	@OnMessage
	public void onMessage(String message, Session session) {
		log.info("收到来"+sid+"的信息:"+message);
	}

	@OnError
	public void onError(Session session, Throwable error) {
		log.error("发生错误");
		error.printStackTrace();
	}
	/**
	 * 实现服务器主动推送
	 */
	private void sendMessage(String message) throws IOException {
		this.session.getBasicRemote().sendText(message);
	}

    public  void rooms(){
		this.roomId = "1";
		log.info(this.roomId);
	}
	/**
	 * 群发自定义消息
	 * */
	public static void sendInfo(SocketMsg socketMsg,@PathParam("sid") String sid) throws IOException {
		String message = JSONObject.toJSONString(socketMsg);
		log.info("推送消息到"+sid+"，推送内容:"+message);
		for (WebSocketServer item : webSocketSet) {
			log.info("WebSocketServer不为空");
			try {
				//这里可以设定只推送给这个sid的，为null则全部推送
				item.sendMessage(message);
				/*
				if(sid==null) {
					item.sendMessage(message);
				}else if(item.sid.equals(sid)){
					item.sendMessage(message);
				}*/
			} catch (IOException ignored) { }
		}
	}

	public static void sendInfoToString (String message,String sid) throws IOException {
		log.info("调用sendInfoToString方法");
		for (WebSocketServer item : webSocketSet) {
			log.info("WebSocketServer不为空");
			try {
				//这里可以设定只推送给这个sid的，为null则全部推送
				item.sendMessage(message);
				log.info("推送消息到"+sid+"，推送内容:"+message);
				/*
				if(sid==null) {
					item.sendMessage(message);
				}else if(item.sid.equals(sid)){
					item.sendMessage(message);
				}*/
			} catch (IOException ignored) { }
		}
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		WebSocketServer that = (WebSocketServer) o;
		return Objects.equals(session, that.session) &&
				Objects.equals(sid, that.sid);
	}

	@Override
	public int hashCode() {
		return Objects.hash(session, sid);
	}

	// 此为广播消息
	public void sendAllMessage(String message) {
		log.info("【websocket消息】广播消息:"+message);
		for(WebSocketServer webSocket : webSocketSet) {
			try {
				if(webSocket.session.isOpen()) {
					webSocket.session.getAsyncRemote().sendText(message);
					//webSocket.session.getAsyncRemote().send();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void sendAllImage(byte[] fileData) {
		log.info("【websocket消息】广播图片");
		for(WebSocketServer webSocket : webSocketSet) {
			try {
				if(webSocket.session.isOpen()) {
					webSocket.session.getAsyncRemote().sendBinary(ByteBuffer.wrap(fileData));
					//webSocket.session.getAsyncRemote().send();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	// 此为单点消息
	public void sendOneMessage(String userId, String message) {
		Session session = sessionPool.get(userId);
		if (session != null&&session.isOpen()) {
			try {
				session.getAsyncRemote().sendText(message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 此为单点消息(多人)
	public void sendMoreMessage(Set<String> sid, String message) {
		for(String userId:sid) {
			Session session = sessionPool.get(userId);
			if (session != null&&session.isOpen()) {
				try {
					session.getBasicRemote().sendText(message);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void sendMoreImage(Set<String> sid, byte[] fileData) {
		for(String userId:sid) {
			Session session = sessionPool.get(userId);
			if (session != null&&session.isOpen()) {
				try {
					//log.info("服务器转发消息:"+message);
					session.getAsyncRemote().sendBinary(ByteBuffer.wrap(fileData));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void addUserToRoom(String roomId, String sid) {
		Set<String> sidSet = roomPool.computeIfAbsent(roomId, key -> new HashSet<>());
		sidSet.add(sid);
	}

	public void removeUserFromRoom(String roomId, String sid) {
		Set<String> sidSet = roomPool.get(roomId);
		if (sidSet != null) {
			sidSet.remove(sid);
			if (sidSet.isEmpty()) {
				roomPool.remove(roomId);
			}
		}
	}

	public Set<String> getUsersInRoom(String roomId) {
		return roomPool.getOrDefault(roomId, Collections.emptySet());
	}
}
