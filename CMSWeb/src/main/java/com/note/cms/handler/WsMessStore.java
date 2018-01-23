package com.note.cms.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;

public class WsMessStore {
    private static final Logger logger = LoggerFactory.getLogger(WsMessStore.class);

    private static Map<String, String> message = new HashMap<>();
    private Thread pushMessageThread;
    private static Map<String, List<WebSocketSession>> macsession = new HashMap<>();

    private static volatile Map<WebSocketSession, String> sessionAndMac = new HashMap<>();

    private static WsMessStore instance;

    public static WsMessStore getInstance() {    //对获取实例的方法进行同步
        if (instance == null) {
            synchronized (WsMessStore.class) {
                if (instance == null)
                    instance = new WsMessStore();
            }
        }
        return instance;
    }

//    public static String getPalyingCameraMac() {
//        return palyingCameraMac;
//    }
//
//    public static void setPalyingCameraMac(String palyingCameraMac) {
//        WsMessStore.palyingCameraMac = palyingCameraMac;
//    }

    public static void setMacsession(Map<String, List<WebSocketSession>> ms) {
        macsession = ms;
    }

    public static Map<String, List<WebSocketSession>> getMacsession() {
        return macsession;
    }

    public void addMessage(String data, String mac) {

        synchronized (this) {
            message.put(mac, data);
//            logger.info("添加一条数据成功，唤醒推送线程");
            this.notifyAll();
        }

    }

    public void startPushMessThread() {
        if (null != pushMessageThread && pushMessageThread.isAlive()) {
            logger.info("推送线程活跃中。。。");
            return;
        }
        pushMessageThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        pushMessage();
                    } catch (Exception e) {
//                        logger.info("=================");
                        e.printStackTrace();
                    }
                }
            }
        });

        pushMessageThread.start();

//        logger.info("推送线程启动，开始推送数据");
    }

    public void pushMessage() {

        try {
            synchronized (this) {
//            synchronized (this) {
                while (message.size() == 0) {
                    try {
//                        logger.info("没有数据推送，等待中。。。");
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Iterator<Map.Entry<String, String>> iterator = message.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> entry = iterator.next();
                    String key = entry.getKey();
                    if (macsession.containsKey(key)) {
                        List<WebSocketSession> sessionList = macsession.get(key);
                        String value = entry.getValue();
//                        logger.info(key + " 数据长度：" + value.length());
                        for (WebSocketSession session : sessionList) {
                            if (session.isOpen()) {
                                session.sendMessage(new TextMessage(value));
                            }
                        }
                    }
                }
                message.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
