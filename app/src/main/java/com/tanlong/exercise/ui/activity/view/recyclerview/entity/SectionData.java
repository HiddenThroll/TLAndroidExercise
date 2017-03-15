package com.tanlong.exercise.ui.activity.view.recyclerview.entity;

/**
 * Created by 龙 on 2017/3/15.
 */

public class SectionData<T> {
    private boolean isHeader;
    private int headerIndex;//用于索引ABC...的index定位
    private T data;
    private String header;

    public SectionData(boolean isHeader, int headerIndex, String header) {
        this.isHeader = isHeader;
        this.header = header;
        this.headerIndex = headerIndex;
        this.data = null;
    }

    public SectionData(T t) {
        this.isHeader = false;
        this.header = null;
        this.data = t;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public int getHeaderIndex() {
        return headerIndex;
    }

    public void setHeaderIndex(int headerIndex) {
        this.headerIndex = headerIndex;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}
