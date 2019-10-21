package charserver.cn.net.bhe.manage.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import charserver.cn.net.bhe.common.base.Rt;
import charserver.cn.net.bhe.common.exception.BusinessException;
import charserver.cn.net.bhe.common.exception.ExpException;

@RestController
public class ExceptionController implements ErrorController {

    static final Logger LOGGER = LoggerFactory.getLogger(ExceptionController.class);

    private ErrorAttributes errorAttributes;

    @Autowired
    public ExceptionController(ErrorAttributes errorAttributes) {
        Assert.notNull(errorAttributes, "ErrorAttributes must not be null");
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping(value = "/error")
    @ResponseBody
    public Rt error(HttpServletRequest request) {
        Throwable e = getException(request);

        e = e == null ? new RuntimeException("unknown error") : e;

        Rt rt = Rt.ins();
        rt.setMsg(e.getLocalizedMessage());

        if (e instanceof BusinessException) {
            rt.setSc(Rt.SC_ERR_BS);
        } else if (e instanceof ExpException) {
            rt.setSc(Rt.SC_ERR_EXP);
        } else {
            rt.setSc(Rt.SC_ERR_SYS);
            LOGGER.error("", e);
        }
        return rt;
    }

    private Throwable getException(HttpServletRequest request) {
        return this.errorAttributes.getError(new ServletWebRequest(request));
    }

    @Override
    public String getErrorPath() {
        return "";
    }

}
