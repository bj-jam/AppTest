package com.app.test.flexbox

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.app.test.R
import com.app.test.util.DensityUtil
import com.app.test.util.Utils
import com.bumptech.glide.Glide
import java.util.*

/**
 * @author lcx
 * Created at 2020.4.17
 * Describe:
 */
class BoxView : FrameLayout {
    private var fitRectPack: FitRectPack? = null
    private var maxHeight = 0
    private var dp50 = 0
    private var dp60 = 0
    private var screenWidth = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        dp50 = DensityUtil.dp2px(50)
        dp60 = DensityUtil.dp2px(60)
        screenWidth = DensityUtil.getScreenWidth(context)
    }

    private val image = intArrayOf(
            R.drawable.ic_01, R.drawable.ic_02, R.drawable.ic_03, R.drawable.ic_04,
            R.drawable.ic_05, R.drawable.ic_06, R.drawable.ic_07, R.drawable.ic_08,
            R.drawable.ic_09, R.drawable.ic_10, R.drawable.ic_11, R.drawable.ic_12,
            R.drawable.ic_13, R.drawable.ic_14, R.drawable.ic_15, R.drawable.ic_16,
            R.drawable.ic_17, R.drawable.ic_18, R.drawable.ic_19, R.drawable.ic_20,
            R.drawable.ic_21, R.drawable.ic_22, R.drawable.ic_23, R.drawable.ic_24,
            R.drawable.ic_25, R.drawable.ic_26, R.drawable.ic_27, R.drawable.ic_28,
            R.drawable.ic_29, R.drawable.ic_30, R.drawable.ic_31, R.drawable.ic_32,
            R.drawable.ic_33, R.drawable.ic_34, R.drawable.ic_35, R.drawable.ic_36,
            R.drawable.ic_37, R.drawable.ic_38, R.drawable.ic_39, R.drawable.ic_40,
            R.drawable.ic_41, R.drawable.ic_42, R.drawable.ic_43, R.drawable.ic_44,
            R.drawable.ic_45, R.drawable.ic_46
    )

    private val list: List<BoxBean?>
        get() {
            val random = Random()
            val list: MutableList<BoxBean?> = ArrayList()
            for (i in 0..149) {
                val boxBean = BoxBean()
                val imageIndex = random.nextInt(image.size)
                boxBean.id = image[imageIndex]
                list.add(boxBean)
            }
            return list
        }

    fun setData() {
        initRect(list)
    }

    private fun initRect(dataList: List<BoxBean?>) {
        if (Utils.isEmpty(dataList)) {
            return
        }
        val size = dataList.size
        val random = Random()
        val allArea = (dp50 + DensityUtil.dp2px(25)) * (dp50 + DensityUtil.dp2px(25)) * size
        fitRectPack = FitRectPack(screenWidth - 20, allArea / screenWidth, false)
        for (i in 0 until size) {
            val imageWidth = dp50 + DensityUtil.dp2px(random.nextInt(30))
            fitRectPack?.insert(imageWidth, imageWidth, FitRectPack.FreeRectangleChoiceHeuristic.BestAreaFit)
        }
        for (i in 0 until size) {
            val imageId = dataList[i]?.id
            val view = View.inflate(context, R.layout.item_box_view, null)
            //view偏移的角度
            val angle = random.nextInt(41) - 20
            val showItem = view.findViewById<ImageView>(R.id.iv_box_item)
            val itemStatus = view.findViewById<ImageView>(R.id.iv_box_item_status)
            view.rotation = angle.toFloat()
            view.rotationX = 0.5f
            view.rotationY = 0.5f
            itemStatus.rotation = -angle.toFloat()
            itemStatus.rotationX = 0.5f
            itemStatus.rotationY = 0.5f
            //这样取的话数组会出现越界的情况
            val rect = fitRectPack?.usedRectangles?.get(i)
            rect?.also {
                //设置单个view的大小
                var params = LayoutParams(it.width, it.height)
                showItem.layoutParams = params
                //设置状态view的大小和位置
                params = LayoutParams(if (it.width > dp60) dp60 else it.width, if (it.height > dp60) dp60 else it.height)
                params.gravity = Gravity.BOTTOM or Gravity.RIGHT
                itemStatus.layoutParams = params
                //加载资源
                Glide.with(context).load(imageId).asBitmap().into(showItem)
                //设置整个view在父容器中的位置
                val itemParams = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                view.x = it.x + 10.toFloat()
                view.y = it.y.toFloat()
                view.layoutParams = itemParams
                addView(view)
                maxHeight = Math.max(maxHeight, it.y)
            }
        }
        val layoutParams: ViewGroup.LayoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, maxHeight + dp50 + dp50)
        Log.e("jam", "initRect: " + (maxHeight + dp50 + dp50))
        setLayoutParams(layoutParams)
    }
}