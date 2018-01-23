package com.note.cms.handler;

/**
 * Created by hasee on 2017/11/27.
 */
import java.util.Map;

import com.google.common.base.Strings;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;



public class HandshakeInterceptor extends HttpSessionHandshakeInterceptor{


    // 握手前
    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
//        String ipcidstr = ((ServletServerHttpRequest) request).getServletRequest().getParameter("ipcid");
//        if (Strings.isNullOrEmpty(ipcidstr)) {
//            return false;
//        } else {
//            Integer ipcid = Integer.valueOf(ipcidstr);
//            attributes.put("ipcid", ipcid);
//        }
        System.out.println("++++++++++++++++ HandshakeInterceptor: beforeHandshake  ++++++++++++++"+attributes);
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }



    // 握手后
    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception ex) {


        System.out.println("++++++++++++++++ HandshakeInterceptor: afterHandshake  ++++++++++++++");
        super.afterHandshake(request, response, wsHandler, ex);
    }

}