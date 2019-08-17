package bhe.net.cn.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bhe.net.cn.base.RedisTemplateUtils;
import bhe.net.cn.base.Rt;
import bhe.net.cn.base.SessionUtils;
import bhe.net.cn.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired
    RedisTemplateUtils redis;

    @Autowired
    private UserService userService;

    @Autowired
    SessionUtils session;

    @RequestMapping(value = "/sub/add", method = { RequestMethod.POST })
    public Rt subAdd(@RequestBody Map<String, Object> rq_u) {
        Rt rt = Rt.suc();
        userService.subAdd(rq_u);
        return rt;
    }

    @RequestMapping(value = "/node/list", method = { RequestMethod.GET })
    public Rt nodeList() {
        Rt rt = Rt.suc();
        rt.setData(userService.nodeList());
        return rt;
    }

    @RequestMapping(value = "/sub/update", method = { RequestMethod.POST })
    public Rt subUpdate(@RequestBody Map<String, Object> rq_u) {
        Rt rt = Rt.suc();
        userService.subUpdate(rq_u);
        return rt;
    }

    @RequestMapping(value = "/list", method = { RequestMethod.POST })
    public Rt list(@RequestBody Map<String, Object> rq_u) {
        Rt rt = Rt.suc();
        rt.setData(userService.list(rq_u));
        return rt;
    }

    @RequestMapping(value = "/update", method = { RequestMethod.POST })
    public Rt update(@RequestBody Map<String, Object> rq_u) {
        Rt rt = Rt.suc();
        userService.update(rq_u);
        return rt;
    }

    @RequestMapping(value = "/password/reset", method = { RequestMethod.POST })
    public Rt pwdReset(@RequestBody Map<String, Object> rq_u) throws Exception {
        Rt rt = Rt.suc();
        userService.pwdReset(rq_u);
        return rt;
    }

    @RequestMapping(value = "/dycode/get", method = { RequestMethod.GET })
    public Rt dycodeSend(@RequestParam String phone) throws Exception {
        Rt rt = Rt.suc();
        userService.dycodeSend(phone);
        return rt;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public Rt logout() {
        Rt rt = Rt.suc();
        userService.logout();
        return rt;
    }

    @RequestMapping(value = "/auth", method = { RequestMethod.GET })
    public Rt auth() {
        Rt rt = Rt.suc();
        return rt;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Rt login(@RequestBody Map<String, Object> rq_u) {
        Rt rt = Rt.suc();
        rt.setData(userService.login(rq_u));
        return rt;
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public Rt test(HttpServletRequest request) {
        Rt rt = Rt.suc();
        System.out.println(session.getCurUser());
        return rt;
    }

}
