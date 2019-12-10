package com.ww.todaylife.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.ww.commonlibrary.CommonConstant;
import com.ww.commonlibrary.util.LogUtils;
import com.ww.commonlibrary.view.LoadStateView;
import com.ww.commonlibrary.view.widget.DividerItemDecoration;
import com.ww.todaylife.NewsDetailActivity;
import com.ww.todaylife.R;
import com.ww.todaylife.adapter.SearchListAdapter;
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

public class SearchNewsListFragment extends LazyFragment<SearchNewsListPresenter> implements ISearchListView {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.loadingView)
    LoadStateView loadingView;
    private SearchListAdapter newsListAdapter;
    private ArrayList<NewsDetail> mList = new ArrayList<>();
    private String typeCode;
    private String keyword = null;

    @Override
    public int getLayoutId() {
        return R.layout.search_news_list_fragment;
    }

    public static Fragment newInstance(String type) {
        SearchNewsListFragment fragment = new SearchNewsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SearchEvent event) {
        this.keyword = event.keyword;
        loadingView.show();
        if(isVisible)
        mPresenter.getSearchNewsList(keyword, String.valueOf(0), typeCode, CommonConstant.TYPE_REFRESH);

    }

    @Override
    protected SearchNewsListPresenter createPresenter() {
        return new SearchNewsListPresenter(this);
    }

    @Override
    protected void initViews() {
        EventBus.getDefault().register(this);
        recyclerView.setItemAnimator(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(mBaseActivity));
        recyclerView.addItemDecoration(new DividerItemDecoration(mBaseActivity, LinearLayoutManager.VERTICAL));
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setOnLoadMoreListener(refreshLayout -> mPresenter.getSearchNewsList(keyword, String.valueOf(mList.size()), typeCode, CommonConstant.TYPE_NEXT));

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        typeCode = getArguments().getString("type");
        newsListAdapter = new SearchListAdapter(mBaseActivity, mList);
        recyclerView.setAdapter(newsListAdapter);
        newsListAdapter.setItemClickListener(position -> {
            NewsDetail newsDetail = mList.get(position);
            Intent intent = new Intent(mBaseActivity, NewsDetailActivity.class);
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
    public void onGetNewsList(List<NewsDetail> mList, int loadType) {

    }

    @Override
    public void onError(String str) {
        refreshLayout.finishLoadMore();
        if (mList.isEmpty()) {
            loadingView.showErrorView();
        }
    }

    @Override
    public void onGetNewsLoadMoreList(List<NewsDetail> mList, int loadType) {

    }

    @Override
    public void onSearchNewsList(SearchResponse searchResponse, int loadType) {
        LogUtils.e(typeCode+"onSearchNewsList");
        refreshLayout.finishLoadMore();
        if (loadType == CommonConstant.TYPE_REFRESH) {
            if (searchResponse.data.isEmpty()) {
                loadingView.setEmpty();
                return;
            }
            recyclerView.scrollToPosition(0);
            newsListAdapter.refreshList(searchResponse.data);
        } else {
            newsListAdapter.addAll(searchResponse.data);
        }
        refreshLayout.setNoMoreData((searchResponse.has_more == 0));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingView.hide();
            }
        }, 200);

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
