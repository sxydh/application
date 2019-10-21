package charserver.cn.net.bhe.manage.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import charserver.cn.net.bhe.common.base.Page;
import charserver.cn.net.bhe.common.exception.BusinessException;
import charserver.cn.net.bhe.manage.base.SessionUtils;
import charserver.cn.net.bhe.manage.mapper.WaterMapper;

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
