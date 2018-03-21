package com.note.cms.handler;

import com.alibaba.fastjson.JSON;
import com.note.cms.NConst;
import com.note.cms.data.vo.OutputSnapshotVo;
import com.note.cms.service.SnapshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by hasee on 2017/11/28.
 */
@Component
public class QueryThread extends Thread {
//    private int currentIndex;
    private Integer ipcid;
    private Integer lastsnapid;
    private SnapshotService mSnapshotService;
    public volatile Boolean exit =false ;
//    private List<OutputSnapshotVo> resultList;
    private HashSet<WebSocketSession> webSocketSessionList;
    public QueryThread(HashSet<WebSocketSession> sessionList,SnapshotService mSnapshotService,
                       Integer ipcid){
        this.webSocketSessionList = sessionList;
        this.ipcid = ipcid;
        this.lastsnapid =0;
        this.mSnapshotService = mSnapshotService;
    }
    public QueryThread(){

    }

    public void setWebsocketSessionList(HashSet<WebSocketSession> sessionList){
        this.webSocketSessionList = sessionList;
    }

    public void run(){
        while (!exit){
            List<OutputSnapshotVo> list=null;
            try {
                list = mSnapshotService.getLastSnapshot(ipcid, lastsnapid, NConst.FORWARD_NEW, 5);
                if(list!=null&&list.size()>0){
                    this.lastsnapid =list.get(0).getSnapshotId();
                }
            }catch(Exception e){
                e.printStackTrace();
            }

            if(list!=null&&list.size()>0){
                TextMessage textMessage =new TextMessage(JSON.toJSONString(list),true);
                try {
//                    HashSet<WebSocketSession> newWebSocketSessionList =new ArrayList(new HashSet(webSocketSessionList));
                    for (WebSocketSession session :webSocketSessionList){
                        session.sendMessage(textMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
