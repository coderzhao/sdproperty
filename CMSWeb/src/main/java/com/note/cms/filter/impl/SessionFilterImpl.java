package com.note.cms.filter.impl;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.note.cms.filter.SessionFilter;


/**
 * 登录过滤器
 * <功能详细描述>
 *
 */
public class SessionFilterImpl implements SessionFilter
{

    /** 登录验证过滤器 */

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    {
        // 不过滤的uri
        String[] notFilter =
                new String[] {"/images", "/js", "/css", "/login/tologin", "/login/mainframe", "/user/exist",
                        "/user/checkPassword", "/signcode"};

        // 请求的uri
        String uri = request.getRequestURI();
        // 是否过滤
        boolean doFilter = true;
        for (String s : notFilter)
        {
            if (uri.indexOf(s) != -1)
            {
                // 如果uri中包含不过滤的uri，则不进行过滤
                doFilter = false;
                break;
            }
        }

//        if (doFilter)
//        {
//            // 执行过滤
//            // 从session中获取登录者实体
//            TbUser obj = (TbUser)request.getSession().getAttribute("tbuser");
//            if (null == obj)
//            {
//                boolean isAjaxRequest = isAjaxRequest(request);
//                if (isAjaxRequest)
//                {
//                    response.setCharacterEncoding("UTF-8");
//                    //response.sendError(HttpStatus.UNAUTHORIZED.value(), "您已经太长时间没有操作,请刷新页面");
//                    return ;
//                }
//                try {
//                    response.sendRedirect("relogin");
//                }catch (Exception e){
//
//                }
//                return;
//            }
//            else
//            {
//                // 如果session中存在登录者实体，则继续
//                //filterChain.doFilter(request, response);
//                doFilter(request, response, filterChain);
//            }
//        }
//        else
//        {
//            // 如果不执行过滤，则继续
//            //filterChain.doFilter(request, response);
//            doFilter(request, response, filterChain);
//        }
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain){
        try{
            filterChain.doFilter(request, response);
        }catch (Exception e){

        }
    }

    /** 判断是否为Ajax请求
     * <功能详细描述>
     * @param request
     * @return 是true, 否false
     * @see [类、类#方法、类#成员]
     */
    public static boolean isAjaxRequest(HttpServletRequest request)
    {
        String header = request.getHeader("X-Requested-With");
        if (header != null && "XMLHttpRequest".equals(header))
            return true;
        else
            return false;
    }

}
