package com.tanlong.exercise.ui.activity.view.animator.propertyanimator;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.fragment.dialog.ShowTipsFragment;
import com.tanlong.exercise.util.ToastHelp;
import com.tanlong.exercise.util.VersionUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 灵动菜单
 * Created by 龙 on 2017/5/23.
 */

public class SmartMenuActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.btn_help)
    Button btnHelp;
    @Bind(R.id.iv_menu_root)
    ImageView ivMenuRoot;
    @Bind(R.id.iv_menu_one)
    ImageView ivMenuOne;
    @Bind(R.id.iv_menu_two)
    ImageView ivMenuTwo;
    @Bind(R.id.iv_menu_three)
    ImageView ivMenuThree;
    @Bind(R.id.iv_menu_four)
    ImageView ivMenuFour;

    private boolean isShow;
    private final float RADIUS = 500;
    private float x, y;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_smart_menu);
        ButterKnife.bind(this);

        btnHelp.setVisibility(View.VISIBLE);
        tvTitle.setText("灵动菜单");
        initData();
        ivMenuRoot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                x = ivMenuRoot.getX();
                y = ivMenuRoot.getY();

                Logger.e("x is %s, y is %s", x, y);

                if (VersionUtil.hasJellyBean()) {
                    ivMenuRoot.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    ivMenuRoot.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }

    private void initData() {
        isShow = false;
    }

    @OnClick({R.id.iv_back, R.id.btn_help, R.id.iv_menu_root, R.id.iv_menu_one, R.id.iv_menu_two, R.id.iv_menu_three, R.id.iv_menu_four})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_help:
                showTips();
                break;
            case R.id.iv_menu_root:
                if (isShow) {
                    closeMenu();
                    isShow = false;
                } else {
                    showMenu();
                    isShow = true;
                }
                break;
            case R.id.iv_menu_one:
                ToastHelp.showShortMsg(this, "menu one");
                break;
            case R.id.iv_menu_two:
                ToastHelp.showShortMsg(this, "menu two");
                break;
            case R.id.iv_menu_three:
                ToastHelp.showShortMsg(this, "menu three");
                break;
            case R.id.iv_menu_four:
                ToastHelp.showShortMsg(this, "menu four");
                break;
        }
    }

    private void showTips() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("实现灵动菜单\n")
                .append("1. 圆弧上任意一点的坐标计算公式为X = 圆心坐标X + 半径 * cos(角度),Y = 圆心坐标Y + 半径 * sin(角度),这里的角度是以X轴正方向为起点，顺时针方向计算的\n")
                .append("2. 每一个ImageView的动画由3个动画组成，translationX,translationY和rotation，3个动画同时执行\n")
                .append("3. 每一个ImageVIew的AnimatorSet组成最终的AnimatorSet，顺序执行");

        ShowTipsFragment fragment = ShowTipsFragment.newInstance(stringBuilder.toString());
        fragment.show(getSupportFragmentManager(), "");
    }

    private void showMenu() {
        AnimatorSet menuOneSet = buildShowAnimator(ivMenuOne, 0);
        AnimatorSet menuTwoSet = buildShowAnimator(ivMenuTwo, -30);
        AnimatorSet menuThreeSet = buildShowAnimator(ivMenuThree, -60);
        AnimatorSet menuFourSet = buildShowAnimator(ivMenuFour, -90);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(menuOneSet, menuTwoSet, menuThreeSet, menuFourSet);
        animatorSet.start();
    }

    private void closeMenu() {
        AnimatorSet menuOneSet = buildCloseAnimator(ivMenuOne);
        AnimatorSet menuTwoSet = buildCloseAnimator(ivMenuTwo);
        AnimatorSet menuThreeSet = buildCloseAnimator(ivMenuThree);
        AnimatorSet menuFourSet = buildCloseAnimator(ivMenuFour);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(menuFourSet, menuThreeSet, menuTwoSet, menuOneSet);
        animatorSet.start();

    }

    /**
     * 生成对应ImageView的开启菜单动画
     * @param obj -- ImageView
     * @param alpha -- 以X轴正方向为起点，顺时针方向计算的角度
     * @return
     */
    private AnimatorSet buildShowAnimator(ImageView obj, float alpha) {
        double translationX = obj.getTranslationX() + RADIUS * Math.cos(Math.toRadians(alpha));//Math.sin/cos方法接收的参数为弧度
        double translationY = obj.getTranslationY() + RADIUS * Math.sin(Math.toRadians(alpha));
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(obj, "translationX", 0, (float) translationX);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(obj, "translationY", 0, (float) translationY);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(obj, "rotation", 0, 360);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator1, animator2, animator3);
        return animatorSet;
    }

    /**
     * 生成对应ImageView的关闭菜单动画
     * @param obj
     * @return
     */
    private AnimatorSet buildCloseAnimator(ImageView obj) {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(obj, "translationX", obj.getTranslationX(), 0);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(obj, "translationY", obj.getTranslationY(), 0);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(obj, "rotation", 0, 360);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator1, animator2, animator3);
        return animatorSet;
    }
}
