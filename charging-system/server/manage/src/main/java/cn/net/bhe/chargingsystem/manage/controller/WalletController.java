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
import cn.net.bhe.chargingsystem.manage.service.WalletService;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    static final Logger LOGGER = LoggerFactory.getLogger(WalletController.class);

    @Autowired
    private WalletService walletService;

    @RequestMapping(value = "/order/list", method = { RequestMethod.POST })
    public Rt orderList(@RequestBody Map<String, Object> rq_w) {
        Rt rt = Rt.suc();
        rt.setData(walletService.orderList(rq_w));
        return rt;
    }

    @RequestMapping(value = "/log/list", method = { RequestMethod.POST })
    public Rt logList(@RequestBody Map<String, Object> rq_w) {
        Rt rt = Rt.suc();
        rt.setData(walletService.logList(rq_w));
        return rt;
    }

    @RequestMapping(value = "/recharge", method = { RequestMethod.POST })
    public Rt recharge(@RequestBody Map<String, Object> rq_w) {
        Rt rt = Rt.suc();
        walletService.recharge(rq_w);
        return rt;
    }

    @RequestMapping(value = "/list", method = { RequestMethod.POST })
    public Rt list(@RequestBody Map<String, Object> rq_w) {
        Rt rt = Rt.suc();
        rt.setData(walletService.list(rq_w));
        return rt;
    }

}
