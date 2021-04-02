package com.app.ui.material.listener

import android.widget.ImageView
import com.google.android.material.tabs.TabLayout

interface LoadHeaderImagesListener {
    fun loadHeaderImages(imageView: ImageView?, tab: TabLayout.Tab?)
}