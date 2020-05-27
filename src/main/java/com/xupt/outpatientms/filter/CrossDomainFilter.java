package com.xupt.outpatientms.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Order(1)
@WebFilter(filterName = "CrossDomainFilter", urlPatterns = "/*")
public class CrossDomainFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(CrossDomainFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.info("enter CrossDomainFilter");
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        httpServletResponse.addHeader("Access-Control-Allow-Origin","*");
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        /* 允许跨域的请求方法GET, POST, HEAD 等 */
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "*");
        /* 重新预检验跨域的缓存时间 (s) */
        httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
        /* 允许跨域的请求头 */
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "*");
        /* 是否携带cookie */
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
