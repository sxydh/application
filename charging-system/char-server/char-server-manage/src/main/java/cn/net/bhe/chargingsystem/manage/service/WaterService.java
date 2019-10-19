package cn.net.bhe.chargingsystem.manage.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.net.bhe.chargingsystem.common.base.Page;
import cn.net.bhe.chargingsystem.common.exception.BusinessException;
import cn.net.bhe.chargingsystem.manage.base.SessionUtils;
import cn.net.bhe.chargingsystem.manage.mapper.WaterMapper;

@Service
public class WaterService {

    @Autowired
    private WaterMapper waterMapper;

    @Autowired
    SessionUtils session;

    public Page list(Map<String, Object> rq_w) {
        Integer offset = (Integer) rq_w.get("offset");
        Integer limit = (Integer) rq_w.get("limit");
        List<Boolean> valid = new ArrayList<>();
        valid.add(offset != null);
        valid.add(limit != null);
        if (valid.contains(false)) {
            throw new BusinessException("invalid arguments");
        }

        //

        Page page = new Page();
        page.setList(waterMapper.waterList(rq_w));
        page.setCount(waterMapper.waterCount(rq_w));

        return page;
    }

}
