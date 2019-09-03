package cn.net.bhe.chargingsystem.app.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
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

import cn.net.bhe.chargingsystem.app.cache.SessionUtils;
import cn.net.bhe.chargingsystem.app.dao.UserDao;
import cn.net.bhe.chargingsystem.app.entity.User;
import cn.net.bhe.chargingsystem.app.entity.UserRefNode;
import cn.net.bhe.chargingsystem.app.entity.Wallet;
import cn.net.bhe.chargingsystem.app.entity.Water;
import cn.net.bhe.chargingsystem.common.dict.K;
import cn.net.bhe.chargingsystem.common.exception.BusinessException;
import cn.net.bhe.chargingsystem.common.exception.ExpException;
import cn.net.bhe.utils.main.MathUtils;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private NodeService nodeService;
    static Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public Map<String, Object> login(HttpServletRequest request, Map<String, Object> rq_u) {
        Integer type = (Integer) rq_u.get("type");
        String phone = (String) rq_u.get("phone");
        String password = (String) rq_u.get("password");
        List<Boolean> valid = new ArrayList<>();
        valid.add(type != null);
        valid.add(StringUtils.isNotEmpty(phone));
        valid.add(StringUtils.isNotEmpty(password));
        if (valid.contains(false)) {
            throw new BusinessException("invalid parameter");
        }
        //

        Map<String, Object> user = userDao.userGet(type, phone);
        if (user == null) {
            throw new BusinessException("user does not exist");
        }
        int status = ((BigDecimal) user.get("status")).intValue();
        if (status != 1) {
            throw new BusinessException("user not available");
        }
        if (!password.equals(user.get("password"))) {
            throw new BusinessException("wrong password");
        }

        // update ip
        user.put("ip", SessionUtils.getIp(request));
        userDao.updateIp(user);

        // update session
        SessionUtils.set(request, K.S_IP, SessionUtils.getIp(request));
        SessionUtils.set(request, K.S_USERID, user.get("id"));
        SessionUtils.set(request, K.S_USER, user);

        String sessionid = request.getSession().getId();
        user.put("session_id", sessionid);
        userDao.updateSessionId(user);

        Map<String, Object> result = new HashMap<>();
        user = new HashMap<>(user);
        user.remove("password");
        result.put("user", user);
        int userId = ((BigInteger) user.get("id")).intValue();
        result.put("wallet", userDao.walletGetByUserId(userId));
        result.put("water", userDao.waterGetByUserId(userId));

        return result;
    }

    public void pwdReset(HttpServletRequest request, Map<String, Object> rq_u) throws Exception {
        String phone = (String) rq_u.get("phone");
        String password = (String) rq_u.get("password");
        List<Boolean> valid = new ArrayList<>();
        valid.add(StringUtils.isNotEmpty(phone));
        valid.add(StringUtils.isNotEmpty(password));
        if (valid.contains(false)) {
            throw new BusinessException("invalid parameter");
        }
        //

        String dycode = (String) rq_u.get("dycode");
        String oldPassword = (String) rq_u.get("oldPassword");
        if (StringUtils.isNotEmpty(dycode)) {
            int check = SessionUtils.validCode(phone, dycode);
            if (check == -1 || check == 2) {
                throw new BusinessException("verification code expired, please reacquire");
            }
            if (check == 0) {
                throw new BusinessException("wrong verification code");
            }
        } else if (StringUtils.isNotEmpty(oldPassword)) {
            Map<String, Object> cachedUser = SessionUtils.get(request, K.S_USER);
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
        userDao.pwdReset(password, phone);
    }

    public List<Map<String, Object>> nodeList() {
        return userDao.nodeList();
    }

    public void dycodeSend(String phone) throws Exception {
        Map<String, Object> user = userDao.userGet(2, phone);
        if (user == null) {
            throw new BusinessException("user doesn't exist");
        }

        SessionUtils.dySms(phone);
    }

    public void register(HttpServletRequest request, Map<String, Object> rq_u) throws Exception {
        String dycode = (String) rq_u.get("dycode");
        String phone = (String) rq_u.get("phone");
        String password = (String) rq_u.get("password");
        Integer nodeId = (Integer) rq_u.get("nodeId");
        List<Boolean> valid = new ArrayList<>();
        valid.add(StringUtils.isNotEmpty(dycode));
        valid.add(StringUtils.isNotEmpty(phone));
        valid.add(StringUtils.isNotEmpty(password));
        valid.add(nodeId != null);
        if (valid.contains(false)) {
            throw new BusinessException("invalid parameter");
        }
        nodeService.nodeCheck(nodeId);
        //

        int check = SessionUtils.validCode(phone, dycode);
        if (check == -1 || check == 2) {
            throw new BusinessException("verification code expired, please reacquire");
        }
        if (check == 0) {
            throw new BusinessException("wrong verification code");
        }
        if (userDao.userCheckoutByPhone(phone) != null) {
            throw new BusinessException("the phone has been registered");
        }

        // user
        User user = new User(rq_u);
        user.setPhone((String) rq_u.get("phone"));
        user.setStatus(0);
        user.setCreatetime(new Date());
        user.setIp(SessionUtils.getIp(request));
        Integer userId = (Integer) userDao.save(user);

        // node
        UserRefNode ref = new UserRefNode();
        ref.setUserId(userId);
        ref.setNodeId(nodeId);
        ref.setCreatetime(new Date());
        userDao.save(ref);

        // wallet
        Wallet wallet = new Wallet();
        wallet.setNum(MathUtils.getRandomNum(17));
        wallet.setUserId(userId);
        wallet.setCredit(0);
        wallet.setBalance(0);
        wallet.setCreatetime(new Date());
        wallet.setUpdatetime(new Date());
        userDao.save(wallet);

        // water
        Water water = new Water();
        water.setEqmNum(MathUtils.getRandomNum(11));
        water.setUserId(userId);
        water.setOldValue(0);
        water.setNewValue(0);
        water.setCreatetime(new Date());
        water.setUpdatetime(new Date());
        userDao.save(water);
    }

    public void update(HttpServletRequest request, Map<String, Object> rq_u) {
        Map<String, Object> cachedUser = SessionUtils.get(request, K.S_USER);
        User user = userDao.getById(User.class, ((BigInteger) cachedUser.get("id")).intValue());

        rq_u.put("ip", SessionUtils.getIp(request));
        user.init(rq_u);
        userDao.update(user);
    }

    public static void main(String[] args) {
        System.out.println(MathUtils.getRandomNum(11));
    }

}
