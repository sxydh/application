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
@Table(name = "CS_WALLET")
public class Wallet implements Serializable {

    private static final long serialVersionUID = -580908332117938726L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String num = MathUtils.randomNum(17);
    @Column(name = "user_id")
    private Integer userId;
    private Integer credit = 0;
    private Integer balance = 0;
    private Date updatetime = new Date();
    private Date createtime = new Date();

    public Wallet() {

    }
    
    public Wallet(int userId) {
        this.userId = userId;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getCredit() {
        return credit;
    }

    public Integer getBalance() {
        return balance;
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

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
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

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

}
