package bhe.net.cn.service;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bhe.net.cn.dao.NodeDao;
import bhe.net.cn.exception.BusinessException;

@Service
public class NodeService {

    @Autowired
    private NodeDao nodeDao;

    public void nodeCheck(int id) {

        boolean proceed = true;
        Map<String, Object> currentNode = null;

        while (proceed) {
            currentNode = nodeDao.nodeGet(id);
            if (currentNode == null) {
                throw new BusinessException("Abnormal data");
            } else {
                Integer status = ((BigDecimal) currentNode.get("status")).intValue();
                String name = (String) currentNode.get("name");
                if (status == 0) {
                    throw new BusinessException(name + " is disabled");
                }
            }
            Integer pId = ((BigDecimal) currentNode.get("p_id")).intValue();
            if (pId == -1) {
                proceed = false;
            }

            id = pId;
        }
    }

}
