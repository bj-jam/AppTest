package com.app.test.notice;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import android.view.View;
import android.widget.ImageView;

import com.app.test.R;

public class NoticeActivity extends Activity implements View.OnClickListener {


    private AudioManager audioManager;
    private NotificationCompat.Builder mBuilder;
    private Notification notification;
    private NotificationManager manager;
    private long lastNotifiyTime;

    private ImageView imageView;
    private ImageView imageView1;
    private ImageView imageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        findViewById(R.id.sendNotice).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sendNotice:
                //TODO implement
                sendNotification(NoticeActivity.this, "测试通知栏的通知", new Intent("notification"));
                break;
        }
    }

    String channelId;

    /**
     * 通知栏发送通知
     */
    private void sendNotification(Context context, String msg, Intent intent) {
        if (manager == null) {
            manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channelId = "2";
            NotificationChannel channel = new NotificationChannel(channelId, "测试通知", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true); //是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.RED); //小红点颜色
            channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            manager.createNotificationChannel(channel);

        }

        if (audioManager == null) {
            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        }
        if (mBuilder == null) {
            mBuilder = new NotificationCompat.Builder(context, "2").setSmallIcon(context.getApplicationInfo().icon)
                    .setAutoCancel(true);
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (System.currentTimeMillis() + "").hashCode(),
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentTitle("test");
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setTicker(msg);// 通知首次出现在通知栏，带上升动画效果的
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

        // id不能相同否则只会显示一条通知
        manager.notify((System.currentTimeMillis() + "").hashCode(), notification);
    }
}
