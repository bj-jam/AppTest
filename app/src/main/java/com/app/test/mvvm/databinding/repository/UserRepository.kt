package com.app.test.mvvm.databinding.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.app.test.mvvm.databinding.bean.CommentResponse

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by 86839 on 2017/10/6.
 */

class UserRepository private constructor() {

    private val userApi = RetrofitFactory.getInstance().create(UserApi::class.java)

    fun getUser(username: String): LiveData<CommentResponse> {
        val user = MutableLiveData<CommentResponse>()
        userApi.queryUserByUsername(username)
                .enqueue(object : Callback<CommentResponse> {
                    override fun onResponse(call: Call<CommentResponse>, response: Response<CommentResponse>) {
                        user.value = response.body()
                    }

                    override fun onFailure(call: Call<CommentResponse>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
        return user
    }

    companion object {
        val instance = UserRepository()
    }
}
