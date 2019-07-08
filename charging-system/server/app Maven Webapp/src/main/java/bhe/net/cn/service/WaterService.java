package bhe.net.cn.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bhe.net.cn.auth.SessionKey;
import bhe.net.cn.auth.SessionUtils;
import bhe.net.cn.base.Page;
import bhe.net.cn.dao.WaterDao;
import bhe.net.cn.exception.NoteException;

@Service
public class WaterService {

    @Autowired
    private WaterDao waterDao;

    public Page logList(HttpServletRequest request, Map<String, Object> rq_w) {
        Integer offset = (Integer) rq_w.get("offset");
        Integer limit = (Integer) rq_w.get("limit");
        List<Boolean> valid = new ArrayList<>();
        valid.add(offset != null);
        valid.add(limit != null);
        if (valid.contains(false)) {
            throw new NoteException("invalid parameter");
        }
        rq_w.put("userId", SessionUtils.get(request, SessionKey.USERID));
        //

        return waterDao.logList(rq_w);
    }

    public Map<String, Object> waterGet(HttpServletRequest request) {
        Integer userId = ((BigInteger) SessionUtils.get(request, SessionKey.USERID)).intValue();
        //

        return waterDao.waterCheckoutByUserId(userId);
    }

}
