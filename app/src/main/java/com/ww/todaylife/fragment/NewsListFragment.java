package com.ww.todaylife.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.ww.commonlibrary.CommonConstant;
import com.ww.commonlibrary.view.LoadStateView;
import com.ww.todaylife.NewsDetailActivity;
import com.ww.todaylife.VideoNewsDetailActivity;
import com.ww.todaylife.R;
import com.ww.todaylife.adapter.NewsList2Adapter;
import com.ww.todaylife.base.LazyFragment;
import com.ww.todaylife.bean.eventBean.UpdateNewsItem;
import com.ww.todaylife.bean.httpResponse.NewsDetail;
import com.ww.todaylife.dataBase.TlDatabase;
import com.ww.todaylife.presenter.Iview.INewsListView;
import com.ww.todaylife.presenter.NewsListPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class NewsListFragment extends LazyFragment<NewsListPresenter> implements INewsListView {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.loadingView)
    LoadStateView loadingView;
    int currentPosition, currentSize;
    private NewsList2Adapter newsListAdapter;
    private ArrayList<NewsDetail> mList = new ArrayList<>();
    private String typeCode;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public int getLayoutId() {
        return R.layout.news_list_fragment;
    }

    public static Fragment newInstance(String type) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected NewsListPresenter createPresenter() {
        return new NewsListPresenter(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(UpdateNewsItem event) {
        if (TextUtils.equals(typeCode, event.typeCode)) {
            mList.get(currentPosition + mList.size() - currentSize).htmlString = event.htmlStr;
            mList.get(currentPosition + mList.size() - currentSize).isStar=event.isStar;
        }
    }

    @Override
    protected void initViews() {
        EventBus.getDefault().register(this);
        linearLayoutManager = new LinearLayoutManager(mBaseActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(mBaseActivity, LinearLayoutManager.VERTICAL));
        refreshLayout.setOnRefreshListener(refreshLayout -> loadData(CommonConstant.TYPE_REFRESH));
        refreshLayout.setOnLoadMoreListener(refreshLayout ->
                mPresenter.getNewsLoadMore(typeCode, CommonConstant.TYPE_NEXT, mList.size())
        );
        loadingView.setRetryListener(() -> {
            loadData(CommonConstant.TYPE_REFRESH);
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        typeCode = getArguments().getString("type");
        newsListAdapter = new NewsList2Adapter(mBaseActivity, mList);
        recyclerView.setAdapter(newsListAdapter);
        newsListAdapter.setItemClickListener(position -> {
            currentPosition = position;
            currentSize = mList.size();
            NewsDetail newsDetail = mList.get(position);
            if (newsDetail.isFeedback)
                return;
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
            if (!newsDetail.isRead) {
                newsDetail.isRead = true;
                TlDatabase.getInstance().newsDao().updateIsReadByItemId(true, newsDetail.item_id);
                newsListAdapter.notifyItemChanged(position);
            }
        });
        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                TextView contentView = view.findViewById(R.id.hiddenContent);
                if (contentView != null) {
                    int position = (int) contentView.getTag();
                    if (position != -1)
                        mList.get(position).isHidden = true;
                }
            }
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
            // dealData();
            mList.addAll(0, newsDetailList);
            newsListAdapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(0);
            refreshLayout.finishRefresh();
            loadingView.hide();
        } else {
            newsListAdapter.addAll(newsDetailList);
            refreshLayout.finishLoadMore();
        }
    }

    public void dealData() {
        //刷新去重前两位（置顶数据）
        if (!mList.isEmpty() && typeCode.equals("")) {
            mList.remove(0);
            mList.remove(0);
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
            newsListAdapter.addAll(mList);
            if (mList.isEmpty()) {
                refreshLayout.finishLoadMoreWithNoMoreData();
            } else {
                refreshLayout.finishLoadMore();
            }

        }
        loadingView.hide();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
