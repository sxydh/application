package bhe.net.cn.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import bhe.net.cn.base.Rt;
import bhe.net.cn.service.WalletService;

@RestController
@RequestMapping(value = "/wallet")
public class WalletController {

    static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private WalletService walletService;

    @RequestMapping(value = "/order/list", method = { RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Object orderList(HttpServletRequest request, @RequestBody Map<String, Object> rq_w) {
        Rt rt = Rt.suc();
        rt.setData(walletService.orderList(request, rq_w));
        return rt;
    }

    @RequestMapping(value = "/log/list", method = { RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Object logList(HttpServletRequest request, @RequestBody Map<String, Object> rq_w) {
        Rt rt = Rt.suc();
        rt.setData(walletService.logList(request, rq_w));
        return rt;
    }

    @RequestMapping(value = "/order/get", method = { RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Object orderGet(HttpServletRequest request, @RequestBody Map<String, Object> rq_w) {
        Rt rt = Rt.suc();
        rt.setData(walletService.orderGet(rq_w));
        return rt;
    }

    @RequestMapping(value = "/recharge", method = { RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Object recharge(HttpServletRequest request, @RequestBody Map<String, Object> rq_w) throws Exception {
        Rt rt = Rt.suc();
        rt.setData(walletService.recharge(request, rq_w));
        return rt;
    }

    @RequestMapping(value = "/order/cancel", method = { RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Object orderCancel(HttpServletRequest request, @RequestBody Map<String, Object> rq_w) {
        Rt rt = Rt.suc();
        walletService.orderCancel(rq_w);
        return rt;
    }

    @RequestMapping(value = "/get", method = { RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Object walletGet(HttpServletRequest request) {
        Rt rt = Rt.suc();
        rt.setData(walletService.walletGet(request));
        return rt;
    }

    @RequestMapping(value = "/qr_code/get", method = { RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Object qrCodeGet(HttpServletRequest request) {
        Rt rt = Rt.suc();
        rt.setData("http://localhost:8080/app/images/QR_code.png");
        return rt;
    }

    @RequestMapping(value = "/recharge/callback", method = { RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Object rechargeCallback(HttpServletRequest request, @RequestBody Map<String, Object> rq_w) {
        Rt rt = Rt.suc();
        walletService.rechargeCallback(request, rq_w);
        return rt;
    }

}
