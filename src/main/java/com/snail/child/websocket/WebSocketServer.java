package com.example.huigu.websocket;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint(value = "/websocket/{uid}")
public class WebSocketServer {

    static Logger log= LoggerFactory.getLogger(WebSocketServer.class);

    private static Integer onlineCount = 0;

    private static CopyOnWriteArraySet<WebSocketServer> webSocketServerSet = new CopyOnWriteArraySet<>();

    private Session session ;

    private String uid;


    /**
     * 记录，所有长连接的对象，和对象的基础属性
     * @param session
     * @param uid
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("uid") String uid) {
        log.info("连接开启");
        this.session = session;
//        log.info("Session的结构："+new Gson().toJson(session));
        webSocketServerSet.add(this);     //加入set中  记录所有长连接的对象
//        log.info("WebSocket的结构："+new Gson().toJson(this));
        addOnlineCount();           //在线数加1
        log.info("有新窗口开始监听:"+uid+",当前在线人数为" + getOnlineCount());
        this.uid=uid;
        try {
            sendMessage("连接成功"+uid);
        } catch (IOException e) {
            log.error("websocket IO异常");
        }
    }

    /**
     * 拿到WebSocketServer对应的对象  给自己发送消息
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        //主动发送消息给自己
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 当前对象关闭后，移除缓存堆栈中的数据对象，并减少连接数
     */
    @OnClose
    public void onClose() {
        webSocketServerSet.remove(this);
        subOnlineCount();           //在线数减1
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount()+"离线用户ID"+this.uid);
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.info("发生错误");
        error.printStackTrace();
    }

    /**
     * 根据项目要求设计收到消息后的逻辑
     * @param uid
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(@PathParam("uid")String uid, String message, Session session) throws IOException {
        log.info("来自客户端"+uid+"的消息:" + message);

        for (WebSocketServer webSocketServer : webSocketServerSet) {
                webSocketServer.sendMessage("用户："+uid+"   消息内容："+message);
        }

    }

    /**
     * 指定消息发送对象的ID并发送消息
     * @param uid
     * @param msg
     * @throws IOException
     */
    public static void sendMsgByUid(String uid ,String msg) throws IOException {
        for (WebSocketServer webSocketServer : webSocketServerSet) {
            if(webSocketServer.uid.equals(uid)){
                webSocketServer.sendMessage("消息内容："+msg);
            }
        }

    }


    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}
