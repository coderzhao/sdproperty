package com.note.cms.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by xuxinjian on 16/7/20.
 */
public class SessionTimeoutInterceptor extends HandlerInterceptorAdapter {
    //此处一般继承HandlerInterceptorAdapter适配器即可
    private static final Logger logger = LoggerFactory.getLogger(SessionTimeoutInterceptor.class);
    private List<String> allowUrls;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String requestUrl = request.getRequestURI();

//        for(String url : allowUrls) {
//            int index = requestUrl.indexOf(url);
//            if(index >=0) {
//                return true;
//            }
//        }
//        //logger.info("===========HandlerInterceptor1 preHandle");
//        String username= (String) WebUtils.getSessionAttribute(request, "username");
//
//        String requestType = request.getHeader("X-Requested-With");//识别ajax的响应头
//        if(username==null){
//            if (requestType != null && requestType.equals("XMLHttpRequest")) {//如果是ajax类型，响应logout给前台
//                response.setStatus(499);
//                response.addHeader("sessionstatus", "logout");
//                response.getWriter().print("logout");
//            }else{
//                response.sendRedirect("./index");//首页居多
//            }
//            //logger.info("preHandle: 888" );
//            return false;//终止后面的拦截器的执行
//        }else{
//            //logger.info("preHandle: 999" );
//            return true;//让下一个拦截器去处理
//        }
        return true;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //logger.info("===========HandlerInterceptor1 postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //logger.info("===========HandlerInterceptor1 afterCompletion");
    }

    public List<String> getAllowUrls() {
        return allowUrls;
    }

    public void setAllowUrls(List<String> allowUrls) {
        this.allowUrls = allowUrls;
    }
}