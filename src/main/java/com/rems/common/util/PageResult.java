package com.rems.common.util;

import java.util.List;

public class PageResult<T> {

    private List<T> data;
    private int page;
    private int size;
    private int total;
    private int totalPages;
    private int startItem;
    private int endItem;

    public PageResult(List<T> data, int page, int size, int total) {
        this.data = data;
        this.page = page;
        this.size = size;
        this.total = total;
        this.totalPages = (int) Math.ceil((double) total / size);
        
        // Calculate start and end items for display
        this.startItem = (page - 1) * size + 1;
        this.endItem = Math.min(page * size, total);
        if (this.endItem == 0) {
            this.startItem = 0;
        }
    }

    public List<T> getData() {
        return data;
    }
    public void setData(List<T> data) {
        this.data = data;
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
    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }
    public int getTotalPages() {
        return totalPages;
    }
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
    
    public int getTotalItems() {
        return total;
    }
    
    public int getStartItem() {
        return startItem;
    }
    
    public int getEndItem() {
        return endItem;
    }
    
    public boolean isHasPreviousPage() {
        return page > 1;
    }
    
    public int getPreviousPage() {
        return page - 1;
    }
    
    public boolean isHasNextPage() {
        return page < totalPages;
    }
    
    public int getNextPage() {
        return page + 1;
    }
}
