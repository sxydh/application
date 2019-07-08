package bhe.net.cn.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import bhe.net.cn.base.ResponseTemplate;
import bhe.net.cn.dict.Note;
import bhe.net.cn.exception.NoteException;
import bhe.net.cn.service.WaterService;
import bhe.net.cn.utils.JacksonUtils;

@Controller
@RequestMapping(value = "/water")
public class WaterController {

    static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private WaterService waterService;

    @RequestMapping(value = "/log/list", method = { RequestMethod.POST }, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String logList(HttpServletRequest request, @RequestBody Map<String, Object> rq_w) {
        ResponseTemplate rt = new ResponseTemplate();
        try {
            rt.setData(waterService.logList(request, rq_w));
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

    @RequestMapping(value = "/get", method = { RequestMethod.GET }, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String waterGet(HttpServletRequest request) {
        ResponseTemplate rt = new ResponseTemplate();
        try {
            rt.setData(waterService.waterGet(request));
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
}
