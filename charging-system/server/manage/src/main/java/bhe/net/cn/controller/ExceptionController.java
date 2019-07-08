package bhe.net.cn.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bhe.net.cn.base.ResponseTemplate;

@RestController
public class ExceptionController implements ErrorController {

    @RequestMapping(value = "/error")
    public ResponseTemplate error(HttpServletRequest request) {
        return null;
    }

    @Override
    public String getErrorPath() {
        // TODO Auto-generated method stub
        return null;
    }

}
