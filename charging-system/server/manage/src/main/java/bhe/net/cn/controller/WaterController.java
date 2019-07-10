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
import bhe.net.cn.service.WaterService;

@RestController
@RequestMapping("/water")
public class WaterController {

    static final Logger LOGGER = LoggerFactory.getLogger(WalletController.class);

    @Autowired
    private WaterService waterService;

    @RequestMapping(value = "/list", method = { RequestMethod.POST })
    public ResponseTemplate list(@RequestBody Map<String, Object> rq_w) {
        ResponseTemplate rt = new ResponseTemplate();
        try {
            rt.setData(waterService.list(rq_w));
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
