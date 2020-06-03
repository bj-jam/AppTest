package com.app.test.game.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 干扰
 */
@Parcelize
class IdiomDisturbWord(var proverbCharacter: IdiomWord? = null,
                       var proverbDisturbWord: SuperType? = null,
                       var proverbDisturbIndex: Int = 0,
                       var disturb: String? = null) : Parcelable {


}