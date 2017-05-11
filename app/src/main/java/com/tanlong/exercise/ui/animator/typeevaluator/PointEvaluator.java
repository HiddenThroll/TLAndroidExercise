package com.tanlong.exercise.ui.animator.typeevaluator;

import android.animation.TypeEvaluator;

import com.tanlong.exercise.model.entity.PointItem;

/**
 * PointEvaluator, 定义Point在属性动画中的变化规则
 * Created by 龙 on 2017/5/10.
 */

public class PointEvaluator implements TypeEvaluator<PointItem> {

    @Override
    public PointItem evaluate(float fraction, PointItem startValue, PointItem endValue) {
        float x = startValue.getX() + fraction * (endValue.getX() - startValue.getX());
        float y = startValue.getY() + fraction * (endValue.getY() - startValue.getY());
        return new PointItem(x, y);
    }
}
