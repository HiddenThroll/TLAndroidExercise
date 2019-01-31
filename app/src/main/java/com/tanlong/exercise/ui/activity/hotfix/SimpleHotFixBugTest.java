package com.tanlong.exercise.ui.activity.hotfix;

import android.content.Context;
import android.widget.Toast;

public class SimpleHotFixBugTest {

    public void test(Context context) {
        int a = 10;
        int b = 0;
        String content = "a is " + a + " b is " + b + " a ÷ b = " + a / b;
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }
}
