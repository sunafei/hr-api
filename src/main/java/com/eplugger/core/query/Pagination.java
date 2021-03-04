package com.eplugger.core.query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.AbstractPageRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * 分页对象
 * creat_user: AFei Sun
 * creat_date: 2018/4/23
 **/
public class Pagination extends AbstractPageRequest {
    private int page;
    private int size;
    private String orderName;
    private int orderType = 0;//排序类型 0:asc,1:desc
    private Sort sort;

    public static Pagination init() {
        return new Pagination(1,2,"", 2);
    }

    public Pagination(int page, int size, String orderName, int orderType) {
        super(page, size);
        this.orderName = orderName;
        this.orderType = orderType;
    }

    public Sort getSort() {
        if (StringUtils.isNotBlank(orderName)) {
            return new Sort(orderType == 0 ? Sort.Direction.ASC : Sort.Direction.DESC, orderName);
        }
        return null;
    }

    public Pageable next() {
        return new PageRequest(this.getPageNumber() + 1, this.getPageSize(), this.getSort());
    }

    public Pagination previous() {
        return this.getPageNumber() == 0 ? this : new Pagination(this.getPageNumber() - 1, this.getPageSize(), this.orderName, this.orderType);
    }

    public Pageable first() {
        return new PageRequest(0, this.getPageSize(), this.getSort());
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }
}
