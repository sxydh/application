package bhe.net.cn.dao;

import java.math.BigInteger;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import bhe.net.cn.base.BaseDao;
import bhe.net.cn.base.Page;

@Repository
public class WaterDao extends BaseDao {

    public Page logList(Map<String, Object> rq_w) {
        String sql = " SELECT wl.*, u.phone operator FROM CS_WATER_LOG wl "
                //
                + " INNER JOIN CS_WATER w ON w.id = wl.water_id "
                //
                + " INNER JOIN CS_USER u ON u.id = wl.operator_id "
                //
                + " WHERE u.id = :userId ORDER BY wl.createtime DESC, wl.id ";
        String sqlc = " SELECT COUNT(0) FROM (" + sql + ") c ";
        Query query = getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                //
                .setBigInteger("userId", (BigInteger) rq_w.get("userId"));
        Query count = getSession().createSQLQuery(sqlc)
                //
                .setBigInteger("userId", (BigInteger) rq_w.get("userId"));
        return pageQuery(count, query, (int) rq_w.get("offset"), (int) rq_w.get("limit"));
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> waterGet(int id) {
        String sql = " SELECT * FROM CS_WATER WHERE id = :id ";
        return handle((Map<String, Object>) getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                //
                .setInteger("id", id).uniqueResult());
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> waterCheckoutByUserId(int userId) {
        String sql = " SELECT w.* FROM CS_WATER w "
                //
                + " INNER JOIN CS_USER u ON u.id = :userId AND w.user_id = u.id ";
        return handle((Map<String, Object>) getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                //
                .setInteger("userId", userId).uniqueResult());
    }
}
