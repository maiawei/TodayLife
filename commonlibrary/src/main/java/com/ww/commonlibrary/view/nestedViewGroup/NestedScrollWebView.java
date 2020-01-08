package com.ww.commonlibrary.view.nestedViewGroup;

import android.content.Context;

import android.net.http.SslError;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Scroller;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.NestedScrollingChild2;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ViewCompat;

import com.ww.commonlibrary.MyApplication;
import com.ww.commonlibrary.util.NetworkUtil;
import com.ww.commonlibrary.util.PreUtils;

import static com.ww.commonlibrary.CommonConstant.WLAN_SETTING;


public class NestedScrollWebView extends WebView implements NestedScrollingChild2 {

    private final int[] mScrollConsumed = new int[2];
    private NestedWebViewRecyclerViewGroup parent;
    private NestedScrollingChildHelper childHelper;
    private VelocityTracker velocityTracker;
    private Scroller scroller;
    private boolean isSelfFling;
    private boolean hasFling;
    private final float density;
    private int mWebViewContentHeight;
    private int mMaximumVelocity;
    private int maxScrollY;
    private int TouchSlop;
    private int firstY;
    private int lastY;
    private PageFinishedListener pageFinishedListener;

    public NestedScrollWebView(Context context) {
        this(context, null);
    }

