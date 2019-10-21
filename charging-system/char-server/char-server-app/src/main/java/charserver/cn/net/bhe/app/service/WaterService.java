package charserver.cn.net.bhe.app.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import charserver.cn.net.bhe.app.cache.SessionUtils;
import charserver.cn.net.bhe.app.dao.WaterDao;
import charserver.cn.net.bhe.common.base.Page;
import charserver.cn.net.bhe.common.dict.K;
import charserver.cn.net.bhe.common.exception.BusinessException;

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
            throw new BusinessException("invalid parameter");
        }
        rq_w.put("userId", SessionUtils.get(request, K.S_USERID));
        //

        return waterDao.logList(rq_w);
    }

    public Map<String, Object> waterGet(HttpServletRequest request) {
        Integer userId = ((BigInteger) SessionUtils.get(request, K.S_USERID)).intValue();
        //

        return waterDao.waterCheckoutByUserId(userId);
    }

}
