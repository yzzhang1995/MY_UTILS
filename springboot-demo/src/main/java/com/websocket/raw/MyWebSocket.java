package com.websocket.raw;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * 原生websocket，可以使用http://www.websocket-test.com/在线测试websocket
 * <p>
 * 注意：由于本工程包含众多springbooot项目，所以启动可能会有冲突，可单独在一个工程中启动。
 * 原生websocket包含：
 * （1）MyWebSocket类
 * （2）pom.xml文件中的javaee-api 和 tomcat7-maven-plugin插件（可在maven-Plugins执行）
 * （3）打包类型为war包
 *
 * @author yzzhang
 * @date 2020/9/15 22:00
 */
/*
声明这是一个websocket服务,需要指定访问该服务的地址，
在地址中可以指定参数，需要通过{}进行占位
 */
@ServerEndpoint("/websocket/{uid}")
public class MyWebSocket {
    /**
     * 该方法将在建立连接后执行，会传入session对象，就是客户端与服务端建立的长连接通道
     * 通过@PathParam获取url申明中的参数
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "uid") String uid) throws IOException {
        System.out.println("websocket已经连接" + session);
        // 给客户端响应，欢迎登陆（连接）系统
        session.getBasicRemote().sendText(uid + "，你好！欢迎登陆系统");
    }

    /**
     * 该方法是在连接关闭后执行
     */
    @OnClose
    public void onClose(Session session) {
        System.out.println("websocket已经关闭" + session);
    }

    /**
     * 该方法用于接收客户端发来的消息
     *
     * @param message 发来的消息数据
     * @param session 会话对象（也是通道）
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        System.out.println("收到客户端发来的消息 --> " + message);

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //给客户端一个反馈
        session.getBasicRemote().sendText("消息已收到");
    }

}
