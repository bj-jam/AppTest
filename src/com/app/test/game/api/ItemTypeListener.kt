package com.app.test.game.api

interface ItemTypeListener<T> {
    fun onItemType(index: Int, t: T, obj: Any?)
}