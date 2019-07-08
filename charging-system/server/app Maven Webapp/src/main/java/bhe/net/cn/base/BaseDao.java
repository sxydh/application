package bhe.net.cn.base;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BaseDao {

    @Autowired
    private SessionFactory sessionFactory;
    static Logger LOGGER = LoggerFactory.getLogger(BaseDao.class);

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    public <X> X getById(Class<X> clazz, Serializable id) {
        return (X) sessionFactory.getCurrentSession().get(clazz, id);
    }

    public void saveOrUpdate(Serializable entity) {
        sessionFactory.getCurrentSession().saveOrUpdate(entity);
    }

    public Serializable save(Serializable entity) {
        return sessionFactory.getCurrentSession().save(entity);
    }

    public void persist(Serializable entity) {
        sessionFactory.getCurrentSession().persist(entity);
    }

    public void update(Serializable entity) {
        sessionFactory.getCurrentSession().update(entity);
    }

    public Page pageQuery(Query count, Query query, int offset, int limit) {
        Page page = new Page();
        page.setCount(((BigInteger) count.uniqueResult()).intValue());
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        page.setList(handle(query.list()));
        return page;
    }

    @SuppressWarnings("unchecked")
    public <X> X handle(X x) {

        if (x != null) {
            LOGGER.info(x.getClass().toString());
            LOGGER.info(x.toString());

            if (x instanceof Map<?, ?>) {
                return (X) HelperUtils.lowercaseKey((Map<String, Object>) x);
            } else if (x instanceof List<?>) {
                List<Map<String, Object>> list = new ArrayList<>();
                for (Object temp : (List<?>) x) {
                    Map<String, Object> map = (Map<String, Object>) temp;
                    list.add(HelperUtils.lowercaseKey(map));
                }
                return (X) list;
            } else {
                throw new RuntimeException("Unsupported parameter");
            }
        } else {
            return null;
        }
    }

}
