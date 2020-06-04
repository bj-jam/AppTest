package com.app.test.redenveloped

import android.os.Parcelable
import com.app.test.util.Utils
import kotlinx.android.parcel.Parcelize

@Parcelize
class RedEnvelopes(var index: Int = 0,
                   var redPacketId: String? = null,
                   var currentX: Int = 0,
                   var currentY: Int = 0,
                   var rotate: Int = 0,
                   var money: Int = 0,
                   var redPacketPath: String? = null,
                   var resId: Int = -1) : Parcelable {

    fun hasRedEnvelopes(): Boolean {
        return resId > 0 || !Utils.trimToEmpty(redPacketPath)
    }
}