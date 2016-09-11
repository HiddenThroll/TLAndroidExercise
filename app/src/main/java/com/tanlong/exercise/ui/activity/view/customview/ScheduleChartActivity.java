package com.tanlong.exercise.ui.activity.view.customview;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.model.entity.OfficialCarSchedule;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.view.customview.ScheduleChart;
import com.tanlong.exercise.util.DateUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 时间分配表
 * Created by 龙 on 2016/9/11.
 */
public class ScheduleChartActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.schedule_chart)
    ScheduleChart scheduleChart;
    @Bind(R.id.iv_back)
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_schedule_chart);
        ButterKnife.bind(this);

        initView();
        initData();
    }

    private void initView() {
        tvTitle.setText(R.string.schedule_chart);
    }

    private void initData() {
        List<OfficialCarSchedule> scheduleTime = new ArrayList<>();
        List<OfficialCarSchedule> useTime = new ArrayList<>();

        try {
            Date date1 = DateUtil.changeStringToDate("2016-09-11 08:00", DateUtil.NO_SECOND_DATETIME);
            Date date2 = DateUtil.changeStringToDate("2016-09-11 18:00", DateUtil.NO_SECOND_DATETIME);
            Date date3 = DateUtil.changeStringToDate("2016-09-12 10:00", DateUtil.NO_SECOND_DATETIME);
            Date date4 = DateUtil.changeStringToDate("2016-09-12 16:00", DateUtil.NO_SECOND_DATETIME);
            scheduleTime.add(new OfficialCarSchedule(date1.getTime(), date2.getTime()));
            scheduleTime.add(new OfficialCarSchedule(date3.getTime(), date4.getTime()));

            Date date5 = DateUtil.changeStringToDate("2016-09-12 10:00", DateUtil.NO_SECOND_DATETIME);
            Date date6 = DateUtil.changeStringToDate("2016-09-12 16:00", DateUtil.NO_SECOND_DATETIME);
            useTime.add(new OfficialCarSchedule(date5.getTime(), date6.getTime()));

            scheduleChart.setmScheduleTime(scheduleTime);
            scheduleChart.setmCurUseTime(useTime);
            scheduleChart.invalidate();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }
}
