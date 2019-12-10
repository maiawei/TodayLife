package com.ww.todaylife.bean.eventBean;

/**
 * created by wang.wei on 2019-12-09
 */
public class UpdateNewsItem {
    public String htmlStr;
    public String typeCode;
    public boolean isStar;
    public UpdateNewsItem(String typeCode,String htmlStr,boolean isStar) {
        this.typeCode=typeCode;
        this.htmlStr = htmlStr;
        this.isStar=isStar;
    }

}
