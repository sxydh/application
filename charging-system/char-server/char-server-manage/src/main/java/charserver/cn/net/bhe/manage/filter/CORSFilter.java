package charserver.cn.net.bhe.manage.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CORSFilter implements Filter {

    static final Logger LOGGER = LoggerFactory.getLogger(CORSFilter.class);

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        int format = 20;
        String log = "";
        //
        Enumeration<String> headers = servletRequest.getHeaderNames();
        log += format("\r\n" + "headers:", format) + "\r\n";
        while (headers.hasMoreElements()) {
            String header = headers.nextElement();
            String value = servletRequest.getHeader(header);
            log += format("", format) + header + ": " + value + "\r\n";
        }
        //
        LOGGER.info(log);
        servletResponse.setHeader("Access-Control-Allow-Origin", servletRequest.getHeader("origin"));
        servletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        servletResponse.setHeader("Access-Control-Allow-Headers", servletRequest.getHeader("access-control-request-headers"));
        servletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub
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
