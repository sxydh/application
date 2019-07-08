package bhe.net.cn.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

import bhe.net.cn.auth.SessionKey;
import bhe.net.cn.auth.SessionUtils;
import bhe.net.cn.base.HelperUtils;
import bhe.net.cn.base.Page;
import bhe.net.cn.dao.WalletDao;
import bhe.net.cn.entity.Order;
import bhe.net.cn.entity.Wallet;
import bhe.net.cn.entity.WalletLog;
import bhe.net.cn.exception.NoteException;
import bhe.net.cn.utils.HttpClientUtils;
import bhe.net.cn.utils.JacksonUtils;

@Service
public class WalletService {

    @Autowired
    private WalletDao walletDao;
    static Logger LOGGER = LoggerFactory.getLogger(WalletService.class);

    public Map<String, Object> recharge(HttpServletRequest request, Map<String, Object> rq_w) throws Exception {
        Integer type = (Integer) rq_w.get("type");
        Integer value = (Integer) rq_w.get("value");
        List<Boolean> valid = new ArrayList<>();
        valid.add(type != null && type != 2);
        valid.add(value != null && value > 0);
        if (valid.contains(false)) {
            throw new NoteException("invalid parameter");
        }
        Integer userId = ((BigInteger) SessionUtils.get(request, SessionKey.USERID)).intValue();
        //

        // api
        String orderId = HelperUtils.buildOrderId(userId);
        /*
        Config config = new Config(type, value / 100f, orderId);
        GYPResponse gypresponse = GreenYepPay.execute(config);
        */

        // order
        Map<String, Object> wallet = walletDao.walletCheckoutByUserId(userId);
        Order order = new Order();
        order.setId(orderId);
        order.setWalletId(((BigInteger) wallet.get("id")).intValue());
        order.setType(type);
        order.setAmount(value);
        order.setStatus(0);
        order.setCreatetime(new Date());
        order.setUpdatetime(new Date());
        order.setCredit(HelperUtils.creditGet(value));
        order.setBalance(((BigDecimal) wallet.get("balance")).intValue());
        order.setOperatorId(userId);
        order.setGreenpayId(HelperUtils.buildOrderId(userId));
        walletDao.save(order);

        Map<String, Object> result = new HashMap<>();
        Map<String, Object> returnOrder = new HashMap<>();
        returnOrder.put("id", order.getId());
        returnOrder.put("status", order.getStatus());
        returnOrder.put("greenpay_id", order.getGreenpayId());
        returnOrder.put("qr_code", "http://localhost:8080/app/images/QR_code.png");
        result.put("order", returnOrder);
        return result;
    }

    public Page orderList(HttpServletRequest request, Map<String, Object> rq_w) {
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

        return walletDao.orderList(rq_w);
    }

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

        return walletDao.logList(rq_w);
    }

    public Map<String, Object> orderGet(Map<String, Object> rq_w) {
        String id = (String) rq_w.get("id");
        List<Boolean> valid = new ArrayList<>();
        valid.add(StringUtils.isNotEmpty(id));
        if (valid.contains(false)) {
            throw new NoteException("invalid parameter");
        }
        //

        return walletDao.orderGet(id);
    }

    public Map<String, Object> walletGet(HttpServletRequest request) {
        Integer userId = ((BigInteger) SessionUtils.get(request, SessionKey.USERID)).intValue();
        //

        return walletDao.walletCheckoutByUserId(userId);
    }

    public void orderCancel(Map<String, Object> rq_w) {
        String id = (String) rq_w.get("id");
        List<Boolean> valid = new ArrayList<>();
        valid.add(id != null);
        if (valid.contains(false)) {
            throw new NoteException("invalid parameter");
        }
        //

        Order order = walletDao.orderGetForUpdate(id);
        if (order.getStatus() == -1) {
            throw new NoteException("Repeated operation");
        }

        order.setStatus(-1);
        order.setUpdatetime(new Date());
        walletDao.update(order);
    }

    public void rechargeCallback(HttpServletRequest request, Map<String, Object> rq_w) {
        String id = (String) rq_w.get("id");
        List<Boolean> valid = new ArrayList<>();
        valid.add(StringUtils.isNotEmpty(id));
        if (valid.contains(false)) {
            throw new NoteException("invalid parameter");
        }
        Integer userId = ((BigInteger) SessionUtils.get(request, SessionKey.USERID)).intValue();
        //

        // wallet
        Wallet wallet = walletDao.walletGetByUserIdForUpdate(userId);
        Order order = walletDao.orderGetForUpdate(id);
        wallet.setBalance(wallet.getBalance() + order.getAmount());
        wallet.setCredit(wallet.getCredit() + HelperUtils.creditGet(order.getAmount()));
        wallet.setUpdatetime(new Date());
        walletDao.update(wallet);

        // wallet log
        String remark = HelperUtils.walletLogRemarkGen(order.getType(), order.getAmount());
        WalletLog walletLog = new WalletLog();
        walletLog.setWalletId(wallet.getId());
        walletLog.setOperatorId(userId);
        walletLog.setCreatetime(new Date());
        walletLog.setRemark(remark);
        walletDao.save(walletLog);
    }

    public void unpaidOrderStatusUpdate(String id) {
        Order order = walletDao.orderGetForUpdate(id);

        if (order.getStatus() != 0) {
            return;
        }

        long create = order.getCreatetime().getTime();
        long now = System.currentTimeMillis();

        //
        if ((now - create) / (1000 * 60) > 30) {
            order.setStatus(-1);
        }

        //
        else {
            Map<String, Object> map = new HashMap<>();
            map.put("greenpay_id", order.getGreenpayId());
            try {
                String jsonStr = HttpClientUtils.get("https://www.greenyep.com/api/index/query", map);
                LOGGER.info(jsonStr);
                JsonNode node = JacksonUtils.readTree(jsonStr);
                if (!Arrays.asList(new String[] { "400" }).contains(node.findValue("code").toString())) {
                    order.setStatus(1);
                }
            } catch (Exception e) {
                LOGGER.info(e.getLocalizedMessage());
            }
        }

        walletDao.update(order);
    }

    public List<String> unpaidOrderIdList() {
        return walletDao.unpaidOrderIdList();
    }

}
