package com.app.test.game.bean;

import java.io.Serializable;

public class LogHelper implements Serializable {
    public int retrycount;
    public int clicks;
    public int clicktips;
    public long startIntoTime;
    public long endTime;
    public long costtime;

    /*开始填字记录时间*/
    public long startSelectWordTime;
    /*填对成语时的结束时间*/
    public long selectWordRightTime;

    private StringBuilder stringBuilder;

    public LogHelper() {
        stringBuilder=new StringBuilder();
    }
    public void appendRightTime(String str){
        stringBuilder.append(str);
    }

    public String getStringBuilder() {
        if(stringBuilder==null){
            return "";
        }
        int lastIndexOf = stringBuilder.lastIndexOf(",");
        if (lastIndexOf != -1 && lastIndexOf == stringBuilder.length() - 1) {
            stringBuilder.deleteCharAt(lastIndexOf);
        }
        return stringBuilder.toString();
    }
}
