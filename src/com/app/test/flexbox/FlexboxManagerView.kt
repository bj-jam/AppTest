package com.app.test.flexbox

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import com.app.test.R
import com.app.test.util.DensityUtil
import com.google.android.flexbox.*
import java.util.*

/**
 * @author lcx
 * Created at 2020.4.1
 * Describe:
 */
class FlexboxManagerView : FrameLayout {
    private var recyclerView: RecyclerView? = null
    private var tvPosition: TextView? = null
    private var adapter: ViewAdapter? = null

    constructor(context: Context?) : super(context) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    private val width = intArrayOf(  40, 43,45,48, 50, 55, 60, 65, 70,72, 75, 80, 85, 90)
    private val image = intArrayOf(
            R.drawable.ic_01,
            R.drawable.ic_02,
            R.drawable.ic_03,
            R.drawable.ic_04,
            R.drawable.ic_05,
            R.drawable.ic_06,
            R.drawable.ic_07,
            R.drawable.ic_08,
            R.drawable.ic_09,
            R.drawable.ic_10,
            R.drawable.ic_11,
            R.drawable.ic_12,
            R.drawable.ic_13,
            R.drawable.ic_14,
            R.drawable.ic_15,
            R.drawable.ic_16,
            R.drawable.ic_17,
            R.drawable.ic_18,
            R.drawable.ic_19,
            R.drawable.ic_20,
            R.drawable.ic_21,
            R.drawable.ic_22,
            R.drawable.ic_23,
            R.drawable.ic_24,
            R.drawable.ic_25,
            R.drawable.ic_26,
            R.drawable.ic_27,
            R.drawable.ic_28,
            R.drawable.ic_29,
            R.drawable.ic_30,
            R.drawable.ic_31,
            R.drawable.ic_32,
            R.drawable.ic_33,
            R.drawable.ic_34,
            R.drawable.ic_35,
            R.drawable.ic_36,
            R.drawable.ic_37,
            R.drawable.ic_38,
            R.drawable.ic_39,
            R.drawable.ic_40,
            R.drawable.ic_41,
            R.drawable.ic_42,
            R.drawable.ic_43,
            R.drawable.ic_44,
            R.drawable.ic_45,
            R.drawable.ic_46
    )

    private fun getList(): List<BoxBean>? {
        val random = Random()
        val list: MutableList<BoxBean> = ArrayList()
        for (i in 0..100) {
            val boxBean = BoxBean()
            val index = random.nextInt(width.size)
            val imageIndex = random.nextInt(image.size)
            boxBean.id = image[imageIndex]
            boxBean.with = DensityUtil.dp2px(width[index])
            list.add(boxBean)
        }
        return list
    }

