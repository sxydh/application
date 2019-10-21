package charserver.cn.net.bhe.manage.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import charserver.cn.net.bhe.common.base.HelperUtils;
import charserver.cn.net.bhe.common.base.Page;
import charserver.cn.net.bhe.common.exception.BusinessException;
import charserver.cn.net.bhe.common.exception.ExpException;
import charserver.cn.net.bhe.manage.base.SessionUtils;
import charserver.cn.net.bhe.manage.mapper.UserMapper;
import cn.net.bhe.utils.main.MathUtils;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SessionUtils session;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Map<String, Object> login(Map<String, Object> rq_u) {
        String phone = (String) rq_u.get("phone");
        String password = (String) rq_u.get("password");
        List<Boolean> valid = new ArrayList<>();
        valid.add(StringUtils.isNotEmpty(phone));
        valid.add(StringUtils.isNotEmpty(password));
        if (valid.contains(false)) {
            throw new BusinessException("invalid arguments");
        }
        //

        Map<String, Object> user = userMapper.login(rq_u);

        if (user == null) {
            throw new BusinessException("user doesn't exist");
        }
        if (!password.equals(user.get("password"))) {
            throw new BusinessException("wrong password");
        }
        if (((BigDecimal) user.get("status")).intValue() == 0) {
            throw new BusinessException("user is disabled");
        }

        String ip = session.getIp();
        user.put("ip", ip);
        userMapper.ipUpdate(phone, ip);

        String token = HelperUtils.genToken();
        session.set("authorization", token);
        session.setCurUser(token, user);

        user.remove("password");

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("user", user);
        result.put("authorization", token);
        return result;
    }

    public void logout() {
        session.delCurUser();
    }

    public void dycodeSend(String phone) throws Exception {
        Map<String, Object> user = userMapper.userCheckoutByPhone(phone);

        if (user == null) {
            throw new BusinessException("user doesn't exist");
        }

        session.dySms(phone);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void pwdReset(Map<String, Object> rq_u) throws Exception {
        String phone = (String) rq_u.get("phone");
        String password = (String) rq_u.get("password");
        List<Boolean> valid = new ArrayList<>();
        valid.add(StringUtils.isNotEmpty(phone));
        valid.add(StringUtils.isNotEmpty(password));
        if (valid.contains(false)) {
            throw new BusinessException("invalid arguments");
        }
        //

        String dycode = (String) rq_u.get("dycode");
        String oldPassword = (String) rq_u.get("oldPassword");

        if (StringUtils.isNotEmpty(dycode)) {
            int check = session.validCode(phone, dycode);
            if (check == -1 || check == 2) {
                throw new BusinessException("verification code expired, please reacquire");
            }
            if (check == 0) {
                throw new BusinessException("wrong verification code");
            }
        } else if (StringUtils.isNotEmpty(oldPassword)) {
            Map<String, Object> cachedUser = session.getCurUser();
            if (cachedUser == null) {
                throw new ExpException("");
            }
            if (!oldPassword.equals(cachedUser.get("password"))) {
                throw new BusinessException("wrong old password");
            }
            phone = (String) cachedUser.get("phone");
        } else {
            throw new BusinessException("require dycode or old password");
        }
        userMapper.pwdReset(phone, password);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void update(Map<String, Object> rq_u) {
        Map<String, Object> cachedUser = session.getCurUser();
        String phone = (String) cachedUser.get("phone");
        rq_u.put("phone", phone);
        //

        rq_u.put("ip", session.getIp());
        userMapper.update(rq_u);

        Map<String, Object> user = userMapper.userCheckoutByPhone(phone);
        session.setCurUser(session.getToken(), user);
    }

    public Page list(Map<String, Object> rq_u) {
        Integer offset = (Integer) rq_u.get("offset");
        Integer limit = (Integer) rq_u.get("limit");
        List<Boolean> valid = new ArrayList<>();
        valid.add(offset != null);
        valid.add(limit != null);
        if (valid.contains(false)) {
            throw new BusinessException("invalid arguments");
        }

        Map<String, Object> cachedUser = session.getCurUser();
        int type = ((BigDecimal) cachedUser.get("type")).intValue();
        int role = ((BigDecimal) cachedUser.get("role")).intValue();
        rq_u.put("type", type);
        rq_u.put("role", role);
        //

        Integer nodeId = (Integer) rq_u.get("nodeId");
        if (nodeId != null) {
            rq_u.put("nodeIds", userMapper.subNodeIdsGet(nodeId));
        }

        Page page = new Page();
        page.setList(userMapper.userList(rq_u));
        page.setCount(userMapper.userCount(rq_u));

        return page;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void subUpdate(Map<String, Object> rq_u) {
        Integer id = (Integer) rq_u.get("id");
        List<Boolean> valid = new ArrayList<>();
        valid.add(id != null);
        if (valid.contains(false)) {
            throw new BusinessException("invalid arguments");
        }

        rq_u.put("ip", session.getIp());
        Map<String, Object> curUser = session.getCurUser();
        Integer curRole = ((BigDecimal) curUser.get("role")).intValue();
        //

        Map<String, Object> oldUser = userMapper.userGet(id);
        Integer oldType = ((BigDecimal) oldUser.get("type")).intValue();
        Integer oldRole = ((BigDecimal) oldUser.get("role")).intValue();
        valid = new ArrayList<>();
        valid.add(curRole == 0 && oldType == 1 && Arrays.asList(new Integer[] { 1, 2 }).contains(oldRole));
        valid.add(curRole == 0 && oldType == 2);
        valid.add(curRole != 0 && oldType == 2);
        if (!valid.contains(true)) {
            throw new BusinessException("invalid operation");
        }

        Integer tarRole = (Integer) rq_u.get("role");
        if (tarRole != null) {
            valid.add(curRole == 0 && oldType == 1 && Arrays.asList(new Integer[] { 1, 2 }).contains(tarRole));
            valid.add(curRole == 0 && oldType == 2);
            valid.add(curRole != 0 && oldType == 2);
            if (!valid.contains(true)) {
                throw new BusinessException("invalid operation");
            }
        }

        String phone = (String) rq_u.get("phone");
        if (StringUtils.isNotEmpty(phone) && userMapper.phoneCheck(rq_u) != null) {
            throw new BusinessException("The phone number already exists");
        }

        userMapper.subUpdate(rq_u);

        Integer nodeId = (Integer) rq_u.get("nodeId");
        if (nodeId != null && userMapper.nodeGet(nodeId) == null) {
            throw new BusinessException("node doesn't exist!");
        }
        if (nodeId != null) {
            userMapper.userRefNodeDel(id);
            userMapper.userRefNodeAdd(id, nodeId);
        }
    }

    public List<Map<String, Object>> nodeList() {
        return userMapper.nodeList();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void subAdd(Map<String, Object> rq_u) {
        String phone = (String) rq_u.get("phone");
        String password = (String) rq_u.get("password");
        Integer type = (Integer) rq_u.get("type");
        Integer role = (Integer) rq_u.get("role");
        Integer nodeId = (Integer) rq_u.get("nodeId");
        List<Boolean> valid = new ArrayList<>();
        valid.add(StringUtils.isNotEmpty(phone));
        valid.add(StringUtils.isNotEmpty(password));
        valid.add(type != null);
        valid.add(role != null);
        valid.add(nodeId != null);
        if (valid.contains(false)) {
            throw new BusinessException("invalid arguments");
        }

        rq_u.put("ip", session.getIp());

        Map<String, Object> curUser = session.getCurUser();
        Integer curRole = ((BigDecimal) curUser.get("role")).intValue();
        //

        valid = new ArrayList<>();
        valid.add(curRole == 0 && type == 1 && Arrays.asList(new Integer[] { 1, 2 }).contains(role));
        valid.add(curRole == 0 && type == 2);
        valid.add(curRole != 0 && type == 2);
        if (!valid.contains(true)) {
            throw new BusinessException("invalid operation");
        }

        // user
        if (userMapper.phoneCheck(rq_u) != null) {
            throw new BusinessException("The phone number already exists");
        }
        userMapper.subAdd(rq_u);
        int userId = ((BigInteger) rq_u.get("id")).intValue();

        // user ref node
        if (userMapper.nodeGet(nodeId) == null) {
            throw new BusinessException("node doesn't exist!");
        }
        userMapper.userRefNodeAdd(userId, nodeId);

        // wallet
        rq_u = new HashMap<>();
        rq_u.put("num", MathUtils.randomNum(17));
        rq_u.put("userId", userId);
        userMapper.walletAdd(rq_u);

        // water
        rq_u = new HashMap<>();
        rq_u.put("eqmNum", MathUtils.randomNum(11));
        rq_u.put("userId", userId);
        userMapper.waterAdd(rq_u);
    }

}
