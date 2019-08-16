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
import org.springframework.web.bind.annotation.ResponseBody;

import bhe.net.cn.base.ResponseTemplate;
import bhe.net.cn.dict.Note;
import bhe.net.cn.exception.NoteException;
import bhe.net.cn.service.WalletService;
import bhe.net.cn.utils.JacksonUtils;

@Controller
@RequestMapping(value = "/wallet")
public class WalletController {

    static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private WalletService walletService;

    @RequestMapping(value = "/order/list", method = { RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String orderList(HttpServletRequest request, @RequestBody Map<String, Object> rq_w) {
        ResponseTemplate rt = new ResponseTemplate();
        try {
            rt.setData(walletService.orderList(request, rq_w));
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

    @RequestMapping(value = "/log/list", method = { RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String logList(HttpServletRequest request, @RequestBody Map<String, Object> rq_w) {
        ResponseTemplate rt = new ResponseTemplate();
        try {
            rt.setData(walletService.logList(request, rq_w));
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

    @RequestMapping(value = "/order/get", method = { RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String orderGet(HttpServletRequest request, @RequestBody Map<String, Object> rq_w) {
        ResponseTemplate rt = new ResponseTemplate();
        try {
            rt.setData(walletService.orderGet(rq_w));
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

    @RequestMapping(value = "/recharge", method = { RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String recharge(HttpServletRequest request, @RequestBody Map<String, Object> rq_w) {
        ResponseTemplate rt = new ResponseTemplate();
        try {
            rt.setData(walletService.recharge(request, rq_w));
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

    @RequestMapping(value = "/order/cancel", method = { RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String orderCancel(HttpServletRequest request, @RequestBody Map<String, Object> rq_w) {
        ResponseTemplate rt = new ResponseTemplate();
        try {
            walletService.orderCancel(rq_w);
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

    @RequestMapping(value = "/get", method = { RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String walletGet(HttpServletRequest request) {
        ResponseTemplate rt = new ResponseTemplate();
        try {
            rt.setData(walletService.walletGet(request));
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

    @RequestMapping(value = "/qr_code/get", method = { RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String qrCodeGet(HttpServletRequest request) {
        ResponseTemplate rt = new ResponseTemplate();
        try {
            rt.setData("http://localhost:8080/app/images/QR_code.png");
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

    @RequestMapping(value = "/recharge/callback", method = { RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String rechargeCallback(HttpServletRequest request, @RequestBody Map<String, Object> rq_w) {
        ResponseTemplate rt = new ResponseTemplate();
        try {
            walletService.rechargeCallback(request, rq_w);
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
