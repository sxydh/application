package bhe.net.cn.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import bhe.net.cn.auth.SessionUtils;
import bhe.net.cn.base.ResponseTemplate;
import bhe.net.cn.dict.Note;
import bhe.net.cn.exception.AuthException;
import bhe.net.cn.exception.NoteException;
import bhe.net.cn.service.UserService;
import bhe.net.cn.utils.JacksonUtils;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = { RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String login(HttpServletRequest request, @RequestBody Map<String, Object> rq_u) {
        ResponseTemplate rt = new ResponseTemplate();
        try {
            rt.setSc(Note.SC_OK);
            rt.setData(userService.login(request, rq_u));
        } catch (NoteException e) {
            rt.setSc(Note.SC_BADREQUEST);
            rt.setMsg(e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getLocalizedMessage());
            rt.setSc(Note.SC_INNERERROR);
            rt.setMsg(Note.MSG_INNERERROR);
        }
        return JacksonUtils.objToJsonStr(rt);
    }

    @RequestMapping(value = "/node/list", method = { RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String nodeList(HttpServletRequest request) {
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
        return JacksonUtils.objToJsonStr(rt);
    }

    @RequestMapping(value = "/dycode/get", method = { RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String dycodeSend(HttpServletRequest request, @RequestParam String phone) {
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
        return JacksonUtils.objToJsonStr(rt);
    }

    @RequestMapping(value = "/password/reset", method = { RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String pwdReset(HttpServletRequest request, @RequestBody Map<String, Object> rq_u) {
        ResponseTemplate rt = new ResponseTemplate();
        try {
            userService.pwdReset(request, rq_u);
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
        return JacksonUtils.objToJsonStr(rt);
    }

    @RequestMapping(value = "/register", method = { RequestMethod.POST }, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String register(HttpServletRequest request, @RequestBody Map<String, Object> rq_u) {
        ResponseTemplate rt = new ResponseTemplate();
        try {
            throw new NoteException("Registration is not currently available");
        } catch (NoteException e) {
            rt.setSc(Note.SC_BADREQUEST);
            rt.setMsg(e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getLocalizedMessage());
            rt.setSc(Note.SC_INNERERROR);
            rt.setMsg(Note.MSG_INNERERROR);
        }
        return JacksonUtils.objToJsonStr(rt);
    }

    @RequestMapping(value = "/logout", method = { RequestMethod.POST }, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String logout(HttpServletRequest request) {
        ResponseTemplate rt = new ResponseTemplate();
        try {
            SessionUtils.invalidate(request);
            rt.setSc(Note.SC_OK);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getLocalizedMessage());
            rt.setSc(Note.SC_INNERERROR);
            rt.setMsg(Note.MSG_INNERERROR);
        }
        return JacksonUtils.objToJsonStr(rt);
    }

    @RequestMapping(value = "/update", method = { RequestMethod.POST }, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String update(HttpServletRequest request, @RequestBody Map<String, Object> rq_u) {
        ResponseTemplate rt = new ResponseTemplate();
        try {
            userService.update(request, rq_u);
            rt.setSc(Note.SC_OK);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getLocalizedMessage());
            rt.setSc(Note.SC_INNERERROR);
            rt.setMsg(Note.MSG_INNERERROR);
        }
        return JacksonUtils.objToJsonStr(rt);
    }

    @RequestMapping(value = "/auth", method = { RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String auth(HttpServletRequest request) {
        ResponseTemplate rt = new ResponseTemplate();
        try {
            rt.setSc(Note.SC_OK);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getLocalizedMessage());
            rt.setSc(Note.SC_INNERERROR);
            rt.setMsg(Note.MSG_INNERERROR);
        }
        return JacksonUtils.objToJsonStr(rt);
    }

}
