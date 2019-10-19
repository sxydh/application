package cn.net.bhe.chargingsystem.app.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CS_USER_REF_NODE")
public class UserRefNode implements Serializable {

    private static final long serialVersionUID = -8303040470661063797L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "node_id")
    private Integer nodeId;
    private Date createtime;

    public UserRefNode() {

    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getNodeId() {
        return nodeId;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setNodeId(Integer nodeId) {
        this.nodeId = nodeId;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

}
