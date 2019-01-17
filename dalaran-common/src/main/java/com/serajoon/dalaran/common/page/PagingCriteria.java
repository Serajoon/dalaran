package com.serajoon.dalaran.common.page;

import java.io.Serializable;

public abstract class PagingCriteria implements Serializable {
    public Integer pageNum;
    public Integer pageSize;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
