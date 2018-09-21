package com.tanlong.exercise.ui.view.customview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class AudioPulsationView extends View {

    /**
     * 动画最大周期 3s
     */
    private final long MAX_ANIM_PERIOD = 1000;

    private final Paint mPaint;
    private final Context context;

    /**
     * 单根柱形宽度，默认为10
     */
    private int pillarWidth = 10;

    /**
     * 柱形数量，默认为4
     */
    private int pillarAmount = 4;

    /**
     * 柱形颜色
     */
    private int pillarColor = Color.rgb(179, 100, 53);

    /**
     * 波动节奏
     * 快-慢 0-1
     */
    private double rhythm = 0.5;

    /**
     * 柱形颜色组，
     * 当pillarColors等于pillarAmount时，对应柱形取pillarColors对应颜色
     * 当pillarColors大于pillarAmount时，对应柱形取pillarColors前pillarAmount个对应颜色
     * 当pillarColors小于pillarAmount时，pillarColors循环拼接后，对应柱形取pillarColors对应颜色
     */
    private int[] pillarColors;

    /**
     * 内容部分宽度
     */
    private int contentWidth;

    /**
     * 内容部分高度
     */
    private int contentHight;

    /**
     * 内容区域的左、上、右、下坐标
     */
    private int left, top, right, bottom;

    /**
     * 柱形条间隙
     */
    private float gap;

    /**
     * 是否使用颜色集合
     */
    private boolean useColors = false;

    /**
     * 动画是否开启
     */
    private boolean isStartAnim = false;

    /**
     * 动画值
     */
    private float[] animValues;

    /**
     * 波动范围
     */
    private Wave[] waves;

    public AudioPulsationView(Context context) {
        this(context, null);
    }

    public AudioPulsationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AudioPulsationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //若动画未开启过，则为每根柱形条开启动画
        if (!isStartAnim) {
            for (int i = 0; i < animValues.length; i++) {
                //每个柱子开启动画
                startAnim(i);
            }
            isStartAnim = true;
        }
        //画柱形条
        drawPillar(canvas);
    }

    /**
     * 开启动画
     * @param pillarPosition -- 第几根柱子
     */
    private void startAnim(final int pillarPosition) {
        final ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //对应柱形条绑定对应属性动画值
                animValues[pillarPosition] = (float) animation.getAnimatedValue();
                //每次动画都重画view
                invalidate();
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            //监听动画重播时，需要产生下一次波动的随机波动周期以及波动实体
            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
                //为了让波动不具备很强规则性，这里随机打乱周期，并将rhythm波动频率带入
                anim.setDuration((long) (MAX_ANIM_PERIOD * (Math.random() / 4 + 0.75) * rhythm));
                //设置下次波动实体
                resetWave(pillarPosition);
            }
        });
        //随机产生第一次波动周期
        anim.setDuration((long) (MAX_ANIM_PERIOD * (Math.random() / 4 + 0.75) * rhythm));
        //无限循环
        anim.setRepeatCount(ValueAnimator.INFINITE);
        //从头开始动画
        anim.setRepeatMode(ValueAnimator.RESTART);
        anim.start();
    }

    /**
     * 画柱形条
     *
     * @param canvas
     */
    private void drawPillar(Canvas canvas) {
        //定义柱形条顶部坐标，因为柱形条需要跟随属性动画波动，所以这是一个时刻变化的值
        float pillarTop;
        //循环画每根柱形条
        for (int i = 0; i < pillarAmount; i++) {
            //是否设置了颜色集合，设置了便根据集合设置画笔颜色，未设置则为画笔设置单一颜色
            if (useColors) {
                mPaint.setColor(pillarColors[i]);
            } else {
                mPaint.setColor(pillarColor);
            }
            /**
             * 根据wave对象及属性动画值计算柱形条当前顶部坐标
             * 公式：波动起始坐标+方向*波动距离*动画值
             */
            pillarTop = waves[i].startPosition + waves[i].direction * waves[i].distance * animValues[i];
            //画柱形条
            canvas.drawRect(left + (gap + pillarWidth) * i, pillarTop, left + (gap + pillarWidth) * i + pillarWidth,
                    bottom, mPaint);
        }
    }

    /**
     * 获取随机波动实体
     * @param position -- 第几根柱子
     */
    private void resetWave(int position) {
        //产生一个0.5-1的随机基数
        double r = Math.random() / 2 + 0.5;
        //将波动方向反向
        waves[position].direction = -waves[position].direction;
        /**
         *根据随机基数获取波动距离
         *当方向为1即方向向下运动时，波动距离的最大值为：底部坐标-上次波动的目标值(bottom - waves[position].targetPosition）
         * 当方向为-1即方向向上时，波动的最大值为：上次运动的目标值-顶部坐标（waves[position].targetPosition - top）
         */
        waves[position].distance = waves[position].direction == Wave.DIRECTION_DOWN ?
                (int) ((bottom - waves[position].targetPosition) * r) :
                (int) ((waves[position].targetPosition - top) * r);
        //该次波动的起始值设置为上次波动的目标值
        waves[position].startPosition = waves[position].targetPosition;
        //该次波动的目标值为：波动起始值+波动反向*波动距离
        waves[position].targetPosition = waves[position].startPosition +
                waves[position].distance * waves[position].direction;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //设置控件宽度, 当高为wrap content时,使用最小宽度作为最小高度
        int measureWidth = measureWidth(widthMeasureSpec);
        int measureHeight = measureHeight(heightMeasureSpec, measureWidth);
        setMeasuredDimension(measureWidth, measureHeight);

        //控件内容宽度
        contentWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        //控件内容高度
        contentHight = getHeight() - getPaddingTop() - getPaddingBottom();
        //控件的左、上、右、下坐标
        left = getPaddingLeft();
        top = getPaddingTop();
        right = getWidth() - getPaddingRight();
        bottom = getHeight() - getPaddingBottom();
        //柱形条间隙 = (内容宽度 - 柱子宽度 * 柱子个数) / (柱子个数 - 1)
        gap = pillarAmount > 1 ? (float) (contentWidth - pillarAmount * pillarWidth) /
                (float) (pillarAmount - 1) : 0;

        //初始化属性动画值数组
        this.animValues = new float[pillarAmount];
        //初始化波动实体数组
        this.waves = new Wave[pillarAmount];
        for (int i = 0; i < waves.length; i++) {
            int distance = (int) (Math.random() * contentHight);
            waves[i] = new Wave(Wave.DIRECTION_UP, distance, bottom, bottom - distance);
        }
    }

    /**
     * 计算控件宽度
     *
     * @param measureSpec -- 父View传入的MeasureSpec
     * @return
     */
    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        //根据柱形数量跟宽度计算出最小宽度
        //最小宽度 = 每个柱子宽度 * 柱子个数 + Padding
        int minWidth = pillarWidth * pillarAmount + getPaddingLeft() + getPaddingRight();
        if (specMode == MeasureSpec.EXACTLY) {
            return Math.max(specSize, minWidth);
        }
        //wrap_content时返回最小宽度
        return minWidth;
    }

    /**
     * 计算控件高度
     * @param measureSpec -- 父View传入的MeasureSpec
     * @param defSize -- wrap_content时的高度
     * @return
     */
    private int measureHeight(int measureSpec, int defSize) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.AT_MOST) {
            specSize = defSize;
        }
        return specSize;
    }

    /**
     * 设置柱形参数
     *
     * @param pillarAmount 柱形个数
     * @param pillarWidth  柱形宽度
     * @param pillarColor  柱形颜色
     */
    public void setPillar(int pillarAmount, int pillarWidth, int pillarColor) {
        this.pillarAmount = pillarAmount > 0 ? pillarAmount : 0;
        this.pillarWidth = pillarWidth > 0 ? dip2px(context, pillarWidth) : 0;
        useColors = false;
        this.pillarColor = pillarColor;
    }

    /**
     * 设置柱形参数
     *
     * @param pillarAmount 柱形个数
     * @param pillarWidth  柱形宽度
     * @param pillarColors 柱形颜色集合
     */
    public void setPillar(int pillarAmount, int pillarWidth, int[] pillarColors) {
        this.pillarAmount = pillarAmount > 0 ? pillarAmount : 0;

        this.pillarWidth = pillarWidth > 0 ? dip2px(context, pillarWidth) : 0;
        if (pillarColors != null && pillarColors.length > 0) {
            useColors = true;
            this.pillarColors = new int[pillarAmount];
            if (pillarColors.length == pillarAmount || pillarColors.length > pillarAmount) {
                System.arraycopy(pillarColors, 0, this.pillarColors, 0, pillarAmount);
            } else {
                for (int i = 0; i < pillarAmount; i++) {
                    this.pillarColors[i] = pillarColors[i % pillarColors.length];
                }
            }
        }
    }

    /**
     * 设置波动节奏
     *
     * @param rhythm
     */
    public void setRhythm(double rhythm) {
        double m = rhythm < 0.0 ? 0.0 : rhythm > 1 ? 1 : rhythm;
        //对rhythm进行偏量计算
        this.rhythm = (m / 4) + 0.75;
    }

    /**
     * 波动实体类
     */
    private class Wave {
        public static final int DIRECTION_UP = -1;
        public static final int DIRECTION_DOWN = 1;

        /**
         * 波动方向.
         * 1：正向波动（向下），-1：负向波动（向上）
         */
        int direction;

        /**
         * 波动距离
         */
        float distance;

        /**
         * 起始位置
         */
        float startPosition;

        /**
         * 目标位置
         */
        float targetPosition;

        public Wave() {
        }

        /**
         * @param direction -- 波动方向, -1向上, 1向下
         * @param distance -- 波动距离
         * @param startPosition -- 起始位置
         * @param targetPosition -- 目标位置
         */
        Wave(int direction, float distance, float startPosition, float targetPosition) {
            this.direction = direction;
            this.distance = distance;
            this.startPosition = startPosition;
            this.targetPosition = targetPosition;
        }

        @Override
        public String toString() {
            return "Wave{" +
                    "direction=" + direction +
                    ", distance=" + distance +
                    ", startPosition=" + startPosition +
                    ", targetPosition=" + targetPosition +
                    '}';
        }
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
