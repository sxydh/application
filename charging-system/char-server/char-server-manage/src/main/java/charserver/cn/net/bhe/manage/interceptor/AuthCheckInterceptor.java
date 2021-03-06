package charserver.cn.net.bhe.manage.interceptor;

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

import charserver.cn.net.bhe.common.base.Rt;
import charserver.cn.net.bhe.manage.base.SessionUtils;
import charserver.cn.net.bhe.manage.dict.Prop;
import cn.net.bhe.utils.main.JacksonUtils;
import cn.net.bhe.utils.main.ServletUtils;

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
