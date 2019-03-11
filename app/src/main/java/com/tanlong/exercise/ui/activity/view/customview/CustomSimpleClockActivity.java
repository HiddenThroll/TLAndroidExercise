package com.tanlong.exercise.ui.activity.view.customview;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.fragment.dialog.ShowTipsFragment;
import com.tanlong.exercise.ui.view.customview.CustomSimpleClock;


import java.util.Date;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *
 * @author Administrator
 * @date 2017/3/20
 */

public class CustomSimpleClockActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_help)
    Button btnHelp;
    @BindView(R.id.custom_simple_clock)
    CustomSimpleClock customSimpleClock;

    private boolean isStartClock = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_custom_simple_clock);
        ButterKnife.bind(this);

        initView();
        startClock();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isStartClock = false;
    }

    private void startClock() {
        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                while (isStartClock) {
                    customSimpleClock.setCurDate(new Date());

                    SystemClock.sleep(200);
                }
            }
        });
    }

    private void initView() {
        tvTitle.setText(R.string.custom_simple_clock);
        btnHelp.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.iv_back, R.id.btn_help})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_help:
                showTips();
                break;
            default:
                break;
        }
    }

    private void showTips() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("1. Canvas常用4方法: \n")
                .append("1.1 Canvas.translate() 画布平移,其实是坐标系的平移, translate(X,Y)即将绘图原点由坐标原点(0,0)移动到(x,y),之后的绘图操作以(x,y)为原点执行.本例中使用该方法绘制时针和分针.\n")
                .append("1.2 Canvas.rotate() 画布旋转,其实是坐标系的旋转,与translate()方法类似,本例中使用该方法完成表盘的绘制\n")
                .append("1.3 Canvas.save() 保存画布, 将之前所有已绘制图像保存起来,让后续操作好像在一个新的图层上操作一样\n")
                .append("1.4 Canvas.restore()合并图层, 将save()之后绘制的所有图像与save()之前的图像进行合并\n")
                .append("2. 测量绘制文字内容宽度方法: \n")
                .append("2.1 Paint.setTextSize()设置绘制文字使用的Paint大小\n")
                .append("2.2 Paint.measure(String content)返回绘制文字内容宽度, 单位为px");

        ShowTipsFragment fragment = ShowTipsFragment.newInstance(stringBuilder.toString());
        fragment.show(getSupportFragmentManager(), "");
    }
}
