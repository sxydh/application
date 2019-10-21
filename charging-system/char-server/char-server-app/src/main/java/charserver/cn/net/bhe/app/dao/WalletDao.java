package charserver.cn.net.bhe.app.dao;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import charserver.cn.net.bhe.app.entity.Order;
import charserver.cn.net.bhe.app.entity.Wallet;
import charserver.cn.net.bhe.common.base.Page;

@Repository
public class WalletDao extends BaseDao {

    public Wallet walletGetByUserIdForUpdate(int userId) {
        String sql = " SELECT * FROM CS_WALLET WHERE user_id = :userId FOR UPDATE ";
        return (Wallet) getSession().createSQLQuery(sql)
                //
                .addEntity(Wallet.class)
                //
                .setInteger("userId", userId).uniqueResult();
    }

    public Page orderList(Map<String, Object> rq_w) {
        String sql = " SELECT o.*, u.phone operator FROM CS_ORDER o "
                //
                + " INNER JOIN CS_WALLET w ON o.wallet_id = w.id "
                //
                + " INNER JOIN CS_USER u ON u.id = o.operator_id "
                //
                + " WHERE w.user_id = :userId ORDER BY o.createtime DESC, o.id ";
        String sqlc = " SELECT COUNT(0) FROM (" + sql + ") c ";
        Query count = getSession().createSQLQuery(sqlc)
                //
                .setBigInteger("userId", (BigInteger) rq_w.get("userId"));
        Query query = getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                //
                .setBigInteger("userId", (BigInteger) rq_w.get("userId"));
        return pageQuery(count, query, (int) rq_w.get("offset"), (int) rq_w.get("limit"));
    }

    public Page logList(Map<String, Object> rq_w) {
        String sql = " SELECT wl.*, u.phone operator FROM CS_WALLET_LOG wl "
                //
                + " INNER JOIN CS_WALLET w ON w.id = wl.wallet_id "
                //
                + " INNER JOIN CS_USER u ON wl.operator_id = u.id "
                //
                + " WHERE w.user_id = :userId ORDER BY wl.createtime DESC, wl.id ";
        String sqlc = " SELECT COUNT(0) FROM (" + sql + ") c ";
        Query count = getSession().createSQLQuery(sqlc)
                //
                .setBigInteger("userId", (BigInteger) rq_w.get("userId"));
        Query query = getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                //
                .setBigInteger("userId", (BigInteger) rq_w.get("userId"));
        return pageQuery(count, query, (int) rq_w.get("offset"), (int) rq_w.get("limit"));
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> orderGet(String id) {
        String sql = " SELECT o.*, u.phone operator FROM CS_ORDER o "
                //
                + " INNER JOIN CS_USER u ON u.id = o.operator_id "
                //
                + " WHERE o.id = :id ";
        return handle((Map<String, Object>) getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                //
                .setString("id", id).uniqueResult());
    }

    @SuppressWarnings("unchecked")
    public List<String> unpaidOrderIdList() {
        String sql = " SELECT id FROM CS_ORDER WHERE status = 0 ";
        return getSession().createSQLQuery(sql).addScalar("id", StandardBasicTypes.STRING)
                //
                .list();
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> walletCheckoutByUserId(int userId) {
        String sql = " SELECT w.* FROM CS_WALLET w INNER JOIN CS_USER u ON u.id = :userId AND w.user_id = u.id ";
        return handle((Map<String, Object>) getSession().createSQLQuery(sql)
                //
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setInteger("userId", userId).uniqueResult());
    }

    public Order orderGetForUpdate(String id) {
        String sql = " SELECT * FROM CS_ORDER WHERE id = :id FOR UPDATE ";
        return (Order) getSession().createSQLQuery(sql).addEntity(Order.class)
                //
                .setString("id", id).uniqueResult();
    }

}
