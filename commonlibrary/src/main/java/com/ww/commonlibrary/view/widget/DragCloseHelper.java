package com.ww.commonlibrary.view.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

import androidx.annotation.FloatRange;

import com.ww.commonlibrary.util.LogUtils;
import com.ww.commonlibrary.util.ScreenUtils;

import java.util.ArrayList;


public class DragCloseHelper {
    private ViewConfiguration viewConfiguration;

    // exit anim duration
    private final static long DURATION = 200;

    // 滑动退出的距离阀
    private final static int MAX_EXIT_Y = 350;
    private int maxExitY = MAX_EXIT_Y;

    //缩放模式的最小scale 不缩放设为1.0f
    private static final float MIN_SCALE = 0.6F;
    private float minScale = MIN_SCALE;

    //是否滑动中
    private boolean isSwipingToClose;

    /**
     * 上次触摸坐标
     */
    private float mLastY = -1, mLastRawY = -1, mLastX = -1, mLastRawX = -1;
    /**
     * 上次触摸手指id
     */
    private int lastPointerId;
    /**
     * 当前位移距离
     */
    private float mCurrentTranslationY, mCurrentTranslationX;
    /**
     * 上次位移距离
     */
    private float mLastTranslationY, mLastTranslationX;
    /**
     * 正在恢复原位中
     */
    private boolean isRevertingAnimate = false;
    /**
     * 共享元素模式
     */
    private boolean isShareElementMode = false;

    /**
     * 状态栏高度
     */
    private int statusBarHeight;


    private View parentV, childV;

    private DragCloseListener dragCloseListener;
    private ClickListener clickListener;

    private LongClickListener longClickListener;
    // 缩放模式 仿微信
    private boolean canScaleMode = false;
    private GestureDetector gestureDetector;


    // 头条小视频不允许上滑
    private boolean canVideoMode = false;

    public DragCloseHelper(Context context) {
        gestureDetector = new GestureDetector(context, new MySimpleGestureListener());
        viewConfiguration = ViewConfiguration.get(context);
        statusBarHeight = ScreenUtils.getStatusBarHeight(context);
    }

