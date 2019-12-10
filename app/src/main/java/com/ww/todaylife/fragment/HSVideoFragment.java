package com.ww.todaylife.fragment;

import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.ww.commonlibrary.CommonConstant;
import com.ww.commonlibrary.util.LogUtils;
import com.ww.commonlibrary.view.LoadStateView;
import com.ww.todaylife.R;
import com.ww.todaylife.adapter.HSVideoListAdapter;
import com.ww.todaylife.base.BaseFragment;
import com.ww.todaylife.base.LazyFragment;
import com.ww.todaylife.bean.httpResponse.HsVideoRootBean;
import com.ww.todaylife.presenter.HsVideoListPresenter;
import com.ww.todaylife.presenter.Iview.IHSVideoListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HSVideoFragment extends LazyFragment<HsVideoListPresenter> implements IHSVideoListView {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.loadingView)
    LoadStateView loadingView;

    private HSVideoListAdapter videoListAdapter;
    private ArrayList<HsVideoRootBean> mList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.news_list_fragment;
    }


    @Override
    protected HsVideoListPresenter createPresenter() {
        return new HsVideoListPresenter(this);
    }

    @Override
    protected void initViews() {
        loadingView.show();
        recyclerView.setItemAnimator(null);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(layoutManager);
        refreshLayout.setOnRefreshListener(refreshlayout -> loadData(CommonConstant.TYPE_REFRESH));
        loadingView.setRetryListener(() -> {
            loadData(CommonConstant.TYPE_REFRESH);
        });

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        videoListAdapter = new HSVideoListAdapter(mBaseActivity, mList);
        recyclerView.setAdapter(videoListAdapter);
    }

    @Override
    protected void lazyLoad() {
        loadData(CommonConstant.TYPE_REFRESH);
    }


    public void loadData(int loadType) {
        mPresenter.getNewsList(loadType);
    }

    @Override
    public void onGetHSVideoList(List<HsVideoRootBean> mList, int loadType) {
        loadingView.hide();
        if (loadType == CommonConstant.TYPE_REFRESH) {
            videoListAdapter.addAllFromTop(mList);
            recyclerView.scrollToPosition(0);
            refreshLayout.finishRefresh();
        } else {
            videoListAdapter.addAll(mList);
            refreshLayout.finishLoadMore();
        }
    }

    @Override
    public void onError(String msg) {
        if (mList.isEmpty()) {
            loadingView.showErrorView();
        }
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();
    }


}
