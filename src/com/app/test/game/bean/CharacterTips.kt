package com.app.test.game.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 成语提示
 */
@Parcelize
class CharacterTips(var title: String? = null,
                    var shortTitle: String? = null) : Parcelable {

}