    public NestedScrollWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedScrollWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        childHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
        scroller = new Scroller(getContext());
        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        density = getResources().getDisplayMetrics().density;
        //TouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        TouchSlop = Util.dip2px(3);
    }

    public void loadHtml(String html) {
        this.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mWebViewContentHeight = 0;
                lastY = (int) event.getRawY();
                firstY = lastY;
                if (!scroller.isFinished()) {
                    scroller.abortAnimation();
                }
                initOrResetVelocityTracker();
                isSelfFling = false;
                hasFling = false;
                maxScrollY = getWebViewContentHeight() - getHeight();
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                break;
            case MotionEvent.ACTION_MOVE:
                initVelocityTrackerIfNotExists();
                velocityTracker.addMovement(event);
                int y = (int) (event.getRawY());
                int dy = y - lastY;
                lastY = y;
                if (!dispatchNestedPreScroll(0, -dy, mScrollConsumed, null)) {
                    scrollBy(0, -dy);
                }
                if (Math.abs(firstY - y) > TouchSlop) {
                    //屏蔽WebView本身的滑动，滑动事件自己处理
                    event.setAction(MotionEvent.ACTION_CANCEL);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (isParentResetScroll() && velocityTracker != null) {
                    velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    int yVelocity = (int) -velocityTracker.getYVelocity();
                    recycleVelocityTracker();
                    isSelfFling = true;
                    flingScroll(0, yVelocity);
                }
                break;
        }
        super.onTouchEvent(event);
        return true;
    }

    @Override
    public void flingScroll(int vx, int vy) {
        int startY = getWebViewContentHeight() - getHeight();
        if (getScrollY() < startY) {
            startY = getScrollY();
        }
        scroller.fling(0, startY, 0, vy, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        scroller.computeScrollOffset();
        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        recycleVelocityTracker();
        stopScroll();
        childHelper = null;
        scroller = null;
        parent = null;
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            final int currY = scroller.getCurrY();
            if (!isSelfFling) {//parent fling
                scrollTo(0, currY);
                invalidate();
                return;
            }
            if (isWebViewCanScroll()) {
                scrollTo(0, currY);
                invalidate();
            }
            if (!hasFling && scroller.getStartY() < currY
                    && !canWebViewScrollDown()
                    && startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL)
                    && !dispatchNestedPreFling(0, scroller.getCurrVelocity())) {
                //滑动到底部时，将fling传递给父控件和RecyclerView
                hasFling = true;
                dispatchNestedFling(0, scroller.getCurrVelocity(), false);
            }
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        if (isParentResetScroll()) {
            if (maxScrollY != 0 && y > maxScrollY) {
                y = maxScrollY;
            }
            if (y < 0) {
                y = 0;
            }
            super.scrollTo(x, y);

            //用于父控件不是嵌套控件时，绘制进度条，仅此而已
            if (!(getParent() instanceof NestedWebViewRecyclerViewGroup)) {
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                dispatchNestedScroll(1, 0, 0, 0, null);
            }
        }
    }

    void stopScroll() {
        if (scroller != null && !scroller.isFinished()) {
            scroller.abortAnimation();
        }
    }

    void scrollToBottom() {
        int y = computeVerticalScrollRange();
        super.scrollTo(0, y - getHeight());
    }

    private void initWebViewParent() {
        if (this.parent != null) {
            return;
        }
        View parent = (View) getParent();
        while (parent != null) {
            if (parent instanceof NestedWebViewRecyclerViewGroup) {
                this.parent = (NestedWebViewRecyclerViewGroup) parent;
                break;
            } else {
                parent = (View) parent.getParent();
            }
        }
    }

    private boolean isParentResetScroll() {
        if (parent == null) {
            initWebViewParent();
        }
        if (parent != null) {
            return parent.getScrollY() == 0;
        }
        return true;
    }

    private void initOrResetVelocityTracker() {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        } else {
            velocityTracker.clear();
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (velocityTracker != null) {
            velocityTracker.recycle();
            velocityTracker = null;
        }
    }

    private boolean canWebViewScrollDown() {
        final int offset = getScrollY();
        final int range = getWebViewContentHeight() - getHeight();
        if (range == 0) {
            return false;
        }
        return offset < range - 3;
    }

    private boolean isWebViewCanScroll() {
        final int offset = getScrollY();
        final int range = getWebViewContentHeight() - getHeight();
        if (range == 0) {
            return false;
        }
        return offset > 0 || offset < range - 3;
    }

    private int getWebViewContentHeight() {
        if (mWebViewContentHeight == 0) {
            mWebViewContentHeight = (int) (getContentHeight() * density);
        }
        return mWebViewContentHeight;
    }

    private NestedScrollingChildHelper getHelper() {
        if (childHelper == null) {
            childHelper = new NestedScrollingChildHelper(this);
        }
        return childHelper;
    }

    @Override
    public boolean startNestedScroll(int axes, int type) {
        return getHelper().startNestedScroll(axes, type);
    }

    @Override
    public void stopNestedScroll(int type) {
        getHelper().stopNestedScroll(type);
    }

    @Override
    public boolean hasNestedScrollingParent(int type) {
        return getHelper().hasNestedScrollingParent(type);
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, @Nullable int[] offsetInWindow, int type) {
        return getHelper().dispatchNestedScroll(dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow, type);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow, int type) {
        return getHelper().dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type);
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        getHelper().setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return getHelper().isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return getHelper().startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        getHelper().stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return getHelper().hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed,
                                        @Nullable int[] offsetInWindow) {
        return getHelper().dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow) {
        return getHelper().dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return getHelper().dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return getHelper().dispatchNestedPreFling(velocityX, velocityY);
    }

    public void setPageFinishedListener(PageFinishedListener listener) {
        this.pageFinishedListener = listener;
        final WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        // 缓存
        settings.setBuiltInZoomControls(false);
        settings.setSupportZoom(false);
        settings.setDisplayZoomControls(false);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDomStorageEnabled(true);
        settings.setAppCacheEnabled(true);
       // settings.setBlockNetworkImage(true);
        //settings.setLoadsImagesAutomatically(false);
        setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();// 接受所有网站的证书，忽略错误
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (pageFinishedListener != null)
                    pageFinishedListener.pageFinished(view);
                if (!NetworkUtil.isMobileConnected(MyApplication.getApp()) || PreUtils.getInt(WLAN_SETTING, 0) != 2) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        view.evaluateJavascript("javascript:loadAllImg()", null);
                    } else {
                        view.loadUrl("javascript:loadAllImg()");
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        view.evaluateJavascript("javascript:loadSingleImg()", null);
                    } else {
                        view.loadUrl("javascript:loadSingleImg()");
                    }
                }
            }

            //拦截二级网页进行处理
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        });
    }

    public interface PageFinishedListener {
        void pageFinished(WebView view);
    }

}
