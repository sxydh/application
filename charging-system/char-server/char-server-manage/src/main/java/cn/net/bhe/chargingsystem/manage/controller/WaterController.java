package cn.net.bhe.chargingsystem.manage.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.net.bhe.chargingsystem.common.base.Rt;
import cn.net.bhe.chargingsystem.manage.service.WaterService;

@RestController
@RequestMapping("/water")
public class WaterController {

    static final Logger LOGGER = LoggerFactory.getLogger(WalletController.class);

    @Autowired
    private WaterService waterService;

    @RequestMapping(value = "/list", method = { RequestMethod.POST })
    public Rt list(@RequestBody Map<String, Object> rq_w) {
        Rt rt = Rt.suc();
        rt.setData(waterService.list(rq_w));
        return rt;
    }

}
