#【Android自定义View实战】之仿百度加载动画，一种优雅的Loading方式

>无意中看到了百度的加载动画，看起来非常优雅，打算亲手造一个。

 ![效果图](https://github.com/linglongxin24/BaiduProgressBar/blob/master/screenshots/baiduloading.gif?raw=true)



>仿百度加载动画的重要思路：当第一遍执行完毕后就让第一个停下来在中间位置，换原来中间位置的第三个开始执行动画，
 以此类推，当第二遍执行完毕后第二个停下来，中间位置的开始执行动画。
 
 ![效果图](https://github.com/linglongxin24/BaiduProgressBar/blob/master/screenshots/effect.gif?raw=true)

#第一个：仿百度加载动画，用ObjectAnimator属性动画操作ImageView的属性方法实现：

 * 1.布局文件

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="500px"
    android:layout_height="500px"
    android:orientation="vertical">

    <ImageView
        android:id="@+id/iv_blue"
        android:layout_width="wrap_content"
        android:scaleType="matrix"
        android:layout_height="wrap_content"
        android:src="@mipmap/dot_blue"
        android:layout_gravity="center" />
    <ImageView
        android:id="@+id/iv_yellow"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:scaleType="matrix"
        android:src="@mipmap/dot_yellow"
        android:layout_gravity="center" />
    <ImageView
        android:id="@+id/iv_red"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:scaleType="matrix"
        android:src="@mipmap/dot_red"
        android:layout_gravity="center" />
</FrameLayout>
```

 * 2.代码

```java
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

```
#第二个：仿百度加载动画第二种实现方式，用ValueAnimator+原生的ondraw()方法实现：

```java
package cn.bluemobi.dylan.baiduprogressbar;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 仿百度优雅的加载动画
 * Created by dylan on 2016-12-04.
 */

public class BaiduProgressBar2 extends View {
    /**
     * 开始执行的第一个动画的索引，
     * 由于第一个和第二个同时当执行，
     * 当第一遍执行完毕后就让第一个停下来在中间位置，换原来中间位置的第三个开始执行动画，
     * 以此类推，当第二遍执行完毕后第二个停下来，中间位置的开始执行动画。
     */
    private int sweepIndex = 0;
    /**
     * 交换执行动画的颜色数组
     */
    private int[] colors = new int[]{getResources().getColor(R.color.colorYellow),
            getResources().getColor(R.color.colorRed),
            getResources().getColor(R.color.colorBlue)};

    /**
     * 动画所执行的最大偏移量（即中间点和最左边的距离）
     */
    private Float maxWidth = 200f;

    /**
     * 三个圆的半径
     */
    private Float radius = 30f;

    /**
     * 当前偏移的X坐标
     */
    private Float currentX = 0f;
    /**
     * 画笔
     */
    private Paint paint;
    /**
     * 属性动画
     */
    private ValueAnimator valueAnimator;

    public BaiduProgressBar2(Context context) {
        super(context);
        startAnimator();
    }

    public BaiduProgressBar2(Context context, AttributeSet attrs) {
        super(context, attrs);
        startAnimator();
    }

    public BaiduProgressBar2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        startAnimator();
    }

    /**
     * 用属性动画实现位移动画
     */
    private void startAnimator() {
        valueAnimator = ValueAnimator.ofFloat(0f, maxWidth, 0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentX = (Float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
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
                sweep(sweepIndex);
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        valueAnimator.setRepeatCount(-1);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.setDuration(1000);
        valueAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        /**画左边的圆**/
        paint.setColor(colors[0]);
        canvas.drawCircle(centerX - currentX, centerY, radius, paint);

        /**画右边的圆**/
        paint.setColor(colors[1]);
        canvas.drawCircle(centerX + currentX, centerY, radius, paint);

        /**画中间的圆**/
        paint.setColor(colors[2]);
        canvas.drawCircle(centerX, centerY, radius, paint);

    }

    /**
     * 每次让先执行动画的目标和中间停止的动画目标交换
     *
     * @param a 最先执行的动画的索引
     */
    private void sweep(int a) {
        int temp = colors[2];
        colors[2] = colors[a];
        colors[a] = temp;

        if (a == 0) {
            sweepIndex = 1;
        } else {
            sweepIndex = 0;
        }
    }

    /**
     * 在View销毁时停止动画
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        valueAnimator.cancel();
    }
}

```

>在经过以上的动画之后，突然在[Loading设计思路分享](http://www.ui.cn/detail/73226.html)中看到了两个比较酷炫的动画
主要思路图如下

 ![效果图](https://github.com/linglongxin24/BaiduProgressBar/blob/master/screenshots/demo.gif?raw=true)
 ![效果图](https://github.com/linglongxin24/BaiduProgressBar/blob/master/screenshots/sl1.jpg?raw=true)
 ![效果图](https://github.com/linglongxin24/BaiduProgressBar/blob/master/screenshots/sl2.gif?raw=true)
 
#第三个：扔球动画->水平旋转动画

```java
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

```


#第四个：扔球动画->垂直旋转动画

```java
package cn.bluemobi.dylan.baiduprogressbar;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
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

public class BaiduProgressLoading extends FrameLayout {

    /**
     * 存放三个小球的集合
     */
    private List<ImageView> views = new ArrayList<>();

    public BaiduProgressLoading(Context context) {
        super(context);
        init();
    }

    public BaiduProgressLoading(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaiduProgressLoading(Context context, AttributeSet attrs, int defStyleAttr) {
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

    private void startAnimator1() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(90, 360);
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
                views.get(0).setTranslationX(point.x);
                views.get(0).setTranslationY(point.y);
            }
        });
        valueAnimator3.setInterpolator(new LinearInterpolator());
        valueAnimator3.setDuration(250);
        valueAnimator3.setStartDelay(1750);
        valueAnimator3.start();
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

```

#[GitHub](https://github.com/linglongxin24/BaiduProgressBar)
