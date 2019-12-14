package com.ww.todaylife.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.ww.commonlibrary.CommonConstant;
import com.ww.commonlibrary.view.LoadStateView;
import com.ww.todaylife.R;
import com.ww.todaylife.VideoNewsDetailActivity;
import com.ww.todaylife.adapter.VideoListAdapter;
import com.ww.todaylife.base.LazyFragment;
import com.ww.todaylife.bean.eventBean.OnBackEvent;
import com.ww.todaylife.bean.httpResponse.NewsDetail;
import com.ww.todaylife.presenter.Iview.INewsListView;
import com.ww.todaylife.presenter.NewsListPresenter;
import com.ww.commonlibrary.util.PreUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.jzvd.Jzvd;

public class VideoListFragment extends LazyFragment<NewsListPresenter> implements INewsListView {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.loadingView)
    LoadStateView loadingView;

    private VideoListAdapter videoListAdapter;
    private ArrayList<NewsDetail> mList = new ArrayList<>();
    private String typeCode;

    @Override
    public int getLayoutId() {
        return R.layout.news_list_fragment;
    }

    public static Fragment newInstance(String type) {
        VideoListFragment fragment = new VideoListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(OnBackEvent event) {
        if (isVisible && refreshLayout != null) {
            if (PreUtils.getBoolean(CommonConstant.RETURN_REFRESH, false))
                recyclerView.scrollToPosition(0);
            refreshLayout.autoRefresh();
        }
    }

    @Override
    protected NewsListPresenter createPresenter() {
        return new NewsListPresenter(this);
    }

    @Override
    protected void initViews() {
        EventBus.getDefault().register(this);
        Jzvd.SAVE_PROGRESS = true;
        recyclerView.setItemAnimator(null);
        recyclerView.addItemDecoration(new DividerItemDecoration(mBaseActivity, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(mBaseActivity));
        refreshLayout.setOnRefreshListener(refreshLayout -> loadData(CommonConstant.TYPE_REFRESH));
        refreshLayout.setOnLoadMoreListener(refreshLayout -> mPresenter.getNewsLoadMore(typeCode, CommonConstant.TYPE_NEXT, typeCode.equals("") ? mList.size() + 2 : mList.size()));
        loadingView.setRetryListener(() -> {
            loadData(CommonConstant.TYPE_REFRESH);
        });
        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                Jzvd jzvd = view.findViewById(R.id.jzPlayer);
                jzvd.videoDurationTv.setVisibility(View.VISIBLE);
                if (jzvd != null && Jzvd.CURRENT_JZVD != null &&
                        jzvd.jzDataSource.containsTheUrl(Jzvd.CURRENT_JZVD.jzDataSource.getCurrentUrl())) {
                    if (Jzvd.CURRENT_JZVD != null && Jzvd.CURRENT_JZVD.screen != Jzvd.SCREEN_FULLSCREEN) {
                        Jzvd.releaseAllVideos();
                    }
                }
            }
        });

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        typeCode = getArguments().getString("type");
        videoListAdapter = new VideoListAdapter(mBaseActivity, mList, mPresenter);
        recyclerView.setAdapter(videoListAdapter);
        videoListAdapter.setItemClickListener(position -> {
            NewsDetail newsDetail = mList.get(position);
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("NewsDetail", newsDetail);
            intent.putExtras(bundle);
            intent.setClass(mBaseActivity, VideoNewsDetailActivity.class);
            startActivity(intent);
        });
    }

    public void loadData(int loadType) {
        mPresenter.getNewsList(typeCode, loadType);
    }

    @Override
    protected void lazyLoad() {
        loadingView.show();
        mPresenter.getNewsLoadMore(typeCode, CommonConstant.TYPE_NEXT, 0);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onGetNewsList(List<NewsDetail> newsDetailList, int loadType) {
        if (loadType == CommonConstant.TYPE_REFRESH) {
            videoListAdapter.addAllFromTop(newsDetailList);
            recyclerView.scrollToPosition(0);
            refreshLayout.finishRefresh();
        } else {
            videoListAdapter.addAll(newsDetailList);
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

    // 数据库读取
    @Override
    public void onGetNewsLoadMoreList(List<NewsDetail> mList, int loadType) {
        if (this.mList.isEmpty() && mList.isEmpty()) {
            refreshLayout.autoRefresh();
        } else {
            videoListAdapter.addAll(mList);
            if (mList.isEmpty()) {
                refreshLayout.finishLoadMoreWithNoMoreData();
            } else {
                refreshLayout.finishLoadMore();
            }
        }
        loadingView.hide();
    }

    @Override
    public void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
