package com.app.test.mvvm.databinding.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.test.mvvm.databinding.bean.CommentBean
import com.app.test.mvvm.databinding.bean.DataBean
import com.app.test.mvvm.databinding.model.BeanModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


/**
 * Created by jam on 2019-09-30.
 *
 * @describe
 */
class DataViewModel : ViewModel(), CoroutineScope by MainScope() {

    val commentData: MutableLiveData<DataBean<List<CommentBean?>?>> by lazy { MutableLiveData<DataBean<List<CommentBean?>?>>() }
    val smsCode: MutableLiveData<String> = MutableLiveData()

    fun loadImage() {
        launch {
            val data = BeanModel.getdata()
            commentData.value = DataBean(data, "", true)
        }
    }

    @ExperimentalCoroutinesApi
    fun getSmsCode(phone: String) {
        launch {
            flow {
                (60 downTo 0).forEach {
                    delay(1000)
                    emit("$it s")
                }
            }.flowOn(Dispatchers.Default)
                    .onStart {
                        // 倒计时开始 ，在这里可以让Button 禁止点击状态
                    }
                    .onCompletion {
                        // 倒计时结束 ，在这里可以让Button 恢复点击状态
                    }
                    .collect { it ->
                        // 在这里 更新LiveData 的值来显示到UI
                        smsCode.value = it
                    }

        }
    }

    override fun onCleared() {
        super.onCleared()
        cancel()
    }
}
