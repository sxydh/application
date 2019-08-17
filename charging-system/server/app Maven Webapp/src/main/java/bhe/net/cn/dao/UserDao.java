package bhe.net.cn.dao;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends BaseDao {

    @SuppressWarnings("unchecked")
    public Map<String, Object> userGet(int type, String phone) {
        String sql = " SELECT * FROM CS_USER WHERE type = :type AND phone = :phone ";
        return handle((Map<String, Object>) getSession().createSQLQuery(sql)
                //
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                //
                .setInteger("type", type)
                //
                .setString("phone", phone).uniqueResult());
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> waterGetByUserId(int userId) {
        String sql = " SELECT * FROM CS_WATER WHERE user_id = :userId ";
        return handle((Map<String, Object>) getSession().createSQLQuery(sql)
                //
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                //
                .setInteger("userId", userId).uniqueResult());
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> walletGetByUserId(int userId) {
        String sql = " SELECT * FROM CS_WALLET WHERE user_id = :userId ";
        return handle((Map<String, Object>) getSession().createSQLQuery(sql)
                //
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                //
                .setInteger("userId", userId).uniqueResult());
    }

    public void pwdReset(String password, String phone) {
        String sql = " UPDATE CS_USER SET "
                //
                + " password = :password, "
                //
                + " updatetime = NOW() "
                //
                + " WHERE phone = :phone AND type = 2 ";
        getSession().createSQLQuery(sql)
                //
                .setString("password", password)
                //
                .setString("phone", phone).executeUpdate();
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> userCheckoutByPhone(String phone) {
        String sql = " SELECT * FROM CS_USER WHERE phone = :phone AND type = 2 ";
        return handle((Map<String, Object>) getSession().createSQLQuery(sql)
                //
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                //
                .setString("phone", phone).uniqueResult());
    }

    public void addUserRefNode(int userId, int nodeId) {
        String sql = " INSERT INTO CS_USER_REF_NODE "
                //
                + " (user_id, node_id, createtime) "
                //
                + " VALUES (:userId, :nodeId, NOW()) ";
        getSession().createSQLQuery(sql)
                //
                .setInteger("userId", userId)
                //
                .setInteger("areaId", nodeId).executeUpdate();
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> nodeList() {
        String sql = " SELECT * FROM CS_NODE ";
        return handle(getSession().createSQLQuery(sql)
                //
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                //
                .list());
    }

    public void updateIp(Map<String, Object> user) {
        String sql = " UPDATE CS_USER SET ip = :ip, updatetime = NOW() WHERE id = :id ";
        getSession().createSQLQuery(sql).setBigInteger("id", (BigInteger) user.get("id")).setString("ip", (String) user.get("ip")).executeUpdate();
    }

}
