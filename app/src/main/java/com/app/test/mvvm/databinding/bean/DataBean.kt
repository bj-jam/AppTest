package com.app.test.mvvm.databinding.bean

/**
 * Created by jam on 2019-09-30.
 *
 * @describe
 */
class DataBean<T> {
    private var mData: T? = null
    private var mInfo: String? = null
    var isSuccess: Boolean = false

    constructor() {}

    constructor(data: T, info: String, status: Boolean) {
        mData = data
        mInfo = info
        isSuccess = status
    }

    fun getmData(): T? {
        return mData
    }

    fun setmData(mData: T) {
        this.mData = mData
    }

    fun getmInfo(): String? {
        return mInfo
    }

    fun setmInfo(mInfo: String) {
        this.mInfo = mInfo
    }
}
