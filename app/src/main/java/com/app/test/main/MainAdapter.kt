package com.app.test.main

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.app.test.R
import com.app.test.anim.AnimActivity
import com.app.test.appinfo.AppInfoActivity
import com.app.test.bezier.NewUiActivity
import com.app.test.bubble.BubbleActivity
import com.app.test.camera.CameraActivity
import com.app.test.cardview.CardViewActivity
import com.app.test.circle.CircleActivity
import com.app.test.circle.anim.color.ColorCircleActivity
import com.app.test.circularprogress.CircularProgressActivity
import com.app.test.citypicker.CityPickerActivity
import com.app.test.column.ColumnActivity
import com.app.test.date.DateActivity
import com.app.test.edittext.PasswordActivity
import com.app.test.expandablelist.ExpandableListViewActivity
import com.app.test.flexbox.FlexboxActivity
import com.app.test.floatbutton.FloatButtonActivity
import com.app.test.game.ui.AnswerActivity
import com.app.test.glide.img.GlideLoadImgAcitvity
import com.app.test.hook.ui.TargetActivity
import com.app.test.jbox.JBoxActivity
import com.app.test.keyboard.KeyboardActivity
import com.app.test.likesougou.LikeSouGouActivity
import com.app.test.line.LineActivity
import com.app.test.listview.LiseViewActivity
import com.app.test.loading.LoadingActivity
import com.app.test.lottie.LottieActivity
import com.app.test.matrix.MatrixActivity
import com.app.test.mirrorimage.MirrorImageActivity
import com.app.test.move.MoveActivity
import com.app.test.mvvm.databinding.DataBindingActivity
import com.app.test.notice.NoticeActivity
import com.app.test.numberprogressbar.NumberProgressActivity
import com.app.test.paint.PaintActivity
import com.app.test.path.PathActivity
import com.app.test.pickcrop.PickCropActivity
import com.app.test.radar.RadarActivity
import com.app.test.recyclerview.Damping1Activity
import com.app.test.recyclerview.DampingActivity
import com.app.test.recyclerview.RecyclerViewActivity
import com.app.test.redenveloped.RedEnvelopesActivity
import com.app.test.ring.anim.CircleAnimActivity
import com.app.test.ring.anim.CircleMenuActivity
import com.app.test.ring.anim.color.RingAnimColorActivity
import com.app.test.scrollview.ScrollViewActivity
import com.app.test.service.ServiceActivity
import com.app.test.shadow.ShadowActivity
import com.app.test.showimage.ShowIamgeActivity
import com.app.test.smartrefresh.SmartRefreshActivity
import com.app.test.sortlist.SortListActivity
import com.app.test.spannable.SpannableActivity
import com.app.test.timelytextview.TimelyActivity
import com.app.test.titanic.TitanicActivity
import com.app.test.transition.activity.TransitionActivity
import com.app.test.viewflipper.ViewFlipperActivity
import com.app.test.viewpager.ViewPagerActivity
import com.app.test.wave.WaveActivity
import com.app.test.webview.WebViewActivity
import com.app.test.williamchart.williamchartdemo.CharActivity

class MainAdapter(private val onLister: OnLister) : BaseAdapter() {
    private val texts: MutableList<Int> = ArrayList()
    private val clzs: ArrayList<Class<*>> = ArrayList()

    override fun getCount(): Int {
        return texts.size
    }

