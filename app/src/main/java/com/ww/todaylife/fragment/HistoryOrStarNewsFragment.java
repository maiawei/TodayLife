package com.ww.todaylife.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.ww.commonlibrary.CommonConstant;
import com.ww.commonlibrary.view.LoadStateView;
import com.ww.todaylife.NewsDetailActivity;
import com.ww.todaylife.R;
import com.ww.todaylife.VideoNewsDetailActivity;
import com.ww.todaylife.adapter.HistoryOrStarAdapter;
import com.ww.todaylife.adapter.NewsList2Adapter;
import com.ww.todaylife.base.BasePresenter;
import com.ww.todaylife.base.LazyFragment;
import com.ww.todaylife.bean.httpResponse.NewsDetail;
import com.ww.todaylife.dataBase.TlDatabase;
import com.ww.todaylife.presenter.Iview.INewsListView;
import com.ww.todaylife.presenter.NewsListPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * created by wang.wei on 2019-12-09
 */
public class HistoryOrStarNewsFragment extends LazyFragment<NewsListPresenter> implements INewsListView {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.loadingView)
    LoadStateView loadingView;
    private ArrayList<NewsDetail> mList = new ArrayList<>();
    private String typeCode;
    private HistoryOrStarAdapter newsListAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.news_list_fragment;
    }

    @Override
    protected NewsListPresenter createPresenter() {
        return new NewsListPresenter(this);
    }

    public static HistoryOrStarNewsFragment newInstance(String typeCode) {

        Bundle args = new Bundle();
        args.putString("typeCode", typeCode);
        HistoryOrStarNewsFragment fragment = new HistoryOrStarNewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initViews() {
        refreshLayout.setEnableRefresh(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(mBaseActivity));
        recyclerView.addItemDecoration(new DividerItemDecoration(mBaseActivity, LinearLayoutManager.VERTICAL));
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            mPresenter.getNewsHistoryOrStar(typeCode, CommonConstant.TYPE_NEXT, mList.size());
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        typeCode = getArguments().getString("typeCode");
        newsListAdapter = new HistoryOrStarAdapter(mBaseActivity, mList);
        recyclerView.setAdapter(newsListAdapter);
        newsListAdapter.setItemClickListener(position -> {
            NewsDetail newsDetail = mList.get(position);
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("NewsDetail", newsDetail);
            intent.putExtras(bundle);
            if (newsDetail.has_video) {
                intent.setClass(mBaseActivity, VideoNewsDetailActivity.class);
            } else {
                intent.setClass(mBaseActivity, NewsDetailActivity.class);
            }
            startActivity(intent);

        });
    }

    @Override
    protected void lazyLoad() {
        mPresenter.getNewsHistoryOrStar(typeCode, CommonConstant.TYPE_NEXT, 0);
    }

    @Override
    public void onGetNewsList(List<NewsDetail> mList, int loadType) {

    }

    @Override
    public void onError(String msg) {

    }

    @Override
    public void onGetNewsLoadMoreList(List<NewsDetail> mList, int loadType) {
        if (this.mList.isEmpty() && mList.isEmpty()) {
            loadingView.setEmpty();
        } else {
            newsListAdapter.addAll(mList);
            if (mList.isEmpty()) {
                refreshLayout.finishLoadMoreWithNoMoreData();
            } else {
                refreshLayout.finishLoadMore();
            }
            loadingView.hide();
        }

    }
}
