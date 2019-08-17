package bhe.net.cn.filter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

import bhe.net.cn.dict.Const;
import bhe.net.cn.servlet.RequestWrapper;
import bhe.net.cn.servlet.ResponseWrapper;
import bhe.net.cn.utils.JacksonUtils;

@Component
public class GateFilter implements Filter {

    static final Logger LOGGER = LoggerFactory.getLogger(GateFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        RequestWrapper requestWrapper = new RequestWrapper((HttpServletRequest) request);
        ResponseWrapper responseWrapper = new ResponseWrapper((HttpServletResponse) response);

        long start = System.currentTimeMillis();
        String uri = requestWrapper.getRequestURI();
        String content = ""
                //
                + "start at: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date())
                //
                + "\r\nuri: " + uri
                //
                + "\r\nrequest body:\r\n" + JacksonUtils.pretty(requestWrapper.getBody());
        LOGGER.info(Const.LOGGER_FORMAT_INFO, content);

        try {
            chain.doFilter(requestWrapper, responseWrapper);
        } catch (IOException | ServletException e) {
            LOGGER.error(Const.LOGGER_TAG_ERR_SERVER, e);
        } finally {
            long end = System.currentTimeMillis();
            long spend = end - start;
            content = ""
                    //
                    + "end at: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date())
                    //
                    + "\r\nuri: " + uri
                    //
                    + "\r\nspend: " + spend
                    //
                    + "\r\nresponse body:\r\n" + JacksonUtils.pretty(new String(responseWrapper.getCopy()));
            LOGGER.info(Const.LOGGER_FORMAT_INFO, content);
        }
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub

    }

}
