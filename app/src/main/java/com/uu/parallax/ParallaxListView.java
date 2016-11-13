package com.uu.parallax;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ListView;

import static android.content.ContentValues.TAG;

/**
 * Created by penghao on 2016/11/13.
 * <p>
 * <p>
 * 带头部视差的listView
 */

public class ParallaxListView extends ListView {
    private ImageView imageView; //头部视图中的listView
    private int orignalHeight;  //图片的原始高度
    private int maxHeight;

    public ParallaxListView(Context context) {
        super(context);
    }

    public ParallaxListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParallaxListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void setImageView(final ImageView imageView) {
        this.imageView = imageView;

        //添加全局视图树的监听
        imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {


            @Override
            public void onGlobalLayout() {
                //移除监听，获取图片的原始宽高
                imageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                orignalHeight = imageView.getHeight();
                //获取图片高度，
                int drawableHeight = imageView.getDrawable().getIntrinsicHeight();
                //如果图片的像素高，就用，绘制出的高度高 ，就成放大图片,不然就会出现，一拖拽就变小的情况
                maxHeight = orignalHeight > drawableHeight ? orignalHeight * 2 : drawableHeight;
            }
        });
    }

    /**
     * 在listview滑动到头的时候执行，可以获取到滑动的距离和方向
     *
     * @param deltaX         继续滑动x方向的距离
     * @param deltaY         继续滑动y 方向的距离        负值：表示顶部到头      正直:表示底部到头
     * @param maxOverScrollX x方向最大可以滑动的距离
     * @param maxOverScrollY y方向最大可以滑动的距离
     * @param isTouchEvent   是否在用手指滑动：  true:手指滑动，  false:  依靠惯性滑动
     * @return
     */
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY
            , int scrollRangeX, int scrollRangeY, int maxOverScrollX,
                                   int maxOverScrollY, boolean isTouchEvent) {


        if (deltaY < 0 && isTouchEvent) { //顶部到头，而且是手指正在触摸
            //不断增加高度-------
            int newHeight = imageView.getHeight() - deltaY;
            if (newHeight > maxHeight) {
                newHeight = maxHeight;
            }
            //设置参数更新控件的高
            imageView.getLayoutParams().height = newHeight;
            imageView.requestLayout(); //deltaY;//  负值
        }
        Log.e(TAG, "deltaY == " + deltaY);

        return super.overScrollBy(deltaX, deltaY, scrollX,
                scrollY, scrollRangeX, scrollRangeY,
                maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    /**
     * 手指抬起时，图片设置成原来的高度，
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            //利用属性动画将   图pain大小  慢慢变为 原来的高度
            ValueAnimator valueAnimator = ValueAnimator.ofInt(imageView.getHeight(), orignalHeight);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    //实时更新控件
                    int height = (Integer) animation.getAnimatedValue();
                    imageView.getLayoutParams().height = height;
                    imageView.requestLayout();
                }
            });
            valueAnimator.setDuration(350);
            valueAnimator.setInterpolator(new OvershootInterpolator(5));  //增加一个插补器，实现弹性效果
            valueAnimator.start();
        }
        return super.onTouchEvent(ev);
    }
}
