package com.note.cms.tools;
import com.note.cms.NConst;

import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 * Created by hasee on 2017/11/27.
 */
@ServerEndpoint("/websocket/{ipcid}/{lastsnapid}")
public class websocket {
    @OnOpen
    public void onOpen(Session session, @PathParam("ipcid")Integer ipcid,
                       @PathParam("lastsnapid")Integer lastsnapid){
        OneThread thread =null;
        thread=new OneThread(session,ipcid, lastsnapid);
        thread.start();
    }
}