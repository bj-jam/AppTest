package com.app.test.extension

import android.databinding.Observable
import android.databinding.ObservableField
import android.support.annotation.NonNull

class NonNullObservableField<T : Any>(
        value: T, vararg dependencies: Observable
) : ObservableField<T>(*dependencies) {
    init {
        set(value)
    }

    override fun get(): T = super.get()!!

    @Suppress("RedundantOverride")
    override fun set(@NonNull value: T) = super.set(value)
}