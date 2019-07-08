package bhe.net.cn.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import bhe.net.cn.utils.DateUtils;

@Entity
@Table(name = "CS_WALLET_LOG")
public class WalletLog implements Serializable {

    private static final long serialVersionUID = 6700801779013081347L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "wallet_id")
    private Integer walletId;
    @Column(name = "operator_id")
    private Integer operatorId;
    private Date createtime;
    private String remark;

    public WalletLog() {

    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public String getCreatetime() {
        return DateUtils.format(createtime);
    }

    public String getRemark() {
        return remark;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getWalletId() {
        return walletId;
    }

    public void setWalletId(Integer walletId) {
        this.walletId = walletId;
    }

}
