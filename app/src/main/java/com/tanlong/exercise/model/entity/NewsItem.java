package com.tanlong.exercise.model.entity;

import static com.baidu.location.g.h.S;

/**
 * Created by 龙 on 2017/3/2.
 */

public class NewsItem {
    /**
     * 新闻类型
     * 1 -- 标题 + 大图形式
     * 2 -- 左侧图片 + 右侧标题 + 描述字段
     * 3 -- 标题 + 3副图片
     */
    private int type;
    private String title;
    private String summary;
    private String content;
    private String time;
    private String titleImgUrl;
    private String summaryImgUrl;
    private String contentImgUrl_1;
    private String contentImgUrl_2;
    private String contentImgUrl_3;

    public static final int NEWS_TYPE_1 = 1;//标题 + 大图形式
    public static final int NEWS_TYPE_2 = 2;//左侧图片 + 右侧标题 + 描述字段
    public static final int NEWS_TYPE_3 = 3;//标题 + 3副图片

    public NewsItem(String title, String titleImgUrl) {
        this.type = NEWS_TYPE_1;
        this.title = title;
        this.titleImgUrl = titleImgUrl;
    }

    public NewsItem(String title, String summary, String summaryImgUrl) {
        this.type = NEWS_TYPE_2;
        this.title = title;
        this.summary = summary;
        this.summaryImgUrl = summaryImgUrl;
    }

    public NewsItem(String title, String contentImgUrl_1, String contentImgUrl_2, String contentImgUrl_3) {
        this.type = NEWS_TYPE_3;
        this.title = title;
        this.contentImgUrl_1 = contentImgUrl_1;
        this.contentImgUrl_2 = contentImgUrl_2;
        this.contentImgUrl_3 = contentImgUrl_3;
    }

    public NewsItem(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitleImgUrl() {
        return titleImgUrl;
    }

    public void setTitleImgUrl(String titleImgUrl) {
        this.titleImgUrl = titleImgUrl;
    }

    public String getSummaryImgUrl() {
        return summaryImgUrl;
    }

    public void setSummaryImgUrl(String summaryImgUrl) {
        this.summaryImgUrl = summaryImgUrl;
    }

    public String getContentImgUrl_1() {
        return contentImgUrl_1;
    }

    public void setContentImgUrl_1(String contentImgUrl_1) {
        this.contentImgUrl_1 = contentImgUrl_1;
    }

    public String getContentImgUrl_2() {
        return contentImgUrl_2;
    }

    public void setContentImgUrl_2(String contentImgUrl_2) {
        this.contentImgUrl_2 = contentImgUrl_2;
    }

    public String getContentImgUrl_3() {
        return contentImgUrl_3;
    }

    public void setContentImgUrl_3(String contentImgUrl_3) {
        this.contentImgUrl_3 = contentImgUrl_3;
    }
}
