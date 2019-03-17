package com.serajoon.dalaran.support.websocket;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint(value = "/websocketExample/{userId}")
@Component
@Slf4j
public class MyWebSocket {

    //用来记录当前在线连接数
    private static AtomicInteger onlineCount = new AtomicInteger();
    private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<>();
    //<UserId,Session>
    private static ConcurrentHashMap<String, Session> webSocketMap = new ConcurrentHashMap<>();
    private Session session;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        webSocketMap.put(userId, session);
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("userId") String userId) {
        webSocketMap.remove(userId);
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        //群发消息
        for (MyWebSocket item : webSocketSet) {
            item.sendMessage(message);
        }
    }

    /**
     * 错误时
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }


    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 群发消息给所有人
     */
    public static void sendToAll(String message) {
        for (MyWebSocket item : webSocketSet) {
            item.sendMessage(message);
        }
    }

    /**
     * 发送消息给指定用户
     *
     * @return
     */
    public void sendMessageByUserId(String userId, String message) {
        Optional.ofNullable(webSocketMap.get(userId)).ifPresent(session -> {
            if(session.isOpen()){
                try {
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static int getOnlineCount() {
        return onlineCount.get();
    }

    private void addOnlineCount() {
        onlineCount.getAndIncrement();
    }

    private void subOnlineCount() {
        onlineCount.getAndDecrement();
    }
}