package com.app.test.mvvm.databinding.model

import com.app.test.mvvm.databinding.bean.CommentBean
import okhttp3.*
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * @author lcx
 * Created at 2020.9.2
 * Describe:
 */
object BeanModel {

    suspend fun getdata(): List<CommentBean?>? = suspendCoroutine {
        val url = "https://www.baidu.com"
        //1,创建okHttpClient对象
        val mOkHttpClient = OkHttpClient()
        // 构造Request->call->执行
        val request = Request.Builder() //                .headers(extraHeaders == null ? new Headers.Builder().build() : Headers.of(extraHeaders))//extraHeaders 是用户添加头
                .url(url)
                .build()
        val call = mOkHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                it.resume(loadImage1())
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                it.resume(loadImage())
            }
        })
    }

    private fun loadImage(): List<CommentBean?>? {
        val list = arrayListOf<CommentBean>()
        for (i in 0..19) {
            val dto = CommentBean()
            dto.content = i.toString() + "loadMore内容"
            dto.commentId = i.toString() + ""
            dto.userName = i.toString() + "loadMorename"
            list.add(dto)
        }
        return list
    }

    private fun loadImage1(): List<CommentBean?>? {
        val list = arrayListOf<CommentBean>()
        for (i in 0..19) {
            val dto = CommentBean()
            dto.content = i.toString() + "loadImage1内容"
            dto.commentId = i.toString() + ""
            dto.userName = i.toString() + "loadImage1name"
            list.add(dto)
        }
        return list
    }
}