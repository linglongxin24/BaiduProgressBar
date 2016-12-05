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
 * 仿百度优雅的加载动画
 * Created by dylan on 2016-12-04.
 */

public class BaiduProgressBar extends FrameLayout {
    /**
     * 开始执行的第一个动画的索引，
     * 由于第一个和第二个同时当执行，
     * 当第一遍执行完毕后就让第一个停下来在中间位置，换原来中间位置的第三个开始执行动画，
     * 以此类推，当第二遍执行完毕后第二个停下来，中间位置的开始执行动画。
     */
    private int startIndex = 0;
    /**
     * 交换执行动画的源图片数组
     */
    private int[] src = new int[]{R.mipmap.dot_yellow, R.mipmap.dot_red, R.mipmap.dot_blue};
    /**
     * 存放三个ImageView的的集合
     */
    private List<ImageView> views = new ArrayList<>();
    /**
     * 让左边和右边动画同时执行的AnimatorSet对象
     */
    private AnimatorSet animatorSet;

    /**
     * 动画所执行的最大半径（即中间点和最左边的距离）
     */
    private int maxRadius = 200;

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

    /**
     * 查找布局控件
     */
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

    /**
     * 开始执行动画
     */
    private void startAnimator() {
        /**向左来回移动的X位移动画**/
        ObjectAnimator objectAnimatorLeft = ObjectAnimator.ofFloat(views.get(0), "translationX", 0, -maxRadius, 0);
        objectAnimatorLeft.setRepeatCount(-1);
        objectAnimatorLeft.setDuration(1000);

        /**向右来回移动的X位移动画**/
        ObjectAnimator  objectAnimatorRight = ObjectAnimator.ofFloat(views.get(1), "translationX", 0, maxRadius, 0);
        objectAnimatorRight.setRepeatCount(-1);
        objectAnimatorRight.setDuration(1000);

        /**动画组合->让左右同时执行**/
        animatorSet = new AnimatorSet();
        animatorSet.play(objectAnimatorRight).with(objectAnimatorLeft);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.start();

        /**动画监听**/
        objectAnimatorLeft.addListener(new Animator.AnimatorListener() {
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
                /**每次记录一下下次应该停止在中间的Image索引，然后和中间的交换**/
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

    /**
     * 每次让先执行动画的目标和中间停止的动画目标交换
     *
     * @param a 最先执行的动画的索引
     * @param b 在中间动画的索引
     */
    private void sweep(int a, int b) {
        views.get(a).setImageResource(src[b]);
        views.get(b).setImageResource(src[a]);
        int temp = src[b];
        src[b] = src[a];
        src[a] = temp;
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
