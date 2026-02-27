package com.rems.common.util;

public class Pagination {

    private int page;
    private int size;

    public Pagination(int page, int size) {
        this.page = page < 1 ? 1 : page;
        this.size = size < 1 ? 10 : size;
    }

    public int getOffset() {
        return (page - 1) * size;
    }

    public int getLimit() {
        return size;
    }
}