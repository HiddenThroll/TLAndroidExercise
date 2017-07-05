package com.tanlong.exercise.ui.activity.view.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.activity.view.ViewCategoryActivity;
import com.tanlong.exercise.util.NumberUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by 龙 on 2017/7/5.
 */

public class NotificationCategoryActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.btn_help)
    Button btnHelp;
    @Bind(R.id.lv_activity_category)
    ListView lvActivityCategory;

    NotificationManager notificationManager;
    NotificationCompat.Builder builder;

    private final int NORMAL_NOTIFICATION = 1;
    private final int NO_CLEAR_NOTIFICATION = 2;
    private final int INDETERMINATE_NOTIFICATION = 3;
    private final int SPECIFIC_NOTIFICATION = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);

        initView();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);//获得NotificationManager
        builder = new NotificationCompat.Builder(this);
    }

    public void initView() {
        tvTitle.setText(R.string.notification_exercise);
        String[] items = getResources().getStringArray(R.array.notification_category);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_category, items);
        lvActivityCategory.setAdapter(adapter);
        btnHelp.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.iv_back, R.id.btn_help})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_help:
                showTips();
                break;
        }
    }

    @OnItemClick(R.id.lv_activity_category)
    public void onItemClick(int position) {
        switch (position) {
            case 0://显示Normal通知
                showNormalNotification();
                break;
            case 1://显示无法左右滑动清除的通知
                showNoClearNotification();
                break;
            case 2://显示不确定进度的通知
                showIndeterminateNotification();
                break;
            case 3://显示具体进度的通知
                showSpecificNotification();
                break;

        }
    }

    /**
     * 显示普通通知
     */
    private void showNormalNotification() {
        Intent intent = new Intent(this, ViewCategoryActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, NORMAL_NOTIFICATION, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Notification notification = builder.setContentTitle("通知标题")
                .setContentText("这是一条普通通知")
                .setContentInfo("右边通知大字内容")
                .setTicker("有新的通知~")//设置通知第一次在状态栏出现时使用的文字
                .setSmallIcon(R.mipmap.ic_launcher)//设置小图标
                .setDefaults(Notification.DEFAULT_LIGHTS|Notification.DEFAULT_SOUND)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                .setContentIntent(pendingIntent)//设置点击触发的Intent
                .build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;//设置通知点击后自动取消
        notificationManager.notify(NORMAL_NOTIFICATION, notification);
    }

    /**
     * 显示无法通过左右滑动消除的通知，适用于高优先级通知
     */
    private void showNoClearNotification() {
        Intent intent = new Intent(this, ViewCategoryActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, NO_CLEAR_NOTIFICATION, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Notification notification = builder.setContentTitle("通知标题")
                .setContentText("这是一条无法通过左右滑动清除的通知，点击后可清除")
                .setTicker("有新的通知~")//设置通知第一次在状态栏出现时使用的文字
                .setSmallIcon(R.mipmap.ic_launcher)//设置小图标
                .setDefaults(Notification.DEFAULT_LIGHTS|Notification.DEFAULT_SOUND)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                .setContentIntent(pendingIntent)//设置点击触发的Intent
                .setPriority(NotificationCompat.PRIORITY_HIGH)//设置通知优先级
                .build();
        notification.flags = Notification.FLAG_AUTO_CANCEL|Notification.FLAG_NO_CLEAR;//设置通知点击后自动取消 并且无法通过左右滑动清除
        notificationManager.notify(NO_CLEAR_NOTIFICATION, notification);
    }

    /**
     * 显示不带具体进度值的通知
     */
    private void showIndeterminateNotification() {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, INDETERMINATE_NOTIFICATION, new Intent(),
                PendingIntent.FLAG_ONE_SHOT);
        Notification notification = builder.setContentTitle("通知标题")
                .setContentText("这是一条没有具体进度值的通知")
                .setTicker("有新的通知~")//设置通知第一次在状态栏出现时使用的文字
                .setSmallIcon(R.mipmap.ic_launcher)//设置小图标
                .setContentIntent(pendingIntent)//设置点击触发的Intent
                .setProgress(0, 0, true)//不显示具体进度
                .build();
        notification.flags = Notification.FLAG_NO_CLEAR;
        notificationManager.notify(INDETERMINATE_NOTIFICATION, notification);

        // 10s后移除进度条
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Logger.e("取消Indeterminate进度条");
                Notification endNotification = builder.setProgress(0, 0, false)//设置值
                        .setContentText("加载完毕，可点击取消")
                        .build();
                endNotification.flags = Notification.FLAG_AUTO_CANCEL;
                notificationManager.notify(INDETERMINATE_NOTIFICATION, endNotification);//更新通知
            }
        }, 10 * 1000);
    }

    /**
     * 显示具体进度的通知
     */
    public void showSpecificNotification() {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, SPECIFIC_NOTIFICATION, new Intent(),
                PendingIntent.FLAG_ONE_SHOT);
        Notification notification = builder.setContentTitle("通知标题")
                .setContentText("这是一条带具体进度的通知")
                .setTicker("有新的通知~")//设置通知第一次在状态栏出现时使用的文字
                .setSmallIcon(R.mipmap.ic_launcher)//设置小图标
                .setContentIntent(pendingIntent)//设置点击触发的Intent
                .setProgress(100, 0, false)//显示具体进度
                .build();
        notification.flags = Notification.FLAG_NO_CLEAR;
        notificationManager.notify(SPECIFIC_NOTIFICATION, notification);

        CountDownTimer countDownTimer = new CountDownTimer(10000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {//倒计时
                Logger.e("millisUntilFinished is " + millisUntilFinished);
                int progress = (int) ((10000 - millisUntilFinished ) * 100 / 10000) ;
                Notification doingNotification = builder.setProgress(100, progress, false)
                        .setContentText(NumberUtil.keepTwoDecimal(progress) + "%")
                        .build();
                doingNotification.flags = Notification.FLAG_NO_CLEAR;
                notificationManager.notify(SPECIFIC_NOTIFICATION, doingNotification);
            }

            @Override
            public void onFinish() {
                Notification finishNotification = builder.setProgress(0, 0, false)
                        .setContentText("加载完毕，可点击取消")
                        .build();
                finishNotification.flags = Notification.FLAG_AUTO_CANCEL;
                notificationManager.notify(SPECIFIC_NOTIFICATION, finishNotification);
            }
        };
        countDownTimer.start();
    }

    private void showTips() {

    }

}
