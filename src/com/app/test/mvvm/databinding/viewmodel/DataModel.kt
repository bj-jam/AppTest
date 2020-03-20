package com.app.test.mvvm.databinding.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData

import com.app.test.mvvm.databinding.bean.CommentBean
import com.app.test.mvvm.databinding.bean.DataBean


/**
 * Created by jam on 2019-09-30.
 *
 * @describe
 */
class DataModel(application: Application) : AndroidViewModel(application) {

    private val mCommentData: MutableLiveData<DataBean<List<CommentBean>>> by lazy { MutableLiveData<DataBean<List<CommentBean>>>() }


    val commentData: MutableLiveData<DataBean<List<CommentBean>>>
        get() {
            return mCommentData
        }

    fun LoadImage() {
        for (i in 0..19) {
            val dto = CommentBean()
            dto.content = i.toString() + "loadMore内容"
            dto.commentId = i.toString() + ""
            dto.userName = i.toString() + "loadMorename"

        }
    }
}
