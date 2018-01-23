package com.note.cms.filter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by xuxinjian on 16/7/18.
 */
public interface SessionFilter {
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain);
}
