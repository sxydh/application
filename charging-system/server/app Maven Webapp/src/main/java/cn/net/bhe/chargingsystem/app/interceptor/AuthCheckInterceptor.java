package cn.net.bhe.chargingsystem.app.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.net.bhe.chargingsystem.app.cache.SessionUtils;
import cn.net.bhe.chargingsystem.app.dict.Prop;
import cn.net.bhe.chargingsystem.common.base.Rt;
import cn.net.bhe.chargingsystem.common.dict.K;
import cn.net.bhe.utils.main.JacksonUtils;
import cn.net.bhe.utils.main.ServletUtils;

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
                ServletUtils.print(response, JacksonUtils.objToJsonStr(rt));

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
