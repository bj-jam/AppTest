package com.app.test.main;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.app.test.R;
import com.app.test.anim.AnimActivity;
import com.app.test.bezier.NewUiActivity;
import com.app.test.camera.CameraActivity;
import com.app.test.cardview.CardViewActivity;
import com.app.test.circle.CircleActivity;
import com.app.test.circle.anim.color.ColorCircleActivity;
import com.app.test.circularprogress.CircularProgressActivity;
import com.app.test.column.ColumnActivity;
import com.app.test.date.DateActivity;
import com.app.test.expandablelist.ExpandableListViewActivity;
import com.app.test.floatbutton.FloatButtonActivity;
import com.app.test.glide.img.GlideLoadImgAcitvity;
import com.app.test.keyboard.KeyboardActivity;
import com.app.test.line.LineActivity;
import com.app.test.listview.LiseViewActivity;
import com.app.test.lottie.LottieActivity;
import com.app.test.matrix.MatrixActivity;
import com.app.test.mirrorimage.MirrorImageActivity;
import com.app.test.move.MoveActivity;
import com.app.test.mvvm.databinding.DataBindingActivity;
import com.app.test.notice.NoticeActivity;
import com.app.test.numberprogressbar.NumberProgressActivity;
import com.app.test.recyclerview.Damping1Activity;
import com.app.test.recyclerview.DampingActivity;
import com.app.test.recyclerview.RecyclerViewActivity;
import com.app.test.redenveloped.RedEnvelopesActivity;
import com.app.test.ring.anim.CircleAnimActivity;
import com.app.test.ring.anim.CircleMenuActivity;
import com.app.test.ring.anim.color.RingAnimColorActivity;
import com.app.test.scrollview.ScrollViewActivity;
import com.app.test.service.ServiceActivity;
import com.app.test.shadow.ShadowActivity;
import com.app.test.showimage.ShowIamgeActivity;
import com.app.test.smartrefresh.SmartRefreshActivity;
import com.app.test.sortlist.SortListActivity;
import com.app.test.spannable.SpannableActivity;
import com.app.test.timelytextview.TimelyActivity;
import com.app.test.titanic.TitanicActivity;
import com.app.test.transition.activity.TransitionActivity;
import com.app.test.viewflipper.ViewFlipperActivity;
import com.app.test.viewpager.ViewPagerActivity;
import com.app.test.wave.WaveActivity;
import com.app.test.webview.WebViewActivity;
import com.app.test.hook.ui.TargetActivity;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends BaseAdapter {

    private List<Integer> texts;
    private ArrayList<Class<?>> clzs;
    private OnLister onLister;

    public MainAdapter(OnLister onLister) {
        // TODO Auto-generated constructor stub
        this.onLister = onLister;
        setData();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return texts.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return texts.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View view, final ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = null;
        if (view == null) {
            view = View.inflate(parent.getContext(), R.layout.view_main_item,
                    null);
            holder = new Holder();
            holder.textView = (TextView) view.findViewById(R.id.textView);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        holder.textView.setText(parent.getContext().getResources()
                .getString(texts.get(position)));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(parent.getContext(), "熱修复", Toast.LENGTH_SHORT).show();
//                Toast.makeText(parent.getContext(), texts.get(100), Toast.LENGTH_SHORT).show();
                parent.getContext().startActivity(
                        new Intent(parent.getContext(), clzs.get(position)));
            }
        });
        return view;
    }

    private final class Holder {
        TextView textView;
    }

    private void setData() {
        if (texts == null) {
            texts = new ArrayList<Integer>();
        }
        texts.add(R.string.date_activity);
        texts.add(R.string.circle_activity);
        texts.add(R.string.line_activity);
        texts.add(R.string.expandablelist_activity);
        texts.add(R.string.showimage_activity);
        texts.add(R.string.circle_anim_activity);
        texts.add(R.string.column_activity);
        texts.add(R.string.mirror_activity);
        texts.add(R.string.ColorCircleActivity);
        texts.add(R.string.ColorRingActivity);
        texts.add(R.string.glide_load_activity);
//		texts.add(R.string.grid_refresh_activity);
        texts.add(R.string.viewpager_activity);
        texts.add(R.string.download_activity);
        texts.add(R.string.shadow_activity);
        texts.add(R.string.camera_activity);
        texts.add(R.string.notice_activity);
        texts.add(R.string.recycler_activity);
        texts.add(R.string.wave_activity);
        texts.add(R.string.webview_activity);
        texts.add(R.string.cardView_activity);
        texts.add(R.string.list_view_activity);
        texts.add(R.string.keyboard_activity);
        texts.add(R.string.anim_activity);
        texts.add(R.string.damping_activity);
        texts.add(R.string.damping1_activity);
        texts.add(R.string.transition_activity);
        texts.add(R.string.data_binding_activity);
        texts.add(R.string.smart_refresh_activity);
        texts.add(R.string.move_activity);
        texts.add(R.string.lottie_activity);
        texts.add(R.string.red_envelopes_activity);
        texts.add(R.string.new_ui_activity);
        texts.add(R.string.float_button_activity);
        texts.add(R.string.circular_progress_activity);
        texts.add(R.string.number_progress_activity);
        texts.add(R.string.titanic_activity);
        texts.add(R.string.scroll_view_activity);
        texts.add(R.string.Circle_Menu_Activity);
        texts.add(R.string.SortListActivity);
        texts.add(R.string.SpannableActivity);
        texts.add(R.string.MatrixActivity);
        texts.add(R.string.ViewFlipperActivity);
        texts.add(R.string.TimelyActivity);
        texts.add(R.string.TargetActivity);
        if (clzs == null) {
            clzs = new ArrayList<Class<?>>();
        }
        clzs.add(DateActivity.class); //
        clzs.add(CircleActivity.class); //
        clzs.add(LineActivity.class); //
        clzs.add(ExpandableListViewActivity.class); //
        clzs.add(ShowIamgeActivity.class); //
        clzs.add(CircleAnimActivity.class); //
        clzs.add(ColumnActivity.class); //
        clzs.add(MirrorImageActivity.class); //
        clzs.add(ColorCircleActivity.class); //
        clzs.add(RingAnimColorActivity.class); //
        clzs.add(GlideLoadImgAcitvity.class); //
//		clzs.add(PullToRefreshGridActivity.class); //
        clzs.add(ViewPagerActivity.class); //
        clzs.add(ServiceActivity.class); //
        clzs.add(ShadowActivity.class); //
        clzs.add(CameraActivity.class); //
        clzs.add(NoticeActivity.class); //
        clzs.add(RecyclerViewActivity.class); //
        clzs.add(WaveActivity.class); //
        clzs.add(WebViewActivity.class); //
        clzs.add(CardViewActivity.class); //
        clzs.add(LiseViewActivity.class); //
        clzs.add(KeyboardActivity.class); //
        clzs.add(AnimActivity.class); //
        clzs.add(DampingActivity.class); //
        clzs.add(Damping1Activity.class); //
        clzs.add(TransitionActivity.class); //
        clzs.add(DataBindingActivity.class); //
        clzs.add(SmartRefreshActivity.class); //
        clzs.add(MoveActivity.class); //
        clzs.add(LottieActivity.class); //
        clzs.add(RedEnvelopesActivity.class); //
        clzs.add(NewUiActivity.class); //
        clzs.add(FloatButtonActivity.class); //
        clzs.add(CircularProgressActivity.class); //
        clzs.add(NumberProgressActivity.class); //
        clzs.add(TitanicActivity.class); //
        clzs.add(ScrollViewActivity.class); //
        clzs.add(CircleMenuActivity.class); //
        clzs.add(SortListActivity.class); //
        clzs.add(SpannableActivity.class); //
        clzs.add(MatrixActivity.class); //
        clzs.add(ViewFlipperActivity.class); //
        clzs.add(TimelyActivity.class); //
        clzs.add(TargetActivity.class); //
    }


    public interface OnLister {
        void onSetInfo();
    }
}
