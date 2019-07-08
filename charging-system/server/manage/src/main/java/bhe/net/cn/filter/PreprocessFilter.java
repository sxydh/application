package bhe.net.cn.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import bhe.net.cn.servlet.RequestWrapper;

public class PreprocessFilter implements Filter {

    static final Logger LOGGER = LoggerFactory.getLogger(PreprocessFilter.class);

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        RequestWrapper requestWrapper = new RequestWrapper((HttpServletRequest) request);
        log(requestWrapper);
        chain.doFilter(requestWrapper, response);
    }

    private void log(HttpServletRequest request) throws IOException {
        String params = paramsGet(request);
        LOGGER.info(params);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub

    }

    public static String getClientIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private String paramsGet(HttpServletRequest request) throws IOException {
        String content = "\r\n";
        int format = 20;
        content += format("method:", format) + request.getMethod() + "\r\n";
        content += format("url:", format) + request.getRequestURL() + "\r\n";
        content += format("remote address:", format) + getClientIpAddr(request) + "\r\n";
        content += format("request data:", format) + StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8) + "\r\n";
        content += "\r\n";
        return content;
    }

    private String format(String content, int length) {
        if (content.length() < length) {
            int dif = content.length();
            for (int i = 0; i < length - dif; i++) {
                content += " ";
            }
        }
        return content;
    }

}
