package com.note.cms.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xuxinjian on 16/7/20.
 */
@Service
public class CalCostTimeInterceptor extends HandlerInterceptorAdapter {
    //此处一般继承HandlerInterceptorAdapter适配器即可
    private static final Logger logger = LoggerFactory.getLogger(CalCostTimeInterceptor.class);
    private List<String> allowUrls;
    private static AtomicInteger mConCount = new AtomicInteger(0);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        mConCount.getAndIncrement();
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        System.out.println("===========HandlerInterceptor1 postHandle");

        long startTime = (Long)request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long executeTime = endTime - startTime;
        String url = request.getRequestURI();
        int count = mConCount.getAndDecrement();
//        int count = mConCount.get();
        logger.info("req:" + request.getRequestURL());
        if(executeTime > 1000)
        {
            //如果请求超时，则记录日志
            logger.warn("apiconcount=" + count + "||apicosttime=" + executeTime + "|| " + url);
        }else{
            logger.info("apiconcount=" + count + "||apicosttime=" + executeTime + "|| " + url);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        System.out.println("===========HandlerInterceptor1 afterCompletion");
    }

}