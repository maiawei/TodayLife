package com.ww.commonlibrary.view.autoLoadMoreRecyclerView;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ww.commonlibrary.base.BaseLoadAdapter;
import com.ww.commonlibrary.util.LogUtils;
import com.ww.commonlibrary.util.NetworkUtil;

import org.jetbrains.annotations.NotNull;

import static com.ww.commonlibrary.view.autoLoadMoreRecyclerView.RecyclerItemType.TYPE_EMPTY;
import static com.ww.commonlibrary.view.autoLoadMoreRecyclerView.RecyclerItemType.TYPE_LOADING;
import static com.ww.commonlibrary.view.autoLoadMoreRecyclerView.RecyclerItemType.TYPE_LOAD_COMPLETE;
import static com.ww.commonlibrary.view.autoLoadMoreRecyclerView.RecyclerItemType.TYPE_NETWORK;

/**
 * created by wang.wei on 2019-11-27
 */
public class AutoLoadRecyclerView extends RecyclerView {
    private EndlessRecyclerOnScrollListener listener;
    private OnLoadMoreListener onLoadMoreListener;
    public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();

    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount, lastVisibleItem;
    private boolean enableLoadMore = true;
    private boolean isAddLoadType;
    private LinearLayoutManager mLinearLayoutManager;
    private boolean loadComplete = true;

    public AutoLoadRecyclerView(@NonNull Context context) {
        super(context);
    }

    public AutoLoadRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoLoadRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener, LinearLayoutManager linearLayoutManager) {
        mLinearLayoutManager = linearLayoutManager;
        this.onLoadMoreListener = onLoadMoreListener;
        init();
    }

    public void init() {
        setItemAnimator(null);
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = mLinearLayoutManager.getItemCount();
                firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
                if (totalItemCount <= visibleItemCount) {
                    //不满一屏
                    return;
                }
                //判断滑到底部
                if (lastVisibleItem >= totalItemCount - 1 && visibleItemCount > 0 && totalItemCount > visibleItemCount && !isAddLoadType) {
                    if (!NetworkUtil.isNetWorkConnected(getContext())) {
                        ((BaseLoadAdapter) getAdapter()).setLoadType(TYPE_NETWORK);
                    } else {
                        if (enableLoadMore) {
                            ((BaseLoadAdapter) getAdapter()).setLoadType(TYPE_LOADING);
                        } else {
                            ((BaseLoadAdapter) getAdapter()).setLoadType(TYPE_LOAD_COMPLETE);
                        }
                    }
                    isAddLoadType = true;
                }
                if (!enableLoadMore) {
                    return;
                }
                if (!NetworkUtil.isNetWorkConnected(getContext())) {
                    return;
                }
                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {

                    if (onLoadMoreListener != null && enableLoadMore && loadComplete) {
                        onLoadMoreListener.onLoadMore();
                        loadComplete = false;
                    }
                    loading = true;
                }
            }

        });
    }

    public void setLoadMoreFinish(boolean hasMore) {
        BaseLoadAdapter mAdapter = (BaseLoadAdapter) getAdapter();
        if (mAdapter.getItems() == null && mAdapter.getItems().size() == 0) {
            return;
        }
        loadComplete = true;
        loading = false;
        enableLoadMore = hasMore;
        if (isAddLoadType) {
            mAdapter.remove(mAdapter.getItemCount() - 1);
            isAddLoadType = false;
        }
    }

    public void setEmpty() {
        enableLoadMore = false;
        BaseLoadAdapter mAdapter = (BaseLoadAdapter) getAdapter();
        if (mAdapter == null) {
            return;
        }
        mAdapter.setLoadType(TYPE_EMPTY);
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void reset(int previousTotal, boolean loading) {
        this.previousTotal = previousTotal;
        this.loading = loading;
    }

}
