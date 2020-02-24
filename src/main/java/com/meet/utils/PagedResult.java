package com.meet.utils;

import java.util.List;

/**
 * @Description: 封装分页后的数据格式
 */
public class PagedResult {

    private int page;            // 当前页码 从1开始
    private int number;            // 当前页码 从1开始
    private int total;            // 总页数
    private long records;        // 总记录数
    private List<?> rows;        // 每行显示的内容
    private int navigatePages = 5;
    boolean first;
    boolean last;
    boolean isHasNext;
    boolean isHasPrevious;
    int[] navigatepageNums;

    public int[] getNavigatepageNums() {
        int navigatepageNum[];
        int totalPages = getTotal();
        int num = getNumber();
        //当总页数小于或等于导航页码数时
        if (totalPages <= navigatePages) {
            navigatepageNum = new int[totalPages];
            for (int i = 0; i < totalPages; i++) {
                navigatepageNum[i] = i + 1;
            }
        } else { //当总页数大于导航页码数时
            navigatepageNum = new int[navigatePages];
            int startNum = num - navigatePages / 2;
            int endNum = num + navigatePages / 2;

            if (startNum < 1) {
                startNum = 1;
                //(最前navigatePages页
                for (int i = 0; i < navigatePages; i++) {
                    navigatepageNum[i] = startNum++;
                }
            } else if (endNum > totalPages) {
                endNum = totalPages;
                //最后navigatePages页
                for (int i = navigatePages - 1; i >= 0; i--) {
                    navigatepageNum[i] = endNum--;
                }
            } else {
                //所有中间页
                for (int i = 0; i < navigatePages; i++) {
                    navigatepageNum[i] = startNum++;
                }
            }
        }
        return navigatepageNum;
    }

    public boolean isHasNext() {
        return page != total;
    }

    public boolean isHasPrevious() {
        return page != 1;
    }

    public boolean isFirst() {
        return page == 1;
    }

    public boolean isLast() {
        return page == total;
    }

    public int getPage() {
        return page;
    }

    public int getNumber() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public long getRecords() {
        return records;
    }

    public void setRecords(long records) {
        this.records = records;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }

}
