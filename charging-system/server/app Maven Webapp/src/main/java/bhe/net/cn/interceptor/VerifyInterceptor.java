package bhe.net.cn.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.util.StringUtils;

import bhe.net.cn.auth.SessionKey;
import bhe.net.cn.auth.SessionUtils;
import bhe.net.cn.base.MapCache;
import bhe.net.cn.base.ResponseTemplate;
import bhe.net.cn.dict.Constant;
import bhe.net.cn.dict.Note;
import bhe.net.cn.utils.JacksonUtils;

public class VerifyInterceptor implements HandlerInterceptor {

    static final Logger LOGGER = LoggerFactory.getLogger(VerifyInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean proceed = true;
        ResponseTemplate rp_t = new ResponseTemplate();
        String url = request.getServletPath().replaceAll("\\/+", "/");
        LOGGER.info(url);
        if (!url.equals("/")) {
            if (StringUtils.isEmpty(MapCache.verify_url.get(url))) {
                rp_t.setSc(Note.SC_BADREQUEST);
                rp_t.setMsg(Note.MSG_BADREQUEST);
                proceed = false;
            } else if (Constant.TRUE.equals(MapCache.verify_url.get(url))) {
                Map<String, Object> user = SessionUtils.get(request, SessionKey.USER);
                if (user == null) {
                    rp_t.setSc(Note.SC_UNAUTHORIZED);
                    rp_t.setMsg(Note.MSG_UNAUTHORIZED);
                    proceed = false;
                }
            }
            if (!proceed) {
                response.setContentType(Constant.RESPONSE_CONTENT_TYPE);
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(JacksonUtils.objToJsonStr(rp_t));
            }
        }
        return proceed;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

}
