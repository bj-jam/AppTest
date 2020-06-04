package com.app.test.mvvm.databinding.repository


import com.app.test.mvvm.databinding.bean.CommentResponse

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


/**
 * Created by 86839 on 2017/10/6.
 */

interface UserApi {
    @GET("/users/{username}")
    fun queryUserByUsername(@Path("username") username: String): Call<CommentResponse>
}
