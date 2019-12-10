package com.ww.todaylife.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.ww.commonlibrary.CommonConstant;
import com.ww.commonlibrary.util.LogUtils;
import com.ww.commonlibrary.view.LoadStateView;
import com.ww.commonlibrary.view.widget.DividerItemDecoration;
import com.ww.todaylife.R;
import com.ww.todaylife.VideoNewsDetailActivity;
import com.ww.todaylife.adapter.SearchVideoListAdapter;
import com.ww.todaylife.base.LazyFragment;
import com.ww.todaylife.bean.eventBean.SearchEvent;
import com.ww.todaylife.bean.httpResponse.NewsDetail;
import com.ww.todaylife.bean.httpResponse.SearchResponse;
import com.ww.todaylife.presenter.Iview.ISearchListView;
import com.ww.todaylife.presenter.SearchNewsListPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.jzvd.Jzvd;

public class SearchVideoListFragment extends LazyFragment<SearchNewsListPresenter> implements ISearchListView {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.loadingView)
    LoadStateView loadingView;
    private String keyword = null;
    private SearchVideoListAdapter videoListAdapter;
    private ArrayList<NewsDetail> mList = new ArrayList<>();
    private String typeCode;

    @Override
    public int getLayoutId() {
        return R.layout.news_list_fragment;
    }

    public static Fragment newInstance(String type) {
        SearchVideoListFragment fragment = new SearchVideoListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected SearchNewsListPresenter createPresenter() {
        return new SearchNewsListPresenter(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SearchEvent event) {
        this.keyword = event.keyword;
        loadingView.show();
        if(isVisible)
        mPresenter.getSearchNewsList(keyword, String.valueOf(0), typeCode, CommonConstant.TYPE_REFRESH);

    }

    @Override
    protected void initViews() {
        Jzvd.SAVE_PROGRESS = true;
        EventBus.getDefault().register(this);
        recyclerView.setItemAnimator(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(mBaseActivity));
        recyclerView.addItemDecoration(new DividerItemDecoration(mBaseActivity, LinearLayoutManager.VERTICAL));
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setOnLoadMoreListener(refreshLayout -> mPresenter.getSearchNewsList(keyword, String.valueOf(mList.size()), typeCode, CommonConstant.TYPE_NEXT));
        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                Jzvd jzvd = view.findViewById(R.id.jzPlayer);
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
        videoListAdapter = new SearchVideoListAdapter(mBaseActivity, mList);
        recyclerView.setAdapter(videoListAdapter);
        videoListAdapter.setItemClickListener(position -> {
            NewsDetail newsDetail = mList.get(position);
            Intent intent = new Intent(mBaseActivity, VideoNewsDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("NewsDetail", newsDetail);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    @Override
    protected void lazyLoad() {
        loadingView.show();
        if (keyword != null)
            mPresenter.getSearchNewsList(keyword, String.valueOf(0), typeCode, CommonConstant.TYPE_REFRESH);
    }


    @Override
    public void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }


    @Override
    public void onSearchNewsList(SearchResponse searchResponse, int loadType) {
        refreshLayout.finishLoadMore();
        if (loadType == CommonConstant.TYPE_REFRESH) {
            if (searchResponse.data.isEmpty()) {
                loadingView.setEmpty();
                return;
            }
            recyclerView.scrollToPosition(0);
            videoListAdapter.refreshList(searchResponse.data);
        } else {
            videoListAdapter.addAll(searchResponse.data);
        }
        refreshLayout.setNoMoreData((searchResponse.has_more == 0));
        loadingView.hide();
    }

    @Override
    public void onGetNewsList(List<NewsDetail> mList, int loadType) {

    }

    @Override
    public void onError(String msg) {
        refreshLayout.finishLoadMore();
        if (mList.isEmpty()) {
            loadingView.showErrorView();
        }
    }

    @Override
    public void onGetNewsLoadMoreList(List<NewsDetail> mList, int loadType) {

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
