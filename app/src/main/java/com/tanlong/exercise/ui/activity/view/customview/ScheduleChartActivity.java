package com.tanlong.exercise.ui.activity.view.customview;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.model.entity.OfficialCarSchedule;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.fragment.dialog.ShowTipsFragment;
import com.tanlong.exercise.ui.view.customview.ScheduleChart;
import com.tanlong.exercise.ui.view.customviewgroup.timepopupwindow.TimePopupWindow;
import com.tanlong.exercise.util.DateUtil;
import com.tanlong.exercise.util.LogTool;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 时间分配表
 *
 * @author 龙
 * @date 2016/9/11
 */
public class ScheduleChartActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_help)
    Button btnHelp;
    @BindView(R.id.schedule_chart)
    ScheduleChart scheduleChart;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_schedule_time_start)
    TextView tvScheduleTimeStart;
    @BindView(R.id.tv_schedule_time_end)
    TextView tvScheduleTimeEnd;
    @BindView(R.id.tv_select_time_start)
    TextView tvSelectTimeStart;
    @BindView(R.id.tv_select_time_end)
    TextView tvSelectTimeEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_schedule_chart);
        ButterKnife.bind(this);

        initView();
        initData();
        initListener();
    }

    private void initView() {
        tvTitle.setText(R.string.schedule_chart);
        btnHelp.setVisibility(View.VISIBLE);
    }

    private void initData() {

    }

    private void initListener() {
        ivBack.setOnClickListener(this);
        tvScheduleTimeStart.setOnClickListener(this);
        tvScheduleTimeEnd.setOnClickListener(this);
        tvSelectTimeStart.setOnClickListener(this);
        tvSelectTimeEnd.setOnClickListener(this);
        btnHelp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_schedule_time_start:
                selectTime(0);
                break;
            case R.id.tv_schedule_time_end:
                selectTime(1);
                break;
            case R.id.tv_select_time_start:
                selectTime(2);
                break;
            case R.id.tv_select_time_end:
                selectTime(3);
                break;
            case R.id.btn_help:
                showTips();
                break;
            default:
                break;
        }
    }

    private void selectTime(final int mode) {
        TimePopupWindow timePopupWindow = new TimePopupWindow(this, TimePopupWindow.Type.MONTH_DAY_HOUR_MIN);
        timePopupWindow.setOnTimeSelectListener(new TimePopupWindow.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                LogTool.e(TAG, "date is " + DateUtil.getDateTime(date));
                String selectDate = DateUtil.getDateTime(date, DateUtil.NO_SECOND_DATETIME);
                switch (mode) {
                    case 0:
                        tvScheduleTimeStart.setText(selectDate);
                        break;
                    case 1:
                        tvScheduleTimeEnd.setText(selectDate);
                        break;
                    case 2:
                        tvSelectTimeStart.setText(selectDate);
                        break;
                    case 3:
                        tvSelectTimeEnd.setText(selectDate);
                        break;
                    default:
                        break;
                }

                changeScheduleTime();
                changeSelectTime();
            }
        });
        timePopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    /**
     * 改变已分配时间
     */
    private void changeScheduleTime() {
        List<OfficialCarSchedule> scheduleTime = new ArrayList<>();

        String startTime = tvScheduleTimeStart.getText().toString();
        String endTime = tvScheduleTimeEnd.getText().toString();

        if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime)) {
            return;
        }

        try {
            Date date1 = DateUtil.changeStringToDate(startTime, DateUtil.NO_SECOND_DATETIME);
            Date date2 = DateUtil.changeStringToDate(endTime, DateUtil.NO_SECOND_DATETIME);
            scheduleTime.add(new OfficialCarSchedule(date1.getTime(), date2.getTime()));

            scheduleChart.setmScheduleTime(scheduleTime);
            scheduleChart.invalidate();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 改变选择时间
     */
    private void changeSelectTime() {
        List<OfficialCarSchedule> scheduleTime = new ArrayList<>();

        String startTime = tvSelectTimeStart.getText().toString();
        String endTime = tvSelectTimeEnd.getText().toString();

        if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime)) {
            return;
        }

        try {
            Date date1 = DateUtil.changeStringToDate(startTime, DateUtil.NO_SECOND_DATETIME);
            Date date2 = DateUtil.changeStringToDate(endTime, DateUtil.NO_SECOND_DATETIME);
            scheduleTime.add(new OfficialCarSchedule(date1.getTime(), date2.getTime()));

            scheduleChart.setmCurUseTime(scheduleTime);
            scheduleChart.invalidate();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void showTips() {
        StringBuilder sb = new StringBuilder();
        sb.append("1. 根据时间绘制对应矩形块\n")
                .append("2. 按照已分配时间、已选择时间和冲突时间的顺序绘制\n");

        ShowTipsFragment fragment = ShowTipsFragment.newInstance(sb.toString());
        fragment.show(getSupportFragmentManager(), "");
    }
}
