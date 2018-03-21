package com.note.cms.handler;

/**
 * Created by hasee on 2017/11/27.
 */

import java.util.*;

import com.alibaba.fastjson.JSON;
import com.note.cms.NConst;
import com.note.cms.data.vo.OutputSnapshotVo;
import com.note.cms.service.SnapshotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;


@Component
public class PictureWebSocketHandler implements WebSocketHandler {
    @Autowired
    private SnapshotService mSnapshotService;

    private static final Logger logger = LoggerFactory.getLogger(PictureWebSocketHandler.class);

    // 保存相应ipcid的sessionList
    private static final Map<Integer, HashSet<WebSocketSession>> ipcidSession = new HashMap<Integer, HashSet<WebSocketSession>>();
    //保存相应的ipcid的thread
    private static final Map<Integer, QueryThread> ipcidThread = new HashMap<Integer, QueryThread>();

    //切换摄像头接收到ipcid和websocketSession的处理
    public void receiveIpcid(Integer ipcid, WebSocketSession session) {
        //判断是ipcid是否存在ipcidSession中

        if (!ipcidSession.containsKey(ipcid)) {
            addIpcid(ipcid, session);
        } else {
            HashSet<WebSocketSession> sessionList = ipcidSession.get(ipcid);
            //   if(!sessionList.contains(session)){
            sessionList.add(session);
            ipcidSession.put(ipcid, sessionList);
            QueryThread queryThread = ipcidThread.get(ipcid);
            if (queryThread.isAlive()) {
                queryThread.setWebsocketSessionList(sessionList);
            } else {
                addIpcid(ipcid, session);
            }
            //     }
        }

    }

    //ipcid添加到ipcSession，启动queryThread
    private void addIpcid(Integer ipcid, WebSocketSession session) {
        HashSet<WebSocketSession> sessionArrayList = new HashSet<WebSocketSession>();
        sessionArrayList.add(session);
        ipcidSession.put(ipcid, sessionArrayList);
        QueryThread thread = new QueryThread(sessionArrayList, mSnapshotService, ipcid);
        thread.start();
        ipcidThread.put(ipcid, thread);
    }

    // 连接 就绪时
    @Override
    public void afterConnectionEstablished(WebSocketSession session)
            throws Exception {
        logger.info("connect websocket success.......");
        Map<String,Object> map = session.getAttributes();
        logger.info(map.toString());
//        logger.info("" + session.getHandshakeAttributes().get("ipcid"));
        Integer ipcid = (Integer) session.getAttributes().get("ipcId");
        List<OutputSnapshotVo> list=null;
        if(ipcid != null){
            //多个新建连接推数据的处理
            if (ipcidSession.containsKey(ipcid)){
                HashSet<WebSocketSession> sessionList = ipcidSession.get(ipcid);
                if(sessionList.size()>=1){
                    try {
                        list = mSnapshotService.getLastSnapshot(ipcid, 0, NConst.FORWARD_NEW, 5);
                    }catch(Exception e){
                        logger.error("error when get Snapshot "+e.getMessage());
                        e.printStackTrace();
                    }
                    if(list!=null&&list.size()>0){
                        TextMessage textMessage =new TextMessage(JSON.toJSONString(list),true);
                        session.sendMessage(textMessage);
                    }
                }
            }
            receiveIpcid(ipcid, session);
        }
    }


