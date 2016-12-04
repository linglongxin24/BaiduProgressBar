package cn.bluemobi.dylan.baiduprogressbar;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.util.AttributeSet;
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

    private int startIndex = 0;
    private int[] src = new int[]{R.mipmap.dot_yellow, R.mipmap.dot_red, R.mipmap.dot_blue};
    private List<ImageView> views = new ArrayList<>();

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

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.baidu_progress_bar, this, true);
        assignViews();
        startAnimator1();
        startAnimator2();
        startAnimator3();
    }

    private void startAnimator1() {
        /**动画组合**/
        PropertyValuesHolder objectAnimatorTranslation = PropertyValuesHolder.ofFloat("translationX", -100, -200, -100, 0, 100, 200, 100, 0, -100);
        PropertyValuesHolder objectAnimatorScale = PropertyValuesHolder.ofFloat("scaleX", 0.5f, 1, 1.5f, 1, 0.5f, 1, 1.5f, 1, 0.5f);
        PropertyValuesHolder objectAnimatorScaleY = PropertyValuesHolder.ofFloat("scaleY", 0.5f, 1, 1.5f, 1, 0.5f, 1, 1.5f, 1, 0.5f);
        /**同时播放两个动画**/
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(views.get(0), objectAnimatorTranslation, objectAnimatorScale, objectAnimatorScaleY);
        objectAnimator.setRepeatCount(-1);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setDuration(2000);
        objectAnimator.start();

    }

    private void startAnimator2() {
        /**动画组合**/
        PropertyValuesHolder objectAnimatorTranslation = PropertyValuesHolder.ofFloat("translationX", 0, 100, 200, 100, 0, -100, -200, -100, 0);
        PropertyValuesHolder objectAnimatorScale = PropertyValuesHolder.ofFloat("scaleX", 1, 0.5f, 1, 1.5f, 1, 0.5f, 1, 1.5f, 1);
        PropertyValuesHolder objectAnimatorScaleY = PropertyValuesHolder.ofFloat("scaleY", 1, 0.5f, 1, 1.5f, 1, 0.5f, 1, 1.5f, 1);
        /**同时播放两个动画**/
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(views.get(1), objectAnimatorTranslation, objectAnimatorScale, objectAnimatorScaleY);
        objectAnimator.setRepeatCount(-1);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setDuration(2000);
        objectAnimator.start();

    }

    private void startAnimator3() {
        /**动画组合**/
        PropertyValuesHolder objectAnimatorTranslation = PropertyValuesHolder.ofFloat("translationX", 100, 0, -100, -200, -100, 0, 100, 200, 100);
        PropertyValuesHolder objectAnimatorScale = PropertyValuesHolder.ofFloat("scaleX", 1.5f, 1f, 0.5f, 1, 1.5f, 1, 0.5f, 1, 1.5f);
        PropertyValuesHolder objectAnimatorScaleY = PropertyValuesHolder.ofFloat("scaleY", 1.5f, 1f, 0.5f, 1, 1.5f, 1, 0.5f, 1, 1.5f);
        /**同时播放两个动画**/
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(views.get(2), objectAnimatorTranslation, objectAnimatorScale, objectAnimatorScaleY);
        objectAnimator.setRepeatCount(-1);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setDuration(2000);
        objectAnimator.start();

    }

}
