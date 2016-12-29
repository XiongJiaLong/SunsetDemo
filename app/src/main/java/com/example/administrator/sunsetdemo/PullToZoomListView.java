package com.example.administrator.sunsetdemo;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/12/9.
 */

public class PullToZoomListView extends ListView {
    //  头部view的容器
    private FrameLayout mHeadViewCont;
    //  头部图片
    private ImageView mHeaderImg;
    //  屏幕高度
    private int mScreenHeight;
    //  屏幕高度
    private int mScreenWidth;
    //  头部图片的高度
    private int mHeaderHeight;
    //  动画执行时间
    private static final int ANIM_TIME = 200;
    //  手指触摸点
    private float lastX;
    private float lastY;

    protected int mActivePointerId = INVALID_POINTER;

    //上一次放大的倍数
    private float mLastScale;
    //当前的放大倍数
    private float mScaleNow;
    //  放大的最大倍数
    private static final float mMaxScale = 2.0f;
    //是否禁用listView的相应事件
    private boolean isNeedCancelList = false;
    //滑动监听接口
    private OnScrollListener mScrollListener;

    //点击到了无效的点
    private static final int INVALID_POINTER = -1;


    public PullToZoomListView(Context context) {
        super(context);
        init(context);
    }

    public PullToZoomListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PullToZoomListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        //创建头部容器
        mHeadViewCont = new FrameLayout(context);
        //获取屏幕尺寸信息
        getScreenDisplay(context);
        //设置头部View容器的初始大小,图片高度设为屏幕宽度的9/16
        mHeaderHeight = (int)((9*1.0f/16)*mScreenWidth);
        mHeadViewCont.setLayoutParams(new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,mHeaderHeight));
        //创建图片显示的view
        mHeaderImg = new ImageView(context);
        mHeaderImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mHeaderImg.setLayoutParams(lp);
        mHeaderImg.setImageResource(R.mipmap.a123);
        //将图片添加到容器中
        mHeadViewCont.addView(mHeaderImg);
        addHeaderView(mHeadViewCont);
        super.setOnScrollListener(new InternalScrollListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                //获取点击点的x，y位置
                lastX = MotionEventCompat.getX(ev, MotionEventCompat.getActionIndex(ev));
                lastY = MotionEventCompat.getY(ev, MotionEventCompat.getActionIndex(ev));
                stopAnim();
                //记录当前缩放比例
                mLastScale = (this.mHeadViewCont.getBottom() / this.mHeaderHeight);
                //当按下时，listView点击事件设为有效
                isNeedCancelList = true;
                break;
            case MotionEvent.ACTION_MOVE:
                int index = MotionEventCompat.getActionIndex(ev);
                mActivePointerId = MotionEventCompat.getPointerId(ev,index);
                if (false){
                    finishPull();
                    isNeedCancelList = true;
                    break;
                }else {
                    if (mHeadViewCont.getBottom() >= mHeaderHeight) {
                        ViewGroup.LayoutParams params = this.mHeadViewCont.getLayoutParams();
                        final float y = MotionEventCompat.getY(ev, index);
                        float dy = y - lastY;
                        float f = ((y - this.lastY + this.mHeadViewCont
                                .getBottom()) / this.mHeaderHeight - this.mLastScale)
                                / 2.0F + this.mLastScale;
                        if ((this.mLastScale <= 1.0D) && (f <= this.mLastScale)) {
                            params.height = this.mHeaderHeight;
                            this.mHeadViewCont.setLayoutParams(params);
                            return super.onTouchEvent(ev);
                        }
                     /*这里设置紧凑度*/
                        dy = dy * 0.5f * (mHeaderHeight * 1.0f / params.height);
                        mLastScale = (dy + params.height) * 1.0f / mHeaderHeight;
                        mScaleNow = clamp(mLastScale, 1.0f, mMaxScale);

                        // Log.v(“zgy”, “=======mScale=====” + mLastScale+”,f = “+f);
                        params.height = (int) (mHeaderHeight * mScaleNow);
                        mHeadViewCont.setLayoutParams(params);
                        lastY = y;
                     /*这里，如果图片有放大，则屏蔽ListView 的其他事件响应*/
                        if(isNeedCancelList ){
                            isNeedCancelList = false;
                            MotionEvent motionEvent = MotionEvent.obtain(ev);
                            motionEvent.setAction(MotionEvent.ACTION_CANCEL);
                            super.onTouchEvent(motionEvent);
                        }
                        return true;
                    }
                    lastY = MotionEventCompat.getY(ev, index);

                }
                break;
            case MotionEvent.ACTION_UP:
                finishPull();
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void getScreenDisplay(Context context){
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenHeight = metrics.heightPixels;
        mScreenWidth = metrics.widthPixels;
    }

    public void setOnScrollListener(OnScrollListener onScrollListener){
        mScrollListener = onScrollListener;
    }

    //计算 当前放大倍数，和缩小倍数，和最大放大倍数之间的最小值
    private float clamp(float value,float min ,float max){
        return Math.min(Math.max(value,min),max);
    }

    /**
     * 停止回弹动画，回收资源
     * */
    private void stopAnim(){
        //
    }

    /**
     * 移动一定距离到达最大值自动结束下拉动作，开始回弹刷新
     * **/
    private void finishPull(){
        if (mHeadViewCont.getBottom() > mHeaderHeight){
            pullBackAnimation();
        }
    }
    /**
     * 属性动画回弹头部放大的图片
     * **/
    private void pullBackAnimation(){
        ValueAnimator pullBack = ValueAnimator.ofFloat(mScaleNow,1.0f);
        pullBack.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                LayoutParams lp = (LayoutParams) mHeadViewCont.getLayoutParams();
                lp.height = (int)(mHeaderHeight*value);
                mHeadViewCont.setLayoutParams(lp);
            }
        });
        pullBack.setDuration((long)(ANIM_TIME*mScaleNow));
        pullBack.start();
    }

    private class InternalScrollListener implements OnScrollListener{

        @Override
        public void onScrollStateChanged(AbsListView absListView, int scrollState) {
            if (mScrollListener != null){
                mScrollListener.onScrollStateChanged(absListView,scrollState);
            }
        }

        @Override
        public void onScroll(AbsListView absListView,
                             int firstVisibleItem,
                             int visibleItemCount,
                             int totalItemCount) {
            if (mScrollListener != null){
                mScrollListener.onScroll(
                        absListView,firstVisibleItem,visibleItemCount,totalItemCount);
            }
        }
    }
}
