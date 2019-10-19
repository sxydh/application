package cn.net.bhe.chargingsystem.common.base;

import java.util.List;

public class Page {

    private Integer count;
    private List<?> list;

    public Integer getCount() {
        return count;
    }

    public List<?> getList() {
        return list;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}
