package com.app.test.game.bean

import android.graphics.Point
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class IdiomFillState(var isAllFill: Boolean = false,
                          var isAllRight: Boolean = false,
                          var rightCount: Int = 0,
                          var position: Int = 0,
                          var needFillCount: Int = 0,
                          var point: Point = Point(-1, -1)) : Parcelable {

}