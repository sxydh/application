package charserver.cn.net.bhe.app.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.net.bhe.utils.main.DateUtils;
import cn.net.bhe.utils.main.MathUtils;

@Entity
@Table(name = "CS_WATER")
public class Water implements Serializable {

    private static final long serialVersionUID = -56278712413452954L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "eqm_num")
    private String eqmNum = MathUtils.randomNum(11);
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "old_value")
    private Integer oldValue = 0;
    @Column(name = "new_value")
    private Integer newValue = 0;
    private Date updatetime = new Date();
    private Date createtime = new Date();

    public Water() {

    }
    
    public Water(int userId){
        this.userId = userId;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getOldValue() {
        return oldValue;
    }

    public Integer getNewValue() {
        return newValue;
    }

    public String getUpdatetime() {
        return DateUtils.format(updatetime);
    }

    public String getCreatetime() {
        return DateUtils.format(createtime);
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setOldValue(Integer oldValue) {
        this.oldValue = oldValue;
    }

    public void setNewValue(Integer newValue) {
        this.newValue = newValue;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getId() {
        return id;
    }

    public String getEqmNum() {
        return eqmNum;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setEqmNum(String eqmNum) {
        this.eqmNum = eqmNum;
    }

}
