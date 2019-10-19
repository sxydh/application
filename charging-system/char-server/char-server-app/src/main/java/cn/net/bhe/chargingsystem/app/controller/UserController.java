package cn.net.bhe.chargingsystem.app.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.net.bhe.chargingsystem.app.cache.SessionUtils;
import cn.net.bhe.chargingsystem.app.service.UserService;
import cn.net.bhe.chargingsystem.common.base.Rt;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = { RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Object login(HttpServletRequest request, @RequestBody Map<String, Object> rq_u) {
        Rt rt = Rt.suc();
        rt.setData(userService.login(request, rq_u));
        return rt;
    }

    @RequestMapping(value = "/node/list", method = { RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Object nodeList(HttpServletRequest request) {
        Rt rt = Rt.suc();
        rt.setData(userService.nodeList());
        return rt;
    }

    @RequestMapping(value = "/dycode/get", method = { RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Object dycodeSend(HttpServletRequest request, @RequestParam String phone) throws Exception {
        Rt rt = Rt.suc();
        userService.dycodeSend(phone);
        return rt;
    }

    @RequestMapping(value = "/password/reset", method = { RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Object pwdReset(HttpServletRequest request, @RequestBody Map<String, Object> rq_u) throws Exception {
        Rt rt = Rt.suc();
        userService.pwdReset(request, rq_u);
        return rt;
    }

    @RequestMapping(value = "/register", method = { RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Object register(HttpServletRequest request, @RequestBody Map<String, Object> rq_u) {
        throw new RuntimeException("Registration is not currently available");
    }

    @RequestMapping(value = "/logout", method = { RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Object logout(HttpServletRequest request) {
        Rt rt = Rt.suc();
        SessionUtils.invalidate(request);
        return rt;
    }

    @RequestMapping(value = "/update", method = { RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Object update(HttpServletRequest request, @RequestBody Map<String, Object> rq_u) {
        Rt rt = Rt.suc();
        userService.update(request, rq_u);
        return rt;
    }

    @RequestMapping(value = "/auth", method = { RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Object auth(HttpServletRequest request) {
        Rt rt = Rt.suc();
        return rt;
    }

}
