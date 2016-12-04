package cn.bluemobi.dylan.baiduprogressbar;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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

public class BaiduProgressBar extends FrameLayout {

    private int startIndex = 0;
    private int[] src = new int[]{R.mipmap.dot_yellow, R.mipmap.dot_red, R.mipmap.dot_blue};
    private List<ImageView> views = new ArrayList<>();

    public BaiduProgressBar(Context context) {
        super(context);
        init();
    }

    public BaiduProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaiduProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
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
        startAnimator();
    }

    private void startAnimator() {

        ObjectAnimator objectAnimatorYellow = ObjectAnimator.ofFloat(views.get(0), "translationX", 0, -100, 0);
        objectAnimatorYellow.setRepeatCount(-1);
        objectAnimatorYellow.setDuration(1000);


        ObjectAnimator objectAnimatorRed = ObjectAnimator.ofFloat(views.get(1), "translationX", 0, 100, 0);
        objectAnimatorRed.setRepeatCount(-1);
        objectAnimatorRed.setDuration(1000);

        /**动画组合**/
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(objectAnimatorRed).with(objectAnimatorYellow);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.start();

        objectAnimatorYellow.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                if (startIndex == 0) {
                    sweep(0, 2);
                    startIndex = 1;
                } else {
                    sweep(1, 2);
                    startIndex = 0;
                }
            }
        });

    }

    private void sweep(int a, int b) {
        views.get(a).setImageResource(src[b]);
        views.get(b).setImageResource(src[a]);
        int temp = src[b];
        src[b] = src[a];
        src[a] = temp;
    }
}
