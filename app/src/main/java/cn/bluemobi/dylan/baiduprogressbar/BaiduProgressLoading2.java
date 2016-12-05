package cn.bluemobi.dylan.baiduprogressbar;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.FloatEvaluator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by dylan on 2016-12-04.
 */

public class BaiduProgressLoading2 extends FrameLayout {

    /**
     * 存放三个小球的集合
     */
    private List<ImageView> views = new ArrayList<>();

    public BaiduProgressLoading2(Context context) {
        super(context);
        init();
    }

    public BaiduProgressLoading2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaiduProgressLoading2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void assignViews() {
        ImageView iv_blue = (ImageView) findViewById(R.id.iv_blue);
        ImageView iv_yellow = (ImageView) findViewById(R.id.iv_yellow);
        ImageView iv_red = (ImageView) findViewById(R.id.iv_red);
        views.add(iv_yellow);
        views.add(iv_red);
        views.add(iv_blue);
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.baidu_progress_bar, this, true);
        assignViews();
        startAnimator1();
        startAnimator2();
        startAnimator3();
    }

    PointF point = new PointF();


    private class MyEvaluor extends FloatEvaluator {
        @Override
        public Float evaluate(float fraction, Number startValue, Number endValue) {
            Log.d("MyEvaluor", "fraction=" + fraction + ";startValue=" + startValue + "endValue=" + endValue);
            return super.evaluate(fraction, startValue, endValue);
        }
    }

    /**
     * ﻿﻿
     * 圆点坐标：(x0,y0)
     * 半径：r
     * 角度：a0
     * 则圆上任一点为：（x1,y1）
     * x1   =   x0   +   r   *   cos(ao   *   3.14   /180   )
     * y1   =   y0   +   r   *   sin(ao   *   3.14   /180   )
     */
    private void startAnimator1() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(90, 360);
        valueAnimator.setEvaluator(new MyEvaluor());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                point.x = (float) Math.cos(value * (Math.PI / 180)) * 100 - 100;
                point.y = (float) -Math.sin(value * (Math.PI / 180)) * 100;
                views.get(0).setTranslationX(point.x);
                views.get(0).setTranslationY(point.y);
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(750);
        valueAnimator.start();


        ValueAnimator valueAnimator2 = ValueAnimator.ofFloat(180, 0, -180);
        valueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                point.x = (float) Math.cos(value * (Math.PI / 180)) * 100 + 100;
                point.y = (float) -Math.sin(value * (Math.PI / 180)) * 100;
                views.get(0).setTranslationX(point.x);
                views.get(0).setTranslationY(point.y);
            }
        });
        valueAnimator2.setInterpolator(new LinearInterpolator());
        valueAnimator2.setDuration(1000);
        valueAnimator2.setStartDelay(750);
        valueAnimator2.start();


        ValueAnimator valueAnimator3 = ValueAnimator.ofFloat(0, 90);
        valueAnimator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                point.x = (float) Math.cos(value * (Math.PI / 180)) * 100 - 100;
                point.y = (float) -Math.sin(value * (Math.PI / 180)) * 100;
                views.get(0).setTranslationX(point.x);
                views.get(0).setTranslationY(point.y);
            }
        });
        valueAnimator3.setInterpolator(new LinearInterpolator());
        valueAnimator3.setDuration(250);
        valueAnimator3.setStartDelay(1750);
        valueAnimator3.start();
        /**动画组合->让左右同时执行**/
        AnimatorSet   animatorSet = new AnimatorSet();
        animatorSet.play(valueAnimator2).with(valueAnimator3);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.start();

        valueAnimator3.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                startAnimator1();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void startAnimator2() {

        ValueAnimator valueAnimator2 = ValueAnimator.ofFloat(180, 0, -180);
        valueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                /**
                 * ﻿﻿
                 * 圆点坐标：(x0,y0)
                 * 半径：r
                 * 角度：a0
                 * 则圆上任一点为：（x1,y1）
                 * x1   =   x0   +   r   *   cos(ao   *   3.14   /180   )
                 * y1   =   y0   +   r   *   sin(ao   *   3.14   /180   )
                 */
                /**第四步，根据每个菜单真实角度计算其坐标值**/
                point.x = (float) Math.cos(value * (Math.PI / 180)) * 100 + 100;
                point.y = (float) -Math.sin(value * (Math.PI / 180)) * 100;
                views.get(1).setTranslationX(point.x);
                views.get(1).setTranslationY(point.y);
            }
        });
        valueAnimator2.setInterpolator(new LinearInterpolator());
        valueAnimator2.setDuration(1000);
        valueAnimator2.start();

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 360);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                /**
                 * ﻿﻿
                 * 圆点坐标：(x0,y0)
                 * 半径：r
                 * 角度：a0
                 * 则圆上任一点为：（x1,y1）
                 * x1   =   x0   +   r   *   cos(ao   *   3.14   /180   )
                 * y1   =   y0   +   r   *   sin(ao   *   3.14   /180   )
                 */
                /**第四步，根据每个菜单真实角度计算其坐标值**/
                point.x = (float) Math.cos(value * (Math.PI / 180)) * 100 - 100;
                point.y = (float) -Math.sin(value * (Math.PI / 180)) * 100;
                views.get(1).setTranslationX(point.x);
                views.get(1).setTranslationY(point.y);
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(1000);
        valueAnimator.setStartDelay(1000);
        valueAnimator.start();
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                startAnimator2();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void startAnimator3() {

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(270, 180);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                /**
                 * ﻿﻿
                 * 圆点坐标：(x0,y0)
                 * 半径：r
                 * 角度：a0
                 * 则圆上任一点为：（x1,y1）
                 * x1   =   x0   +   r   *   cos(ao   *   3.14   /180   )
                 * y1   =   y0   +   r   *   sin(ao   *   3.14   /180   )
                 */
                /**第四步，根据每个菜单真实角度计算其坐标值**/
                point.x = (float) Math.cos(value * (Math.PI / 180)) * 100 + 100;
                point.y = (float) -Math.sin(value * (Math.PI / 180)) * 100;
                views.get(2).setTranslationX(point.x);
                views.get(2).setTranslationY(point.y);
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(250);
        valueAnimator.start();

        ValueAnimator valueAnimator2 = ValueAnimator.ofFloat(0, 360);
        valueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                /**
                 * ﻿﻿
                 * 圆点坐标：(x0,y0)
                 * 半径：r
                 * 角度：a0
                 * 则圆上任一点为：（x1,y1）
                 * x1   =   x0   +   r   *   cos(ao   *   3.14   /180   )
                 * y1   =   y0   +   r   *   sin(ao   *   3.14   /180   )
                 */
                /**第四步，根据每个菜单真实角度计算其坐标值**/
                point.x = (float) Math.cos(value * (Math.PI / 180)) * 100 - 100;
                point.y = (float) -Math.sin(value * (Math.PI / 180)) * 100;
                views.get(2).setTranslationX(point.x);
                views.get(2).setTranslationY(point.y);
            }
        });
        valueAnimator2.setInterpolator(new LinearInterpolator());
        valueAnimator2.setDuration(1000);
        valueAnimator2.setStartDelay(250);
        valueAnimator2.start();


        ValueAnimator valueAnimator3 = ValueAnimator.ofFloat(180, -90);
        valueAnimator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                /**
                 * ﻿﻿
                 * 圆点坐标：(x0,y0)
                 * 半径：r
                 * 角度：a0
                 * 则圆上任一点为：（x1,y1）
                 * x1   =   x0   +   r   *   cos(ao   *   3.14   /180   )
                 * y1   =   y0   +   r   *   sin(ao   *   3.14   /180   )
                 */
                /**第四步，根据每个菜单真实角度计算其坐标值**/
                point.x = (float) Math.cos(value * (Math.PI / 180)) * 100 + 100;
                point.y = (float) -Math.sin(value * (Math.PI / 180)) * 100;
                views.get(2).setTranslationX(point.x);
                views.get(2).setTranslationY(point.y);
            }
        });
        valueAnimator3.setInterpolator(new LinearInterpolator());
        valueAnimator3.setDuration(750);
        valueAnimator3.setStartDelay(1250);
        valueAnimator3.start();
        valueAnimator3.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                startAnimator3();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
