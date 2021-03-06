package charserver.cn.net.bhe.manage.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import charserver.cn.net.bhe.common.base.Rt;
import charserver.cn.net.bhe.common.dict.Const;
import charserver.cn.net.bhe.common.exception.BusinessException;
import charserver.cn.net.bhe.common.exception.ExpException;

@RestController
@ControllerAdvice
public class ExceptionController {

    static final Logger LOGGER = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler(value = { Exception.class })
    @ResponseBody
    public Rt error(HttpServletRequest request, Exception e) {
        e = e == null ? new RuntimeException("unknown error") : e;

        Rt rt = Rt.ins();
        rt.setMsg(e.getLocalizedMessage());

        if (e instanceof BusinessException) {
            rt.setSc(Rt.SC_ERR_BS);
        } else if (e instanceof ExpException) {
            rt.setSc(Rt.SC_ERR_EXP);
        } else {
            rt.setSc(Rt.SC_ERR_SYS);
            LOGGER.error(Const.LOGGER_TAG_ERR_SERVER, e);
        }
        return rt;
    }

}
