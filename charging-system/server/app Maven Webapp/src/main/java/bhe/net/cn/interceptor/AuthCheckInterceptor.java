package bhe.net.cn.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import bhe.net.cn.base.Rt;
import bhe.net.cn.cache.SessionUtils;
import bhe.net.cn.dict.K;
import bhe.net.cn.dict.Prop;
import bhe.net.cn.utils.JacksonUtils;

public class AuthCheckInterceptor implements HandlerInterceptor {

    static final Logger LOGGER = LoggerFactory.getLogger(AuthCheckInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (Prop.needCheck(request.getServletPath())) {
            Map<String, Object> user = SessionUtils.get(request, K.S_USER);
            if (user == null) {
                Rt rt = new Rt(Rt.SC_ERR_EXP);

                response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(JacksonUtils.objToJsonStr(rt));

                return false;
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

}
