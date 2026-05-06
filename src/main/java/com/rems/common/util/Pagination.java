package com.rems.common.util;

public class Pagination {

    private int page;
    private int size;
    private long totalItems;

    public Pagination(int page, int size) {
        this.page = page < 1 ? 1 : page;
        this.size = size < 1 ? 10 : size;
    }

    public Pagination(int page, int size, long totalItems) {
        this.page = page < 1 ? 1 : page;
        this.size = size < 1 ? 10 : size;
        this.totalItems = totalItems;
    }

    public int getOffset() {
        return (page - 1) * size;
    }

    public int getLimit() {
        return size;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page < 1 ? 1 : page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size < 1 ? 10 : size;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalPages() {
        return (int) Math.ceil((double) totalItems / size);
    }

    public boolean hasNextPage() {
        return page < getTotalPages();
    }

    public boolean hasPreviousPage() {
        return page > 1;
    }

    public int getNextPage() {
        return hasNextPage() ? page + 1 : page;
    }

    public int getPreviousPage() {
        return hasPreviousPage() ? page - 1 : 1;
    }

    public long getStartItem() {
        return totalItems == 0 ? 0 : ((page - 1) * size) + 1;
    }

    public long getEndItem() {
        return Math.min(page * size, totalItems);
    }
}
