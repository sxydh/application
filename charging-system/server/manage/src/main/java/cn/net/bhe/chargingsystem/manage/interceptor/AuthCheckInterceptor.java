package cn.net.bhe.chargingsystem.manage.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.net.bhe.chargingsystem.common.base.Rt;
import cn.net.bhe.chargingsystem.manage.base.SessionUtils;
import cn.net.bhe.chargingsystem.manage.dict.Prop;
import cn.net.bhe.utils.main.JacksonUtils;

@Component
public class AuthCheckInterceptor implements HandlerInterceptor {

    static final Logger LOGGER = LoggerFactory.getLogger(AuthCheckInterceptor.class);

    @Autowired
    private SessionUtils session;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (Prop.needCheck(request.getRequestURI())) {
            Map<String, Object> user = session.getCurUser();
            if (user == null) {
                Rt rt = Rt.suc();
                rt.setSc(Rt.SC_ERR_EXP);
                rt.setMsg("");

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
