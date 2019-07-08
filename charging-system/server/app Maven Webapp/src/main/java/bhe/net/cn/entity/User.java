package bhe.net.cn.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

import bhe.net.cn.utils.DateUtils;

@Entity
@Table(name = "CS_USER")
public class User implements Serializable {

    private static final long serialVersionUID = 8612212705002183193L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String phone;
    private String name;
    private String password;
    private Integer sex;
    private Integer age;
    private String address;
    private Integer type;
    private Integer role;
    private Integer status;
    private Date updatetime;
    private Date createtime;
    private String ip;

    public User() {

    }

    public User(Map<String, Object> map) {
        init(map);
    }

    public void init(Map<String, Object> map) {
        if (StringUtils.isNotEmpty((String) map.get("password"))) {
            this.password = (String) map.get("password");
        }
        if (StringUtils.isNotEmpty((String) map.get("name"))) {
            this.name = (String) map.get("name");
        }
        if (map.get("sex") != null) {
            this.sex = (Integer) map.get("sex");
        }
        if (map.get("age") != null) {
            this.age = (Integer) map.get("age");
        }
        if (StringUtils.isNotEmpty((String) map.get("address"))) {
            this.address = (String) map.get("address");
        }
        this.type = 2;
        this.role = 1;
        this.updatetime = new Date();
        if (StringUtils.isNotEmpty((String) map.get("ip"))) {
            this.ip = (String) map.get("ip");
        }
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public Integer getSex() {
        return sex;
    }

    public Integer getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public Integer getType() {
        return type;
    }

    public Integer getRole() {
        return role;
    }

    public Integer getStatus() {
        return status;
    }

    public String getUpdatetime() {
        return DateUtils.format(updatetime);
    }

    public String getCreatetime() {
        return DateUtils.format(createtime);
    }

    public String getIp() {
        return ip;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
