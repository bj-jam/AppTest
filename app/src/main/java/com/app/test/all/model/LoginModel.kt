package com.app.test.all.model

import android.databinding.BaseObservable
import com.app.test.extension.NonNullObservableField

/**
 *
 * @author lcx
 * Created at 2020.9.21
 * Describe:
 */
class LoginModel : BaseObservable() {
    val name = NonNullObservableField("")

}