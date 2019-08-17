package bhe.net.cn.dao;

import java.util.Map;

import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

@Repository
public class NodeDao extends BaseDao {

    @SuppressWarnings("unchecked")
    public Map<String, Object> nodeGet(int id) {
        String sql = " SELECT * FROM CS_NODE WHERE id = :id ";
        return handle((Map<String, Object>) getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                //
                .setInteger("id", id).uniqueResult());
    }

}