    // 处理信息
    @Override
    public void handleMessage(WebSocketSession session,
                              WebSocketMessage<?> message) throws Exception {
//        Integer ipcid = 0;
//        String flag = "";
//        logger.info("handleMessage......." + message.getPayload() + "...........");
//        JSONObject msg = JSON.parseObject(message.getPayload().toString());
//        // 处理消息 msgContent消息内容
//        if (!msg.getString("flag").equals("") && msg.getString("flag") != null) {
//            flag = msg.getString("flag");
//        }
//        if (!msg.getString("ipcid").equals("") && msg.getString("ipcid") != null) {
//            ipcid = Integer.valueOf(msg.getString("ipcid"));
//        }
//        if (flag.equals("open")) {
//            List<OutputSnapshotVo> list = null;
//            if (ipcid != null) {
//                //多个新建连接推数据的处理
//                if (ipcidSession.containsKey(ipcid)) {
////                    QueryThread queryThread = ipcidThread.get(ipcid);
//                    List<WebSocketSession> sessionList = ipcidSession.get(ipcid);
//                    if (sessionList.size() >= 1) {
//                        try {
//                            list = mSnapshotService.getLastSnapshot(ipcid, 0, NConst.FORWARD_NEW, 5);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        if (list != null && list.size() > 0) {
//                            TextMessage textMessage = new TextMessage(JSON.toJSONString(list), true);
//                            session.sendMessage(textMessage);
//                        }
//                    }
//                }
//                receiveIpcid(ipcid, session);
//            }
//        } else if (flag.equals("close")) {
//            List<WebSocketSession> sessionList = ipcidSession.get(ipcid);
//            if(sessionList !=null){
//                sessionList.remove(session);
//                ipcidSession.put(ipcid, sessionList);
//                if (sessionList.size() == 0) {
//                    ipcidSession.remove(ipcid);
//                    QueryThread queryThread = ipcidThread.get(ipcid);
//                    queryThread.exit = true;
//                    ipcidThread.remove(ipcid);
//                } else {
//                    ipcidSession.put(ipcid, sessionList);
//                    QueryThread queryThread = ipcidThread.get(ipcid);
//                    queryThread.setWebsocketSessionList(sessionList);
//                }
//            }

//        }

    }

    // 处理传输时异常
    @Override
    public void handleTransportError(WebSocketSession session,
                                     Throwable exception) throws Exception {
        logger.info("error");
    }

    // 关闭 连接时
    @Override
    public void afterConnectionClosed(WebSocketSession session,
                                      CloseStatus closeStatus) throws Exception {

        logger.info("connect websocket closed.......");

        int ipcid = 0;
        for (Integer key : ipcidSession.keySet()) {
            HashSet<WebSocketSession> sessions = ipcidSession.get(key);
            if (sessions.contains(session)) {
                ipcid = key;
            }
        }

        HashSet<WebSocketSession> sessionList = ipcidSession.get(ipcid);
        if (sessionList != null) {
//            synchronized (sessionList) {
            if (sessionList.contains(session)) {
                sessionList.remove(session);
//                ipcidSession.put(ipcid, sessionList);
            }

            if (sessionList.size() == 0) {
                ipcidSession.remove(ipcid);
                QueryThread queryThread = ipcidThread.get(ipcid);
                queryThread.exit = true;
                ipcidThread.remove(ipcid);
            } else {
                ipcidSession.put(ipcid, sessionList);
                QueryThread queryThread = ipcidThread.get(ipcid);
                queryThread.setWebsocketSessionList(sessionList);
            }
//            }
        }
//        synchronized(this){
//
//        }


//        logger.info("" + session.getHandshakeAttributes().get("ipcid"));
//        Integer ipcid = (Integer) session.getHandshakeAttributes().get("ipcid");
//        List<WebSocketSession> sessionList = ipcidSession.get(ipcid);
//        sessionList.remove(session);
//        ipcidSession.put(ipcid, sessionList);
//        if (sessionList.size() == 0) {
//            ipcidSession.remove(ipcid);
//            QueryThread queryThread = ipcidThread.get(ipcid);
//            queryThread.exit = true;
//            ipcidThread.remove(ipcid);
//        } else {
//            ipcidSession.put(ipcid, sessionList);
//            QueryThread queryThread = ipcidThread.get(ipcid);
//            queryThread.setWebsocketSessionList(sessionList);
//        }
    }


    @Override
    public boolean supportsPartialMessages() {
        return false;
    }


}