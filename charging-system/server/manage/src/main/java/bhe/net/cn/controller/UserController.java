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
import bhe.net.cn.base.ResponseTemplate;
import bhe.net.cn.base.SessionUtils;
import bhe.net.cn.dict.Note;
import bhe.net.cn.exception.AuthException;
import bhe.net.cn.exception.NoteException;
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
    public ResponseTemplate subAdd(@RequestBody Map<String, Object> rq_u) {
        ResponseTemplate rt = new ResponseTemplate();
        try {
            userService.subAdd(rq_u);
            rt.setSc(Note.SC_OK);
        } catch (NoteException e) {
            rt.setSc(Note.SC_BADREQUEST);
            rt.setMsg(e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getLocalizedMessage());
            rt.setSc(Note.SC_INNERERROR);
            rt.setMsg(Note.MSG_INNERERROR);
        }
        return rt;
    }

    @RequestMapping(value = "/node/list", method = { RequestMethod.GET })
    public ResponseTemplate nodeList() {
        ResponseTemplate rt = new ResponseTemplate();
        try {
            rt.setData(userService.nodeList());
            rt.setSc(Note.SC_OK);
        } catch (NoteException e) {
            rt.setSc(Note.SC_BADREQUEST);
            rt.setMsg(e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getLocalizedMessage());
            rt.setSc(Note.SC_INNERERROR);
            rt.setMsg(Note.MSG_INNERERROR);
        }
        return rt;
    }

    @RequestMapping(value = "/sub/update", method = { RequestMethod.POST })
    public ResponseTemplate subUpdate(@RequestBody Map<String, Object> rq_u) {
        ResponseTemplate rt = new ResponseTemplate();
        try {
            userService.subUpdate(rq_u);
            rt.setSc(Note.SC_OK);
        } catch (NoteException e) {
            rt.setSc(Note.SC_BADREQUEST);
            rt.setMsg(e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getLocalizedMessage());
            rt.setSc(Note.SC_INNERERROR);
            rt.setMsg(Note.MSG_INNERERROR);
        }
        return rt;
    }

    @RequestMapping(value = "/list", method = { RequestMethod.POST })
    public ResponseTemplate list(@RequestBody Map<String, Object> rq_u) {
        ResponseTemplate rt = new ResponseTemplate();
        try {
            rt.setData(userService.list(rq_u));
            rt.setSc(Note.SC_OK);
        } catch (NoteException e) {
            rt.setSc(Note.SC_BADREQUEST);
            rt.setMsg(e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getLocalizedMessage());
            rt.setSc(Note.SC_INNERERROR);
            rt.setMsg(Note.MSG_INNERERROR);
        }
        return rt;
    }

    @RequestMapping(value = "/update", method = { RequestMethod.POST })
    public ResponseTemplate update(@RequestBody Map<String, Object> rq_u) {
        ResponseTemplate rt = new ResponseTemplate();
        try {
            userService.update(rq_u);
            rt.setSc(Note.SC_OK);
        } catch (NoteException e) {
            rt.setSc(Note.SC_BADREQUEST);
            rt.setMsg(e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getLocalizedMessage());
            rt.setSc(Note.SC_INNERERROR);
            rt.setMsg(Note.MSG_INNERERROR);
        }
        return rt;
    }

    @RequestMapping(value = "/password/reset", method = { RequestMethod.POST })
    public ResponseTemplate pwdReset(@RequestBody Map<String, Object> rq_u) {
        ResponseTemplate rt = new ResponseTemplate();
        try {
            userService.pwdReset(rq_u);
            rt.setSc(Note.SC_OK);
        } catch (NoteException e) {
            rt.setSc(Note.SC_BADREQUEST);
            rt.setMsg(e.getLocalizedMessage());
        } catch (AuthException e) {
            rt.setSc(Note.SC_UNAUTHORIZED);
            rt.setMsg(Note.MSG_UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getLocalizedMessage());
            rt.setSc(Note.SC_INNERERROR);
            rt.setMsg(Note.MSG_INNERERROR);
        }
        return rt;
    }

    @RequestMapping(value = "/dycode/get", method = { RequestMethod.GET })
    public ResponseTemplate dycodeSend(@RequestParam String phone) {
        ResponseTemplate rt = new ResponseTemplate();
        try {
            userService.dycodeSend(phone);
            rt.setSc(Note.SC_OK);
        } catch (NoteException e) {
            rt.setSc(Note.SC_BADREQUEST);
            rt.setMsg(e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getLocalizedMessage());
            rt.setSc(Note.SC_INNERERROR);
            rt.setMsg(Note.MSG_INNERERROR);
        }
        return rt;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResponseTemplate logout() {
        ResponseTemplate rt = new ResponseTemplate();
        try {
            userService.logout();
            rt.setSc(Note.SC_OK);
        } catch (NoteException e) {
            rt.setSc(Note.SC_BADREQUEST);
            rt.setMsg(e.getLocalizedMessage());
            LOGGER.error(e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getLocalizedMessage());
            rt.setSc(Note.SC_INNERERROR);
            rt.setMsg(Note.MSG_INNERERROR);
        }
        return rt;
    }

    @RequestMapping(value = "/auth", method = { RequestMethod.GET })
    public ResponseTemplate auth() {
        ResponseTemplate rt = new ResponseTemplate();
        try {
            rt.setSc(Note.SC_OK);
        } catch (NoteException e) {
            rt.setSc(Note.SC_BADREQUEST);
            rt.setMsg(e.getLocalizedMessage());
            LOGGER.error(e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getLocalizedMessage());
            rt.setSc(Note.SC_INNERERROR);
            rt.setMsg(Note.MSG_INNERERROR);
        }
        return rt;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseTemplate login(@RequestBody Map<String, Object> rq_u) {
        ResponseTemplate rt = new ResponseTemplate();
        try {
            rt.setData(userService.login(rq_u));
            rt.setSc(Note.SC_OK);
        } catch (NoteException e) {
            rt.setSc(Note.SC_BADREQUEST);
            rt.setMsg(e.getLocalizedMessage());
            LOGGER.error(e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getLocalizedMessage());
            rt.setSc(Note.SC_INNERERROR);
            rt.setMsg(Note.MSG_INNERERROR);
        }
        return rt;
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public ResponseTemplate test(HttpServletRequest request) {
        ResponseTemplate rt = new ResponseTemplate();
        try {
            System.out.println(session.getCurUser());
            rt.setSc(Note.SC_OK);
        } catch (NoteException e) {
            rt.setSc(Note.SC_BADREQUEST);
            rt.setMsg(e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getLocalizedMessage());
            rt.setSc(Note.SC_INNERERROR);
            rt.setMsg(Note.MSG_INNERERROR);
        }
        return rt;
    }

}
