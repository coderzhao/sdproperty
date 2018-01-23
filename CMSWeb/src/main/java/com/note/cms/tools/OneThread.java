package com.note.cms.tools;

import com.note.cms.NConst;
import com.note.cms.data.vo.OutputSnapshotVo;
import com.note.cms.service.SnapshotService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hasee on 2017/11/27.
 */
public class OneThread extends Thread {
    @Autowired
    private SnapshotService mSnapshotService;
    private Session session;
    private List<OutputSnapshotVo> outputSnapshotVoList;
    private int currentIndex;
    private Integer ipcid;
    private Integer lastsnapid;


    public OneThread(Session session,Integer ipcid, Integer lastsnapid) {
        this.session = session;
        this.ipcid = ipcid;
        this.lastsnapid = lastsnapid;
        outputSnapshotVoList = new ArrayList<OutputSnapshotVo>();
        currentIndex = 0;//此时是0条
    }
    @Override
    public void run() {
        while (true) {
            List<OutputSnapshotVo> list = null;
            try {
                list = mSnapshotService.getLastSnapshot(ipcid, lastsnapid, NConst.FORWARD_NEW,5);
                session.getBasicRemote().sendObject(list); //No encoder specified for object of class [class AlarmMessage]
            } catch (Exception e) {
                e.printStackTrace();
            }
//            if (list != null && currentIndex < list.size()) {
//                for (int i = currentIndex; i < list.size(); i++) {
//                    try {
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (EncodeException e) {
//                        e.printStackTrace();
//                    }
//                }
//                currentIndex = list.size();
//            }
            try {
                //一秒刷新一次
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
