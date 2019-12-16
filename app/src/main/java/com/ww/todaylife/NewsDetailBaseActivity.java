package com.ww.todaylife;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.ww.commonlibrary.CommonConstant;
import com.ww.commonlibrary.MyApplication;
import com.ww.commonlibrary.util.LogUtils;
import com.ww.commonlibrary.util.ScreenUtils;
import com.ww.commonlibrary.util.UiUtils;
import com.ww.commonlibrary.view.ClickAnimImage;
import com.ww.commonlibrary.view.LoadStateView;
import com.ww.commonlibrary.view.autoLoadMoreRecyclerView.AutoLoadRecyclerView;
import com.ww.todaylife.adapter.NewsComment2Adapter;
import com.ww.todaylife.base.BaseSwipeActivity;
import com.ww.todaylife.bean.eventBean.UpdateNewsItem;
import com.ww.todaylife.bean.httpResponse.CommentData;
import com.ww.todaylife.bean.httpResponse.CommentResponse;
import com.ww.todaylife.bean.httpResponse.NewsDetail;
import com.ww.todaylife.bean.httpResponse.ReplyListResponse;
import com.ww.todaylife.bean.httpResponse.StarNews;
import com.ww.todaylife.dataBase.TlDatabase;
import com.ww.todaylife.fragment.dialogfragment.CommentDialogFragment;
import com.ww.todaylife.fragment.dialogfragment.ReleaseCommentDialogFragment;
import com.ww.todaylife.presenter.Iview.INewsDetailView;
import com.ww.todaylife.presenter.NewsDetailPresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * created by wang.wei on 2019-12-06
 */
public abstract class NewsDetailBaseActivity extends BaseSwipeActivity<NewsDetailPresenter> implements INewsDetailView {
    @BindView(R.id.commentRv)
    AutoLoadRecyclerView commentRv;
    @BindView(R.id.loadingView)
    LoadStateView loadingView;
    @BindView(R.id.replyTv)
    TextView replyTv;
    @BindView(R.id.commentCountTv)
    TextView commentCountTv;
    @BindView(R.id.commentCountLayout)
    FrameLayout commentCountLayout;
    @BindView(R.id.starImage)
    ClickAnimImage starImage;
    @BindView(R.id.comment_bottom)
    LinearLayout commentBottom;
    LinearLayoutManager mLayoutManager;
    NewsDetail newsDetail;
    NewsComment2Adapter newsCommonAdapter;
    CommentDialogFragment commentDialogFragment;

    ArrayList<CommentData> mList = new ArrayList<>();
    String userId = "";

    @Override
    protected NewsDetailPresenter createPresenter() {
        return new NewsDetailPresenter(this);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        newsDetail = (NewsDetail) bundle.getSerializable("NewsDetail");
        newsCommonAdapter = new NewsComment2Adapter(this, mList);
        commentRv.setAdapter(newsCommonAdapter);
        commentRv.setOnLoadMoreListener(() -> mPresenter.getNewsCommentList(String.valueOf(newsDetail.group_id), String.valueOf(newsDetail.item_id), mList.size() / CommonConstant.COMMENT_PAGE_SIZE + 1, CommonConstant.TYPE_NEXT), mLayoutManager);
        newsCommonAdapter.setItemClickListener(position -> {
            if (commentDialogFragment == null) {
                commentDialogFragment = new CommentDialogFragment();
                commentDialogFragment.setPresenter(mPresenter);
                if (newsDetail.has_video) {
                    int height = ScreenUtils.getScreenHeight() - ScreenUtils.getStatusBarHeight(NewsDetailBaseActivity.this) - ScreenUtils.dip2px(NewsDetailBaseActivity.this, 200);
                    commentDialogFragment.setHeight(height);
                } else {
                    commentDialogFragment.setHeight(0);
                }
            }
            Bundle bundle1 = new Bundle();
            bundle1.putSerializable("comment", mList.get(position));
            commentDialogFragment.setArguments(bundle1);
            Log.e("startTime",System.currentTimeMillis()+"ms");
            commentDialogFragment.show(getSupportFragmentManager(), "commentDialogFragment");
        });
        if (newsDetail.isStar) {
            starImage.setIsActive(true);
        }
        loadData();
    }

    public void loadData() {
        mPresenter.getNewsCommentList(String.valueOf(newsDetail.group_id), String.valueOf(newsDetail.item_id), mList.size() / CommonConstant.COMMENT_PAGE_SIZE + 1, CommonConstant.TYPE_REFRESH);
        mPresenter.getNewsDetail(String.valueOf(newsDetail.item_id));
    }

    @Override
    public void initView() {
        mLayoutManager = new LinearLayoutManager(this);
        commentRv.setItemAnimator(null);
        commentRv.setLayoutManager(mLayoutManager);
        replyTv.setOnClickListener(v -> {
            ReleaseCommentDialogFragment dialogFragment = new ReleaseCommentDialogFragment();
            dialogFragment.show(getSupportFragmentManager(), "ReleaseCommentDialogFragment");
        });
        starImage.setAnimClickListener(view -> {
            newsDetail.isStar = !newsDetail.isStar;
            EventBus.getDefault().post(new UpdateNewsItem(newsDetail.typeCode, newsDetail.htmlString, newsDetail.isStar));
            TlDatabase.getInstance().newsDao().updateIsStarByItemId(newsDetail.isStar, newsDetail.item_id);
            StarNews news = new StarNews();
            news.newsDetail = newsDetail;
            TlDatabase.getInstance().starNewsDao().insertNews(news);
            List<StarNews> list = TlDatabase.getInstance().starNewsDao().searchAllNews();
            LogUtils.e(list.size() + "");
        });
        loadingView.show();
    }

    @Override
    public void onError(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            UiUtils.showShortToast(MyApplication.getApp(), msg);
        }
    }

    public void gotoUserDetail() {
        Intent intent = new Intent(this, UserDetailActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    public void updateNewsDetail() {
        EventBus.getDefault().post(new UpdateNewsItem(newsDetail.typeCode, newsDetail.htmlString, newsDetail.isStar));
        TlDatabase.getInstance().newsDao().updateHtmlStrByItemId(newsDetail.htmlString, newsDetail.item_id);
    }

    @Override
    public void onGetCommentReply(ReplyListResponse commentReply, int loadType, boolean hasMore) {
        if (commentDialogFragment != null)
            commentDialogFragment.handleData(commentReply, loadType, hasMore);
    }

    @Override
    public void onGetNewsComment(CommentResponse commentResponse, int loadType, boolean hasMore) {
        if (newsDetail.has_video) {
            loadingView.hide();
        }
        // error
        if (commentResponse == null) {
            commentRv.setLoadMoreFinish(true);
            return;
        }
        if (commentResponse.total_number == 0) {
            commentRv.setEmpty();
        } else {
            commentRv.setLoadMoreFinish(hasMore);
            if (loadType == CommonConstant.TYPE_REFRESH) {
                commentCountTv.setText(String.valueOf(commentResponse.total_number));
                this.mList.addAll(commentResponse.data);
                newsCommonAdapter.notifyDataSetChanged();
            } else {
                newsCommonAdapter.addAll(commentResponse.data);
            }
        }
    }

}
