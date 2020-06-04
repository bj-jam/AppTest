package com.app.test.main

import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import okhttp3.*
import java.io.IOException

/**
 */
fun main(args: Array<String>) {
    creat()

}

fun creat() {
    Observable.create(ObservableOnSubscribe<String> { e ->
        e?.onNext("hhaha ")
        e?.onComplete()
    }).subscribe(object : Observer<String> {
        override fun onSubscribe(d: Disposable?) {
        }

        override fun onNext(value: String?) {
            println(value)
        }

        override fun onError(e: Throwable?) {
        }

        override fun onComplete() {
            println("onComplete")
        }

    })
}

fun just() {
    Observable.just(1, 2).subscribe(object : Observer<Int> {
        override fun onComplete() {
        }

        override fun onSubscribe(d: Disposable?) {
        }

        override fun onNext(value: Int?) {
            println(value)

        }

        override fun onError(e: Throwable?) {

        }

    })
}

fun fromArray() {
    val array = arrayOf(1, 2, 3, 4)

}

fun okhttp() {
    val ok = OkHttpClient.Builder().build()
    val request = Request.Builder().url("").build()
    val call = ok.newCall(request)
    call.enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
//            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onResponse(call: Call, response: Response) {
//            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    })
    call.execute()
}