    fun initView() {
        View.inflate(context, R.layout.view_flexbox_manager, this)
        recyclerView = findViewById(R.id.rv_list)
        tvPosition = findViewById(R.id.tv_position)
        tvPosition?.setOnClickListener(OnClickListener {
            recyclerView?.also {
                val index = Random().nextInt(100)
                Toast.makeText(context, index.toString(), Toast.LENGTH_SHORT).show()
                it.scrollToPosition(index)
            }
        })
        val layoutManager = FlexboxLayoutManager()
        layoutManager.flexWrap = FlexWrap.WRAP //设置是否换行
        layoutManager.flexDirection = FlexDirection.ROW // 设置主轴排列方式
        layoutManager.alignItems = AlignItems.CENTER
        layoutManager.justifyContent = JustifyContent.SPACE_BETWEEN
        recyclerView?.layoutManager = layoutManager
        adapter = ViewAdapter(getList())
        recyclerView?.adapter = adapter
        adapter?.setOnItemClickListener { adapter, view, position ->
            Toast.makeText(context, position.toString(), Toast.LENGTH_SHORT).show()
            view.visibility = View.INVISIBLE
        }
    }

//    private val imgs = arrayOf(
//            R.drawable.share_fb,
//            R.drawable.ic_game_gold_10,
//            R.drawable.icon_pic3,
//            R.drawable.share_weibo,
//            R.drawable.ic_number_six,
//            R.drawable.ic_game_gold_10,
//            R.drawable.ic_share_bonus_cat_my_bonus,
//            R.drawable.ic_game_gold_10,
//            R.drawable.ic_game_gold_10,
//            R.drawable.icon_pic3,
//            R.drawable.share_kongjian,
//            R.drawable.ic_game_gold_10,
//            R.drawable.icon_pic3,
//            R.drawable.share_pyq,
//            R.drawable.share_tw,
//            R.drawable.main_tab_home_selected,
//            R.drawable.icon_pic7,
//            R.drawable.ic_game_gold_02,
//            R.drawable.icon_pic3,
//            R.drawable.ic_game_gold_04,
//            R.drawable.icon_pic5,
//            R.drawable.main_tab_home_selected,
//            R.drawable.ic_game_gold_07,
//            R.drawable.icon_pic8,
//            R.drawable.ic_game_gold_09,
//            R.drawable.share_wechat,
//            R.drawable.share_weibo,
//            R.drawable.icon_pic5,
//            R.drawable.ic_number_six,
//            R.drawable.ic_game_gold_10,
//            R.drawable.ic_share_bonus_cat_my_bonus,
//            R.drawable.ic_game_gold_10,
//            R.drawable.lib_picture_icon,
//            R.drawable.rating_star_light,
//            R.drawable.icon_pic5,
//            R.drawable.idr,
//            R.drawable.ic_game_gold_10,
//            R.drawable.ic_game_progress_box_arrived,
//            R.drawable.lib_video_icon,
//            R.drawable.ic_game_gold_10,
//            R.drawable.ic_body_bmi,
//            R.drawable.icon_pic5,
//            R.drawable.ilovecheese_heart,
//            R.drawable.ic_game_fortune_bag,
//            R.drawable.ic_number_four,
//            R.drawable.ic_game_gold_10,
//            R.drawable.lib_pdf_icon,
//            R.drawable.share_weibo,
//            R.drawable.icon_pic5,
//            R.drawable.ic_mine_back,
//            R.drawable.ic_game_header_coin,
//            R.drawable.lib_excel_icon,
//            R.drawable.ic_game_gold_10,
//            R.drawable.ilovecheese_i,
//            R.drawable.icon_pic5,
//            R.drawable.ic_body_age,
//            R.drawable.ic_game_gold_10,
//            R.drawable.video_firstplay,
//            R.drawable.ic_game_hand,
//            R.drawable.ic_game_gold_11,
//            R.drawable.share_qq,
//            R.drawable.ic_game_gold_12,
//            R.drawable.ic_number_eight,
//            R.drawable.share_qq,
//            R.drawable.ic_game_gold_14,
//            R.drawable.ic_body_weight, R.drawable.icon_pic5,
//            R.drawable.ic_body_age,
//            R.drawable.ic_game_gold_10,
//            R.drawable.ic_home_calories,
//            R.drawable.ic_game_hand,
//            R.drawable.ic_game_gold_11,
//            R.drawable.share_qq,
//            R.drawable.ic_game_gold_12,
//            R.drawable.ic_number_eight,
//            R.drawable.ic_walk_hot_activity,
//            R.drawable.ic_walk_data,
//            R.drawable.ic_body_weight, R.drawable.icon_pic5,
//            R.drawable.ic_body_age,
//            R.drawable.ic_game_gold_10,
//            R.drawable.ic_home_distance,
//            R.drawable.ic_game_hand,
//            R.drawable.ic_game_gold_11,
//            R.drawable.ic_home_time,
//            R.drawable.ic_game_gold_12,
//            R.drawable.ic_number_eight,
//            R.drawable.share_qq,
//            R.drawable.ic_game_gold_14,
//            R.drawable.ic_body_weight, R.drawable.icon_pic5,
//            R.drawable.ic_body_age,
//            R.drawable.ic_game_gold_10,
//            R.drawable.video_firstplay,
//            R.drawable.ic_game_hand,
//            R.drawable.ic_game_gold_11,
//            R.drawable.ic_step_calories,
//            R.drawable.ic_game_gold_12,
//            R.drawable.ic_number_eight,
//            R.drawable.share_qq,
//            R.drawable.ic_game_gold_14,
//            R.drawable.ic_body_weight,
//            R.drawable.main_tab_mine_selected,
//            R.drawable.icon_pic5,
//            R.drawable.ic_game_gold_17,
//            R.drawable.share_weibo
//    )

}