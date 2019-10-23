package charserver.cn.net.bhe.app.service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import charserver.cn.net.bhe.app.dao.NodeDao;
import charserver.cn.net.bhe.app.entity.Node;
import charserver.cn.net.bhe.common.exception.BusinessException;
import cn.net.bhe.utils.main.IOUtils;
import cn.net.bhe.utils.main.JacksonUtils;

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
    
    public void init() {
        BigInteger count = nodeDao.nodeCount();
        if (count.compareTo(new BigInteger("0")) == 0) {
            InputStream is = NodeService.class.getClassLoader().getResourceAsStream("province.json");
            String content = IOUtils.streamToStr(is);
            JsonNode root = JacksonUtils.readTree(content);
            process(-1, root.get("provinces"));
        }
    }
    
    public void process(int pId, JsonNode node) {
        if (node == null) {
            return;
        }
        if (node.isObject() && node.get("name") != null && node.get("level") != null) {
            String name = node.get("name").asText();
            Integer type = node.get("level").asInt();
            Node saveNode = new Node(pId, type, name);
            Integer newId = (Integer) nodeDao.save(saveNode);
            process(newId, node.get("child"));
        } else if (node.isArray()) {
            ArrayNode array = (ArrayNode) node;
            for (int i = 0; i < array.size(); i++) {
                process(pId, array.get(i));
            }
        }
    }
    
}
