package com.app.test.game.bean

import android.os.Parcelable
import com.app.test.util.StringUtils
import kotlinx.android.parcel.Parcelize

@Parcelize
open class SuperType(var title: String? = null,
                     var shortTitle: String? = null,
                     var isSelected: Boolean = false,
                     var index: Int = 0) : Parcelable {


    override fun toString(): String {
        return "SuperType{" +
                ", title='" + title + '\'' +
                ", selected=" + isSelected +
                '}'
    }

    protected fun isEmpty(str: String?): Boolean {
        return StringUtils.isEmpty(str)
    }

    protected fun trimToEmpty(str: String?): Boolean {
        return StringUtils.isEmpty(StringUtils.trimToEmpty(str))
    }

}