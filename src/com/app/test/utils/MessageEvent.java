package com.app.test.utils;

/**
 * Created by jam on 2017/9/5.
 */

public class MessageEvent<T> {
    private T data;
    private String eventCode;

    public MessageEvent(String eventCode) {
        this(eventCode, null);
    }

    public MessageEvent(String eventCode, T data) {
        this.eventCode = eventCode;
        this.data = data;
    }

    public String getEventCode() {
        return this.eventCode;
    }

    public T getData() {
        return this.data;
    }
}
