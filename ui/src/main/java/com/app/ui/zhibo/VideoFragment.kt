package com.app.ui.zhibo

import android.media.MediaPlayer.OnCompletionListener
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import androidx.fragment.app.Fragment
import com.app.ui.R

/**
 * 播放页面
 */
class VideoFragment : Fragment() {
    private var video_view: VideoView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_live, null)
        video_view = view.findViewById(R.id.video_view)
        video_view?.visibility = View.VISIBLE
        video_view?.setVideoURI(Uri.parse("android.resource://" + activity!!.packageName + "/" + R.raw.video_1))
        video_view?.start()
        video_view?.setOnCompletionListener(OnCompletionListener {
            video_view?.setVideoURI(Uri.parse("android.resource://" + activity!!.packageName + "/" + R.raw.video_1))
            video_view?.start()
        })
        val s = arrayOf("DD")
        return view
    }

    companion object {
        //二分查找法，必须有序
        fun halfSeach_2(arr: IntArray, key: Int): Int {
            var min: Int
            var max: Int
            var mid: Int
            min = 0
            max = arr.size - 1
            mid = max + min shr 1 //(max+min)/2;
            while (arr[mid] != key) {
                if (key > arr[mid]) {
                    min = mid + 1
                } else if (key < arr[mid]) max = mid - 1
                if (max < min) return -1
                mid = max + min shr 1
            }
            return mid
        }
    }
}