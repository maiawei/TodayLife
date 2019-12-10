package com.ww.commonlibrary.view.autoLoadMoreRecyclerView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * created by wang.wei on 2019-11-27
 */
public class LoadRecyclerViewLayout extends ViewGroup implements NestedScrollingParent {
    private static final String TAG = "LoadRecyclerView";
    private RecyclerView mRecyclerView;
    private OnLoadMoreListener mLoadMoreListener;
    private List mDataList;
    private RecyclerView.Adapter mAdapter;
    private boolean mLoading;
    private NestedScrollingParentHelper mParentHelper;
    private int mTouchSlop;
    private boolean mHasMoreData = true;
    private boolean mHasCompleteLoadShown;
    public LoadRecyclerViewLayout(Context context) {
        super(context);
    }

    public LoadRecyclerViewLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mParentHelper = new NestedScrollingParentHelper(this);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ensureTarget();
        if(mRecyclerView != null){
//            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                @Override
//                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//
//                }
//
//                @Override
//                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                    LinearLayoutManager llm = (LinearLayoutManager)recyclerView.getLayoutManager();
//                    if(dy > 0) //check for scroll down
//                    {
//                        attemptLoad();
//                    }
//                }
//            });

        }else{
            throw new IllegalArgumentException("You must supply a RecyclerView as a direct child.");
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(mRecyclerView == null){
            ensureTarget();
        }
        int widthSpec = MeasureSpec.makeMeasureSpec(
                getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                MeasureSpec.EXACTLY);
        int heightSpec = MeasureSpec.makeMeasureSpec(
                getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY);
        mRecyclerView.measure(widthSpec, heightSpec);
//        mFooterView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
//                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        final int childLeft = getPaddingLeft();
        int childTop = getPaddingTop();
        final int childWidth = width - getPaddingLeft() - getPaddingRight();
        final int childHeight = height - getPaddingTop() - getPaddingBottom();
        mRecyclerView.layout(childLeft, childTop, childWidth, childHeight);
    }

    private void ensureTarget(){
        for(int i = 0, N = getChildCount(); i < N; i++){
            View childView = getChildAt(i);
            if(childView instanceof RecyclerView){
                mRecyclerView = (RecyclerView)childView;
            }
        }
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        mLoadMoreListener = loadMoreListener;
    }

    public void setLoading(boolean loading, boolean hasMoreData){
        if(mAdapter == null){
            mAdapter = mRecyclerView.getAdapter();
            if(mAdapter instanceof OnRequireDataListener){
                mDataList = ((OnRequireDataListener) mAdapter).requireData();
            }
        }
        if(mDataList == null || mHasCompleteLoadShown){
            return;
        }
        if(loading){
            if(!mLoading){
                loading = true;
                if(mHasMoreData){
                    mDataList.add(RecyclerItemType.TYPE_LOADING);
                }else{
                    mDataList.add(RecyclerItemType.TYPE_LOAD_COMPLETE);
                    mHasCompleteLoadShown = true;
                }
                mAdapter.notifyItemInserted(mDataList.size() - 1);
            }
        }else{
            if(mLoading){
                if(!mHasCompleteLoadShown){
                    mHasMoreData = hasMoreData;
                    int lastPosition = mDataList.size() - 1;
                    mDataList.remove(lastPosition);
                    mAdapter.notifyItemRemoved(lastPosition);
                    loading = false;
                }
            }
        }
        mLoading = loading;
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return needLoadMore();
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {

    }

    @Override
    public void onStopNestedScroll(View target) {
        mParentHelper.onStopNestedScroll(target);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if(dy > mTouchSlop) //check for scroll down
        {
            attemptLoad();
        }
    }


    @Override
    public int getNestedScrollAxes() {
        return mParentHelper.getNestedScrollAxes();
    }

    public interface OnLoadMoreListener{
        void loadMore();
    }

    public interface OnRequireDataListener{
        List requireData();
    }

    private void attemptLoad(){
        if(needLoadMore()){
            if(mLoadMoreListener != null){
                setLoading(true, true);
                mLoadMoreListener.loadMore();
            }
        }
    }

    private boolean needLoadMore(){
        LinearLayoutManager llm = (LinearLayoutManager)mRecyclerView.getLayoutManager();
        int visibleItemCount = llm.getChildCount();
        int totalItemCount = llm.getItemCount();
        int pastVisiblesItems = llm.findFirstVisibleItemPosition();
        int firstVisiblePosition = llm.findFirstVisibleItemPosition();
        if(firstVisiblePosition == 0){
            return false;
        }
        if (!mLoading) {

            if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
            {
                return true;
            }
        }
        return false;
    }
    public enum RecyclerItemType {
        TYPE_LOADING, TYPE_LOAD_COMPLETE
    }
}
