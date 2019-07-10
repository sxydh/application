package bhe.net.cn.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import bhe.net.cn.base.ResponseTemplate;
import bhe.net.cn.dict.Note;
import bhe.net.cn.exception.NoteException;
import bhe.net.cn.service.WalletService;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    static final Logger LOGGER = LoggerFactory.getLogger(WalletController.class);

    @Autowired
    private WalletService walletService;

    @RequestMapping(value = "/order/list", method = { RequestMethod.POST })
    public ResponseTemplate orderList(@RequestBody Map<String, Object> rq_w) {
        ResponseTemplate rt = new ResponseTemplate();
        try {
            rt.setData(walletService.orderList(rq_w));
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

    @RequestMapping(value = "/log/list", method = { RequestMethod.POST })
    public ResponseTemplate logList(@RequestBody Map<String, Object> rq_w) {
        ResponseTemplate rt = new ResponseTemplate();
        try {
            rt.setData(walletService.logList(rq_w));
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

    @RequestMapping(value = "/recharge", method = { RequestMethod.POST })
    public ResponseTemplate recharge(@RequestBody Map<String, Object> rq_w) {
        ResponseTemplate rt = new ResponseTemplate();
        try {
            walletService.recharge(rq_w);
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
    public ResponseTemplate list(@RequestBody Map<String, Object> rq_w) {
        ResponseTemplate rt = new ResponseTemplate();
        try {
            rt.setData(walletService.list(rq_w));
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
