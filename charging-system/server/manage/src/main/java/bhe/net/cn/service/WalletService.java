package bhe.net.cn.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import bhe.net.cn.base.HelperUtils;
import bhe.net.cn.base.Page;
import bhe.net.cn.base.SessionUtils;
import bhe.net.cn.exception.BusinessException;
import bhe.net.cn.mapper.WalletMapper;

@Service
public class WalletService {

    @Autowired
    private WalletMapper walletMapper;

    @Autowired
    private SessionUtils session;

    public Page list(Map<String, Object> rq_w) {
        Integer offset = (Integer) rq_w.get("offset");
        Integer limit = (Integer) rq_w.get("limit");
        List<Boolean> valid = new ArrayList<>();
        valid.add(offset != null);
        valid.add(limit != null);
        if (valid.contains(false)) {
            throw new BusinessException("invalid arguments");
        }

        Map<String, Object> cachedUser = session.getCurUser();
        int type = ((BigDecimal) cachedUser.get("type")).intValue();
        int role = ((BigDecimal) cachedUser.get("role")).intValue();
        rq_w.put("type", type);
        rq_w.put("role", role);
        //

        Page page = new Page();
        page.setList(walletMapper.walletList(rq_w));
        page.setCount(walletMapper.walletCount(rq_w));

        return page;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void recharge(Map<String, Object> rq_w) {
        String walletNum = (String) rq_w.get("walletNum");
        Integer value = (Integer) rq_w.get("value");
        List<Boolean> valid = new ArrayList<>();
        valid.add(StringUtils.isNotEmpty(walletNum));
        valid.add(value != null && value > 0);
        if (valid.contains(false)) {
            throw new BusinessException("invalid arguments");
        }

        Map<String, Object> curUser = session.getCurUser();
        Integer curId = ((Long) curUser.get("id")).intValue();
        Integer curRole = ((BigDecimal) curUser.get("role")).intValue();
        //

        if (!Arrays.asList(new Integer[] { 0, 1 }).contains(curRole)) {
            throw new BusinessException("invalid operation");
        }

        Map<String, Object> oldWallet = walletMapper.walletCheckoutByNumForUpdate(walletNum);
        if (oldWallet == null) {
            throw new BusinessException("wrong wallet num");
        }
        Map<String, Object> tarUser = walletMapper.userCheckoutByWalletNum(walletNum);

        Integer tarType = ((BigDecimal) tarUser.get("type")).intValue();
        Integer tarRole = ((BigDecimal) tarUser.get("role")).intValue();
        if (tarType != 2 || tarRole != 1) {
            throw new BusinessException("invalid operation");
        }

        // order
        Long walletId = (Long) oldWallet.get("id");
        BigDecimal balance = (BigDecimal) oldWallet.get("balance");
        rq_w.put("id", HelperUtils.buildOrderId(((Long) tarUser.get("id")).intValue()));
        rq_w.put("type", 3);
        rq_w.put("status", 1);
        rq_w.put("walletId", walletId);
        rq_w.put("credit", HelperUtils.creditGet(value));
        rq_w.put("balance", balance);
        rq_w.put("userId", curId);
        walletMapper.orderAdd(rq_w);

        // wallet
        BigDecimal oldCredit = (BigDecimal) oldWallet.get("credit");
        BigDecimal oldBalance = (BigDecimal) oldWallet.get("balance");
        rq_w.put("id", walletId);
        rq_w.put("credit", oldCredit.add(new BigDecimal(HelperUtils.creditGet(value))));
        rq_w.put("balance", oldBalance.add(new BigDecimal(value)));
        walletMapper.walletUpdate(rq_w);

        // wallet log
        rq_w.put("remark", HelperUtils.walletLogRemarkGen(3, value));
        walletMapper.walletLogAdd(rq_w);
    }

    public Page logList(Map<String, Object> rq_w) {
        String num = (String) rq_w.get("num");
        Integer offset = (Integer) rq_w.get("offset");
        Integer limit = (Integer) rq_w.get("limit");
        List<Boolean> valid = new ArrayList<>();
        valid.add(StringUtils.isNotEmpty(num));
        valid.add(offset != null);
        valid.add(limit != null);
        if (valid.contains(false)) {
            throw new BusinessException("invalid arguments");
        }

        Map<String, Object> cachedUser = session.getCurUser();
        int role = ((BigDecimal) cachedUser.get("role")).intValue();
        if (!Arrays.asList(new Integer[] { 0, 1 }).contains(role)) {
            throw new BusinessException("invalid operation");
        }
        //

        Page page = new Page();
        page.setList(walletMapper.logList(rq_w));
        page.setCount(walletMapper.logCount(rq_w));

        return page;
    }

    public Page orderList(Map<String, Object> rq_w) {
        String num = (String) rq_w.get("num");
        Integer offset = (Integer) rq_w.get("offset");
        Integer limit = (Integer) rq_w.get("limit");
        List<Boolean> valid = new ArrayList<>();
        valid.add(StringUtils.isNotEmpty(num));
        valid.add(offset != null);
        valid.add(limit != null);
        if (valid.contains(false)) {
            throw new BusinessException("invalid arguments");
        }

        Map<String, Object> cachedUser = session.getCurUser();
        int role = ((BigDecimal) cachedUser.get("role")).intValue();
        if (!Arrays.asList(new Integer[] { 0, 1 }).contains(role)) {
            throw new BusinessException("invalid operation");
        }
        //

        Page page = new Page();
        page.setList(walletMapper.orderList(rq_w));
        page.setCount(walletMapper.orderCount(rq_w));

        return page;
    }

}
