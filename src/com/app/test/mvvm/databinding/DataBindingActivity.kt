package com.app.test.mvvm.databinding

import android.app.Activity
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.app.test.R
import com.app.test.mvvm.databinding.adapter.MyAdapter
import com.app.test.mvvm.databinding.bean.CommentBean
import com.app.test.mvvm.databinding.bean.DataBean
import com.app.test.mvvm.databinding.viewmodel.DataModel
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_data_binding.*
import java.util.*

/**
 * Created by jam on 2019-09-29.
 *
 * @describe
 */
class DataBindingActivity : Activity(), LifecycleOwner {

    override fun getLifecycle(): Lifecycle {
        return LifecycleRegistry(this)
    }

    val adapter: MyAdapter by lazy {
        MyAdapter(dataList).also {
            it.setOnLoadMoreListener({
                mDataModel.LoadImage()
                it.loadMoreComplete()
            }, data_recyclerView)
        }
    }
    internal var uuid: String? = null
    internal var courseId: String? = null

    private val dataList = ArrayList<CommentBean>()

    val mDataModel: DataModel by lazy {
        ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.application).create(DataModel::class.java)
                .also {

                }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_binding)
        initView()
        initDate()
        handleData()
    }

    private fun handleData() {
//        mDataModel.commentData.observe(this, object : Observer<DataBean<CommentBean>> {
//            override fun onChanged(t: DataBean<CommentBean>?) {
//
//            }
//
//        })
        mDataModel.commentData.observe(this, Observer<DataBean<List<CommentBean>>> {
            it?.also {
                if (it.isSuccess) {
                    it.getmData()?.let { it1 -> dataList.addAll(it1) }
                    adapter.setNewData(dataList)
                } else {
                    Toast.makeText(this, it.getmInfo(), Toast.LENGTH_LONG).show()
                }
            }

        })
    }

    private fun initView() {
        data_swipeRefreshLayout?.setOnRefreshListener {
            dataList?.clear()
            initDate()
            data_swipeRefreshLayout?.isRefreshing = false
        }
        data_recyclerView?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        data_recyclerView?.adapter = adapter
    }

    private fun initDate() {
        uuid = ""
        courseId = ""
        for (i in 0..19) {
            val dto = CommentBean()
            dto.content = i.toString() + "内容"
            dto.commentId = i.toString() + ""
            dto.userName = i.toString() + "name"
            dataList.add(dto)
        }
        adapter.setNewData(dataList)
        Observable.fromIterable(dataList).subscribe(object : io.reactivex.Observer<CommentBean> {
            override fun onComplete() {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onSubscribe(d: Disposable?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onNext(value: CommentBean?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onError(e: Throwable?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })

    }

    private fun loadMore() {

        adapter.setNewData(dataList)
    }
}
