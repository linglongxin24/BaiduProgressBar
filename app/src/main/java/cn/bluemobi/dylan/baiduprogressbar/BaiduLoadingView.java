package cn.bluemobi.dylan.baiduprogressbar;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dylan on 2016-12-04.
 */

public class BaiduLoadingView extends FrameLayout {
    /**
     * 存放三个小球的集合
     */
    private List<ImageView> views = new ArrayList<>();
    /**
     * 同时播放动画的对象
     */
    private AnimatorSet animatorSet;

    public BaiduLoadingView(Context context) {
        super(context);
        init();
    }

    public BaiduLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaiduLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
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

    /**
     * 初始化
     */
    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.baidu_progress_bar, this, true);
        assignViews();
        startAnimator();
    }

    private void startAnimator() {
        /**动画组合->让左右同时执行**/
        animatorSet = new AnimatorSet();
        animatorSet.play(startAnimator1()).with(startAnimator2()).with(startAnimator3());
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.start();
    }

    private ObjectAnimator startAnimator1() {
        /**对象的不同属性组合**/
        PropertyValuesHolder objectAnimatorTranslation = PropertyValuesHolder.ofFloat("translationX", -100, -200, -100, 0, 100, 200, 100, 0, -100);
        PropertyValuesHolder objectAnimatorScale = PropertyValuesHolder.ofFloat("scaleX", 0.5f, 1, 1.5f, 1, 0.5f, 1, 1.5f, 1, 0.5f);
        PropertyValuesHolder objectAnimatorScaleY = PropertyValuesHolder.ofFloat("scaleY", 0.5f, 1, 1.5f, 1, 0.5f, 1, 1.5f, 1, 0.5f);
        /**同时操作对象的两个属性动画**/
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(views.get(0), objectAnimatorTranslation, objectAnimatorScale, objectAnimatorScaleY);
        objectAnimator.setRepeatCount(-1);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setDuration(2000);
        objectAnimator.start();
        return objectAnimator;

    }

    private ObjectAnimator startAnimator2() {
        /**对象的不同属性组合**/
        PropertyValuesHolder objectAnimatorTranslation = PropertyValuesHolder.ofFloat("translationX", 0, 100, 200, 100, 0, -100, -200, -100, 0);
        PropertyValuesHolder objectAnimatorScale = PropertyValuesHolder.ofFloat("scaleX", 1, 0.5f, 1, 1.5f, 1, 0.5f, 1, 1.5f, 1);
        PropertyValuesHolder objectAnimatorScaleY = PropertyValuesHolder.ofFloat("scaleY", 1, 0.5f, 1, 1.5f, 1, 0.5f, 1, 1.5f, 1);
        /**同时操作对象的两个属性动画**/
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(views.get(1), objectAnimatorTranslation, objectAnimatorScale, objectAnimatorScaleY);
        objectAnimator.setRepeatCount(-1);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setDuration(2000);
        objectAnimator.start();
        return objectAnimator;

    }

    private ObjectAnimator startAnimator3() {
        /**对象的不同属性组合**/
        PropertyValuesHolder objectAnimatorTranslation = PropertyValuesHolder.ofFloat("translationX", 100, 0, -100, -200, -100, 0, 100, 200, 100);
        PropertyValuesHolder objectAnimatorScale = PropertyValuesHolder.ofFloat("scaleX", 1.5f, 1f, 0.5f, 1, 1.5f, 1, 0.5f, 1, 1.5f);
        PropertyValuesHolder objectAnimatorScaleY = PropertyValuesHolder.ofFloat("scaleY", 1.5f, 1f, 0.5f, 1, 1.5f, 1, 0.5f, 1, 1.5f);
        /**同时操作对象的两个属性动画**/
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(views.get(2), objectAnimatorTranslation, objectAnimatorScale, objectAnimatorScaleY);
        objectAnimator.setRepeatCount(-1);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setDuration(2000);
        objectAnimator.start();
        return objectAnimator;
    }

    /**
     * 在View销毁时停止动画
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        animatorSet.cancel();
    }
}
