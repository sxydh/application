package charserver.cn.net.bhe.app.controller;

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

import charserver.cn.net.bhe.app.service.WaterService;
import charserver.cn.net.bhe.common.base.Rt;

@RestController
@RequestMapping(value = "/water")
public class WaterController {

    static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private WaterService waterService;

    @RequestMapping(value = "/log/list", method = { RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Object logList(HttpServletRequest request, @RequestBody Map<String, Object> rq_w) {
        Rt rt = Rt.suc();
        rt.setData(waterService.logList(request, rq_w));
        return rt;
    }

    @RequestMapping(value = "/get", method = { RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Object waterGet(HttpServletRequest request) {
        Rt rt = Rt.suc();
        rt.setData(waterService.waterGet(request));
        return rt;
    }
}
