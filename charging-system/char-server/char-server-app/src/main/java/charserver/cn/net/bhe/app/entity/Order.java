package charserver.cn.net.bhe.app.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CS_ORDER")
public class Order implements Serializable {

    private static final long serialVersionUID = 3043141678316396470L;
    @Id
    private String id;
    @Column(name = "wallet_id")
    private Integer walletId;
    private Integer type;
    private Integer amount;
    private Integer status;
    private Date createtime;
    private Date updatetime;
    private Integer credit;
    private Integer balance;
    @Column(name = "operator_id")
    private Integer operatorId;
    @Column(name = "greenpay_id")
    private String greenpayId;

    public Order() {

    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public Integer getType() {
        return type;
    }

    public Integer getAmount() {
        return amount;
    }

    public Integer getStatus() {
        return status;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public Integer getCredit() {
        return credit;
    }

    public Integer getBalance() {
        return balance;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public String getGreenpayId() {
        return greenpayId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public void setGreenpayId(String greenpayId) {
        this.greenpayId = greenpayId;
    }

    public Integer getWalletId() {
        return walletId;
    }

    public void setWalletId(Integer walletId) {
        this.walletId = walletId;
    }

}
