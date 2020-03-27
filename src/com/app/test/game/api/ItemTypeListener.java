package com.app.test.game.api;

public interface ItemTypeListener<T> {
    void onItemType(int index, T t, Object obj);
}