    public void setDragCloseListener(DragCloseListener dragCloseListener) {
        this.dragCloseListener = dragCloseListener;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    /**
     * 设置共享元素模式
     */
    public void setShareElementMode(boolean shareElementMode) {
        isShareElementMode = shareElementMode;
    }

    /**
     * 是否缩放
     *
     * @param b
     */
    public void setCanScaleMode(boolean b) {
        this.canScaleMode = b;
        if (b) {
            minScale = MIN_SCALE;
        } else {
            minScale = 1.0f;
        }
    }

    public void setLongClickListener(LongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public void setCanVideoMode(boolean canVideoMode) {
        this.canVideoMode = canVideoMode;
    }

    /**
     * 设置拖拽关闭的view
     *
     * @param parentV
     * @param childV
     */
    public void setDragCloseViews(View parentV, View childV) {
        this.parentV = parentV;
        this.childV = childV;
    }

    /**
     * 设置最大退出距离
     *
     * @param maxExitY
     */
    public void setMaxExitY(int maxExitY) {
        this.maxExitY = maxExitY;
    }

    /**
     * 设置最小缩放尺寸
     *
     * @param minScale
     */
    public void setMinScale(@FloatRange(from = 0.1f, to = 1.0f) float minScale) {
        this.minScale = minScale;
    }

    /**
     * 处理点击事件
     */
    private void dealClickEvent() {
        if (clickListener != null) {
            clickListener.onClick(childV);
        }
    }

    /**
     * 处理touch事件
     *
     * @param event
     * @return
     */
    public boolean handleEvent(MotionEvent event) {
        if (event.getPointerCount() > 1) {
            //如果有多个手指
            if (isSwipingToClose) {
                //已经开始滑动关闭，恢复原状，否则需要派发事件
                isSwipingToClose = false;
                resetCallBackAnimation();
                return true;
            }
            reset();
            return false;
        } else if (dragCloseListener != null && dragCloseListener.intercept()) {
            //被接口中的方法拦截，但是如果设置了点击事件，将继续执行点击逻辑
            if (clickListener != null) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //界面保存按钮 不处理
                    if (getTouchTarget(parentV, (int) event.getX(), (int) event.getY()) != null && getTouchTarget(parentV, (int) event.getX(), (int) event.getY()) instanceof Button) {
                        return false;
                    }
                } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    //
                }
            }
            isSwipingToClose = false;
            return false;
        } else {
            //不拦截
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                reset();
                LogUtils.e("currentDown" + event.getRawY());
                mLastY = event.getY();
                mLastX = event.getX();
                mLastRawY = event.getRawY();
                mLastRawX = event.getRawX();
                if (isInvalidTouch()) {
                    //触摸点在状态栏的区域 或者 是无效触摸区域，则需要拦截
                    return true;
                }
                //初始化数据
                lastPointerId = event.getPointerId(0);
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                float currentY = event.getY();
                float currentX = event.getX();
                if (isInvalidTouch()) {
                    //无效触摸区域，则需要拦截
                    return true;
                }
                //小视频模式不让上滑
                if (canVideoMode) {
                    if (currentY <= mLastRawY) {
                        return isSwipingToClose;
                    }
                }
                if (mLastRawY == -1) {
                    //解决触摸底部，部分有虚拟导航栏的手机会出现先move后down的问题，因此up和cancel的时候需要重置为-1
                    return true;
                }
                if (lastPointerId != event.getPointerId(0)) {
                    //手指不一致，恢复原状
                    if (isSwipingToClose) {
                        resetCallBackAnimation();
                    }
                    reset();
                    return true;
                }
                if (isSwipingToClose ||
                        ( Math.abs(currentY - mLastY) > Math.abs(currentX - mLastX) * 1.5)) {
                    //已经触发或者开始触发，更新view
                    mLastY = currentY;
                    mLastX = currentX;
                    float currentRawY = event.getRawY();
                    float currentRawX = event.getRawX();
                    if (!isSwipingToClose) {
                        //准备开始
                        isSwipingToClose = true;
                        if (dragCloseListener != null) {
                            dragCloseListener.dragStart();
                        }
                    }
                    mCurrentTranslationY = currentRawY - mLastRawY + mLastTranslationY ;
                    mCurrentTranslationX = currentRawX - mLastRawX + mLastTranslationX;

                    float percent;
                    percent = 1 - Math.abs(Math.abs(mCurrentTranslationY) / childV.getHeight());
                    if (percent > 1) {
                        percent = 1;
                    } else if (percent < 0) {
                        percent = 0;
                    }
                    parentV.getBackground().mutate().setAlpha((int) (percent * 255));
                    if (dragCloseListener != null) {
                        dragCloseListener.dragging(percent);
                    }
                    if (percent < minScale) {
                        percent = minScale;
                    }
                    childV.setTranslationY(mCurrentTranslationY);
                    if (canScaleMode) {
                        childV.setTranslationX(mCurrentTranslationX);
                        childV.setScaleX(percent);
                        childV.setScaleY(percent);
                    }
                    return true;
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                if (isInvalidTouch()) {
                    //无效触摸区域，则需要拦截
                    return true;
                }
                //界面保存按钮 不处理
                if (getTouchTarget(parentV, (int) event.getX(), (int) event.getY()) != null && getTouchTarget(parentV, (int) event.getX(), (int) event.getY()) instanceof Button) {
                    return false;
                }
                mLastRawY = -1;
                //手指抬起事件
                if (isSwipingToClose) {
                    if (mCurrentTranslationY > maxExitY) {
                        if (isShareElementMode) {
                            //会执行共享元素的离开动画
                            if (dragCloseListener != null) {
                                dragCloseListener.dragClose(true);
                            }
                        } else {
                            //会执行定制的离开动画
                            exitWithTranslation(mCurrentTranslationY);
                        }
                    } else {
                        resetCallBackAnimation();
                    }
                    isSwipingToClose = false;
                    return true;
                }
            } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                mLastRawY = -1;
                if (isSwipingToClose) {
                    resetCallBackAnimation();
                    isSwipingToClose = false;
                    return true;
                }
            }
        }
        return gestureDetector.onTouchEvent(event);
    }

    /**
     * 退出动画
     *
     * @param currentY
     */
    private void exitWithTranslation(float currentY) {
        int targetValue = currentY > 0 ? childV.getHeight() : -childV.getHeight();
        ValueAnimator anim = ValueAnimator.ofFloat(mCurrentTranslationY, targetValue);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                DragCloseHelper.this.updateChildView(mCurrentTranslationX, (float) animation.getAnimatedValue());
            }
        });
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                if (dragCloseListener != null) {
                    parentV.getBackground().mutate().setAlpha(0);
                    dragCloseListener.dragClose(false);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.setDuration(DURATION);
        anim.setInterpolator(new LinearInterpolator());
        anim.start();
    }

    /**
     * 重置数据
     */
    private void reset() {
        isSwipingToClose = false;
        mLastY = -1;
        mLastX = -1;
        mLastRawY = -1;
        mLastRawX = -1;
        mLastTranslationY = 0;
        mLastTranslationX = 0;
    }

    /**
     * 更新缩放的view
     */
    private void updateChildView(float transX, float transY) {
        float percent = Math.abs(transY / childV.getHeight());
        float scale = 1 - percent;
        if (scale < minScale) {
            scale = minScale;
        }
        childV.setTranslationY(transY);
        if (canScaleMode) {
            childV.setTranslationX(transX);
            childV.setScaleX(scale);
            childV.setScaleY(scale);
        }
    }

    /**
     * 恢复到原位动画
     */
    private void resetCallBackAnimation() {
        if (isRevertingAnimate || mCurrentTranslationY == 0) {
            return;
        }
        final float ratio = mCurrentTranslationX / mCurrentTranslationY;
        ValueAnimator animatorY = ValueAnimator.ofFloat(mCurrentTranslationY, 0);
        animatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (isRevertingAnimate) {
                    mCurrentTranslationY = (float) valueAnimator.getAnimatedValue();
                    mCurrentTranslationX = ratio * mCurrentTranslationY;
                    mLastTranslationY = mCurrentTranslationY;
                    mLastTranslationX = mCurrentTranslationX;
                    updateChildView(mLastTranslationX, mCurrentTranslationY);
                }
            }
        });
        animatorY.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isRevertingAnimate = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isRevertingAnimate) {
                    parentV.getBackground().mutate().setAlpha(255);
                    mCurrentTranslationY = 0;
                    mCurrentTranslationX = 0;
                    isRevertingAnimate = false;
                    if (dragCloseListener != null) {
                        dragCloseListener.dragCancel();
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorY.setDuration(DURATION).start();
    }

    public interface DragCloseListener {
        /**
         * 是否有拦截
         *
         * @return
         */
        boolean intercept();

        /**
         * 开始拖拽
         */
        void dragStart();

        /**
         * 拖拽中
         *
         * @param percent
         */
        void dragging(float percent);

        /**
         * 取消拖拽
         */
        void dragCancel();

        /**
         * 拖拽结束并且关闭
         *
         * @param isShareElementMode
         */
        void dragClose(boolean isShareElementMode);
    }

    public interface ClickListener {
        /**
         * 点击事件
         */
        void onClick(View view);
    }

    public interface LongClickListener {
        /**
         * 点击事件
         */
        void onLongClick(View view);
    }

    /**
     * 是否有效点击，如果点击到了状态栏区域 或者 虚拟导航栏区域，则无效
     */
    private boolean isInvalidTouch() {
        return mLastRawY < statusBarHeight;
    }


    //根据坐标返回触摸到的View
    private View getTouchTarget(View rootView, int x, int y) {
        View targetView = null;
        // 获取clickable view
        ArrayList<View> touchableViews = rootView.getTouchables();
        for (View touchableView : touchableViews) {
            if (isTouchPointInView(touchableView, x, y)) {
                targetView = touchableView;
                break;
            }
        }
        return targetView;
    }

    //(x,y)是否在view的区域内
    private boolean isTouchPointInView(View view, int x, int y) {
        if (view == null) {
            return false;
        }
        int[] position = new int[2];
        view.getLocationOnScreen(position);
        int left = position[0];
        int top = position[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        return x >= left && x <= right && y >= top && y <= bottom;
    }


    public class MySimpleGestureListener extends GestureDetector.SimpleOnGestureListener {
        // 处理点击事件
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            dealClickEvent();
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            if (longClickListener != null) {
                longClickListener.onLongClick(childV);
            }

        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return false;
        }
    }

}
