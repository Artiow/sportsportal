package ru.vldf.sportsportal.dto.pagination.filters.generic;

import ru.vldf.sportsportal.dto.generic.DataTransferObject;

public class PageDividerDTO implements DataTransferObject {

    private Integer pageSize;
    private Integer pageNum;


    public Integer getPageSize() {
        return pageSize;
    }

    public PageDividerDTO setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public PageDividerDTO setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
        return this;
    }
}