    override fun getItem(position: Int): Any {
        return texts[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View? {
        var v = view
        val holder: Holder?
        if (v == null) {
            v = View.inflate(parent?.context, R.layout.view_main_item, null)
            holder = Holder()
            holder.textView = v.findViewById<View>(R.id.textView) as TextView
            v.tag = holder
        } else {
            holder = v.tag as Holder
        }
        holder.textView?.text = parent?.context?.resources?.getString(texts[position])
        v?.setOnClickListener {
            parent?.context?.startActivity(
                    Intent(parent?.context, clzs[position]))
        }
        return v
    }

    private inner class Holder {
        var textView: TextView? = null
    }

    private fun setData() {
        texts.add(R.string.date_activity)
        texts.add(R.string.circle_activity)
        texts.add(R.string.line_activity)
        texts.add(R.string.expandablelist_activity)
        texts.add(R.string.showimage_activity)
        texts.add(R.string.circle_anim_activity)
        texts.add(R.string.column_activity)
        texts.add(R.string.mirror_activity)
        texts.add(R.string.ColorCircleActivity)
        texts.add(R.string.ColorRingActivity)
        texts.add(R.string.glide_load_activity)
        texts.add(R.string.viewpager_activity)
        texts.add(R.string.download_activity)
        texts.add(R.string.shadow_activity)
        texts.add(R.string.camera_activity)
        texts.add(R.string.notice_activity)
        texts.add(R.string.recycler_activity)
        texts.add(R.string.wave_activity)
        texts.add(R.string.webview_activity)
        texts.add(R.string.cardView_activity)
        texts.add(R.string.list_view_activity)
        texts.add(R.string.keyboard_activity)
        texts.add(R.string.anim_activity)
        texts.add(R.string.damping_activity)
        texts.add(R.string.damping1_activity)
        texts.add(R.string.transition_activity)
        texts.add(R.string.data_binding_activity)
        texts.add(R.string.smart_refresh_activity)
        texts.add(R.string.move_activity)
        texts.add(R.string.lottie_activity)
        texts.add(R.string.red_envelopes_activity)
        texts.add(R.string.new_ui_activity)
        texts.add(R.string.float_button_activity)
        texts.add(R.string.circular_progress_activity)
        texts.add(R.string.number_progress_activity)
        texts.add(R.string.titanic_activity)
        texts.add(R.string.scroll_view_activity)
        texts.add(R.string.Circle_Menu_Activity)
        texts.add(R.string.SortListActivity)
        texts.add(R.string.SpannableActivity)
        texts.add(R.string.MatrixActivity)
        texts.add(R.string.ViewFlipperActivity)
        texts.add(R.string.TimelyActivity)
        texts.add(R.string.TargetActivity)
        texts.add(R.string.RadarActivity)
        texts.add(R.string.LoadingActivity)
        texts.add(R.string.BubbleActivity)
        texts.add(R.string.LikeSouGouActivity)
        texts.add(R.string.GameReviewModeActivity)
        texts.add(R.string.PathActivity)
        texts.add(R.string.FlexboxActivity)
        texts.add(R.string.JBoxActivity)
        texts.add(R.string.PaintActivity)
        texts.add(R.string.PickCropActivity)
        texts.add(R.string.CharActivity)
        texts.add(R.string.CityPickerActivity)
        texts.add(R.string.PasswordActivity)
        texts.add(R.string.AppInfoActivity)


        clzs.add(DateActivity::class.java) //
        clzs.add(CircleActivity::class.java) //
        clzs.add(LineActivity::class.java) //
        clzs.add(ExpandableListViewActivity::class.java) //
        clzs.add(ShowIamgeActivity::class.java) //
        clzs.add(CircleAnimActivity::class.java) //
        clzs.add(ColumnActivity::class.java) //
        clzs.add(MirrorImageActivity::class.java) //
        clzs.add(ColorCircleActivity::class.java) //
        clzs.add(RingAnimColorActivity::class.java) //
        clzs.add(GlideLoadImgAcitvity::class.java) //
        clzs.add(ViewPagerActivity::class.java) //
        clzs.add(ServiceActivity::class.java) //
        clzs.add(ShadowActivity::class.java) //
        clzs.add(CameraActivity::class.java) //
        clzs.add(NoticeActivity::class.java) //
        clzs.add(RecyclerViewActivity::class.java) //
        clzs.add(WaveActivity::class.java) //
        clzs.add(WebViewActivity::class.java) //
        clzs.add(CardViewActivity::class.java) //
        clzs.add(LiseViewActivity::class.java) //
        clzs.add(KeyboardActivity::class.java) //
        clzs.add(AnimActivity::class.java) //
        clzs.add(DampingActivity::class.java) //
        clzs.add(Damping1Activity::class.java) //
        clzs.add(TransitionActivity::class.java) //
        clzs.add(DataBindingActivity::class.java) //
        clzs.add(SmartRefreshActivity::class.java) //
        clzs.add(MoveActivity::class.java) //
        clzs.add(LottieActivity::class.java) //
        clzs.add(RedEnvelopesActivity::class.java) //
        clzs.add(NewUiActivity::class.java) //
        clzs.add(FloatButtonActivity::class.java) //
        clzs.add(CircularProgressActivity::class.java) //
        clzs.add(NumberProgressActivity::class.java) //
        clzs.add(TitanicActivity::class.java) //
        clzs.add(ScrollViewActivity::class.java) //
        clzs.add(CircleMenuActivity::class.java) //
        clzs.add(SortListActivity::class.java) //
        clzs.add(SpannableActivity::class.java) //
        clzs.add(MatrixActivity::class.java) //
        clzs.add(ViewFlipperActivity::class.java) //
        clzs.add(TimelyActivity::class.java) //
        clzs.add(TargetActivity::class.java) //
        clzs.add(RadarActivity::class.java) //
        clzs.add(LoadingActivity::class.java) //
        clzs.add(BubbleActivity::class.java) //
        clzs.add(LikeSouGouActivity::class.java) //
        clzs.add(AnswerActivity::class.java) //
        clzs.add(PathActivity::class.java) //
        clzs.add(FlexboxActivity::class.java) //
        clzs.add(JBoxActivity::class.java) //
        clzs.add(PaintActivity::class.java) //
        clzs.add(PickCropActivity::class.java) //
        clzs.add(CharActivity::class.java) //
        clzs.add(CityPickerActivity::class.java) //
        clzs.add(PasswordActivity::class.java) //
        clzs.add(AppInfoActivity::class.java) //
    }

    interface OnLister {
        fun onSetInfo()
    }

    init {
        setData()
    }
}