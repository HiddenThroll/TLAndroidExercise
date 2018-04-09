package com.tanlong.exercise.ui.activity.databinding;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tanlong.exercise.R;
import com.tanlong.exercise.databinding.ActivitySimpleDataBindingBinding;
import com.tanlong.exercise.model.entity.User;

import java.util.Random;

public class SimpleDataBindingActivity extends AppCompatActivity {

    ActivitySimpleDataBindingBinding binding;
    User user = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_simple_data_binding);

        user.setName("Test");
        user.setIcon("http://avatar.csdn.net/4/0/7/1_zhuhai__yizhi.jpg");
        user.setAge(1);
        binding.setUser(user);
        binding.setSimpleActivity(this);
    }

    public void changeUserName() {
        user.setName("Test " + new Random().nextInt(10));
        if (user.getAge() > 18) {
            user.setAge(1);
        } else {
            user.setAge(19);
        }

    }
}
