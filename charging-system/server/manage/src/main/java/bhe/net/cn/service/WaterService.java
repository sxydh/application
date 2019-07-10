package bhe.net.cn.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bhe.net.cn.base.Page;
import bhe.net.cn.base.SessionUtils;
import bhe.net.cn.exception.NoteException;
import bhe.net.cn.mapper.WaterMapper;

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
            throw new NoteException("invalid arguments");
        }

        //

        Page page = new Page();
        page.setList(waterMapper.waterList(rq_w));
        page.setCount(waterMapper.waterCount(rq_w));

        return page;
    }

}
