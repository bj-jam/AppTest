package com.app.test.recyclerview;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.app.test.R;
import com.app.test.util.MyListView;
import com.app.test.windows.WindowUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.app.test.base.App.context;

/**
 * Created by Administrator on 2017/3/7.
 */

public class RecyclerViewActivity extends Activity {
    private MyListView myListView;
    private CourseAdapter adapter;
    private List<CourseInfoDto> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        initDate();
        initView();

        Intent intent = new Intent();
        sendNotification(context, "《课程名称》已就位，快去确认课程并开始学习吧！", intent);
    }

    private void initDate() {
        list = new ArrayList<CourseInfoDto>();
        for (int i = 0; i < 20; i++) {
            CourseInfoDto dto = new CourseInfoDto();
            dto.type = "测试" + i;
            dto.courseDtos = new ArrayList<CourseInfo>();
            for (int j = 0; j < 20; j++) {
                CourseInfo courseInfo = new CourseInfo();
                if (j % 2 == 0) {
                    courseInfo.name = "姓名" + i + j + "测试新的控件大姐夫拉开到交付快递费显示问题";
                } else {
                    courseInfo.name = "姓名" + i + "++" + j;
                }
                courseInfo.price = "价格" + i + j;
                dto.courseDtos.add(courseInfo);
            }
            list.add(dto);
        }
    }

    private void initView() {
        myListView = (MyListView) findViewById(R.id.myListView);
        adapter = new CourseAdapter(this, list);
        myListView.setAdapter(adapter);
    }


    private class CourseAdapter extends BaseAdapter {

        private List<CourseInfoDto> myCourseList;
        private Context context;

        public CourseAdapter(Context context, List<CourseInfoDto> myCourseList) {
            this.myCourseList = myCourseList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return myCourseList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder h;
            if (convertView == null) {
                h = new ViewHolder();
                convertView = View.inflate(RecyclerViewActivity.this, R.layout.item_recycler, null);
                h.mRecyclerView = (RecyclerView) convertView.findViewById(R.id.mRecyclerView);
                h.type = (TextView) convertView.findViewById(R.id.type);
                h.mRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                convertView.setTag(h);
            } else {
                h = (ViewHolder) convertView.getTag();
            }

            SecondAdapter treeDownAdapter = new SecondAdapter(RecyclerViewActivity.this);
            h.secondAdapter = treeDownAdapter;
            h.mRecyclerView.setAdapter(treeDownAdapter);
            h.mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
            initTreeDownItemListener(h);

            CourseInfoDto dto = myCourseList.get(position);
            h.type.setText(dto.type);
            h.secondAdapter.setList(dto.courseDtos, position % 3);
            h.secondAdapter.notifyDataSetChanged();
            return convertView;
        }
    }


    private void initTreeDownItemListener(ViewHolder holder) {
        holder.secondAdapter.setItemClickListener(new SecondAdapter.RecycleItemClickListener() { //设置树下item点击监听
            @Override
            public void onItemClick(CourseInfo courseInfo) {
//                TreeDownDto dto = treeDownDtoList.get(position);
//                Intent intent = new Intent(getContext(), TreeDownDetialActivity.class);
//                intent.putExtra("treedownDto", dto);
//                startActivity(intent);
                Toast.makeText(RecyclerViewActivity.this, courseInfo.name + "-----" + courseInfo.price, Toast.LENGTH_SHORT).show();
                WindowUtils.getInstance(RecyclerViewActivity.this).stopAni();
            }
        });
    }

    private class ViewHolder {
        private TextView type;
        private RecyclerView mRecyclerView;
        private SecondAdapter secondAdapter;
    }

    private class CourseInfoDto implements Serializable {
        private String type;
        List<CourseInfo> courseDtos;
    }


    public class CourseInfo implements Serializable {
        public String name;
        public String price;
    }

    private AudioManager audioManager;
    private NotificationCompat.Builder mBuilder;
    private Notification notification;
    private NotificationManager manager;
    private long lastNotifiyTime;

    /**
     * 通知栏发送通知
     */
    private void sendNotification(Context context, String msg, Intent intent) {
        if (audioManager == null) {
            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        }
        if (mBuilder == null) {
            mBuilder = new NotificationCompat.Builder(context).setSmallIcon(context.getApplicationInfo().icon)
                    .setAutoCancel(true);
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (System.currentTimeMillis() + "").hashCode(),
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentTitle(context.getResources().getString(R.string.app_name));
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setTicker(msg);// 通知首次出现在通知栏，带上升动画效果的
//        RemoteViews contentViews = new RemoteViews(getPackageName(),
//                R.layout.custom_notification);
//
//        contentViews.setTexViewText(R.id.notifiContent, "xxx");
//
//        mBuilder.setContent(RemoteViews)

        mBuilder.setContentText(msg);
        mBuilder.setContentIntent(pendingIntent);
        if (System.currentTimeMillis() - lastNotifiyTime > 2000) {
            lastNotifiyTime = System.currentTimeMillis();
            if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {// 静音
            } else if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {// 响铃
                mBuilder.setDefaults(Notification.DEFAULT_SOUND);
            } else if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE) {// 震动
                mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
            }
        }
        if (notification == null) {
            notification = mBuilder.build();
        }
        if (manager == null) {
            manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        // id不能相同否则只会显示一条通知
        manager.notify((System.currentTimeMillis() + "").hashCode(), notification);
    }
}
