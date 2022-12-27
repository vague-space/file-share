package com.example.fileshare.filter;

import com.example.fileshare.common.SessionManager;
import com.example.fileshare.util.RequestUtil;
import com.example.fileshare.vo.OnlineUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

/**
 * @author vague 5/5/2022 下午 2:01
 */
@Slf4j
@Component
@WebFilter(urlPatterns = {"/**"}, filterName = "tokenAuthorFilter")
public class ServerFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("TokenFilter init {}", filterConfig.getFilterName());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        String ip = new RequestUtil(httpRequest).getIp();
        SessionManager.add(ip, new OnlineUser(ip, new Date()));
        // 到下一个链
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        log.info("TokenFilter destroy");
    }


}
