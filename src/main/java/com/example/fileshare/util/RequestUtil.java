package com.example.fileshare.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;


/**
 * @author vague 5/5/2022 下午 2:03
 */
public class RequestUtil {


    private HttpServletRequest request;

    public RequestUtil(HttpServletRequest request) {
        this.request = request;
    }

    public String getParameters() {
        Enumeration<String> paraNames = this.request.getParameterNames();
        if (paraNames == null) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder();

            while(paraNames.hasMoreElements()) {
                String paraName = paraNames.nextElement();
                sb.append("&").append(paraName).append("=").append(this.request.getParameter(paraName));
            }

            return sb.toString();
        }
    }

    public String getHeader(String headerName) {
        return this.request.getHeader(headerName);
    }

    public String getReferer() {
        return this.getHeader("Referer");
    }

    public String getUa() {
        return this.getHeader("User-Agent");
    }

    public String getIp() {
        String ip = this.request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = this.request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = this.request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = this.request.getRemoteAddr();
        }

        return ip;
    }

    public String getRequestUrl() {
        return this.request.getRequestURL().toString();
    }

    public String getServletPath() {
        return this.request.getServletPath();
    }

    public String getMethod() {
        return this.request.getMethod();
    }
}

