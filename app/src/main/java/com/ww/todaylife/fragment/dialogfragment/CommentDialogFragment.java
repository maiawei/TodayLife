package com.ww.todaylife.fragment.dialogfragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.ww.commonlibrary.CommonConstant;
import com.ww.commonlibrary.util.LogUtils;
import com.ww.commonlibrary.util.ScreenUtils;
import com.ww.commonlibrary.util.TimeUtils;
import com.ww.commonlibrary.view.CircleImageView;
import com.ww.commonlibrary.view.ClickAnimImage;
import com.ww.commonlibrary.view.LimitTextView;
import com.ww.commonlibrary.view.autoLoadMoreRecyclerView.AutoLoadRecyclerView;
import com.ww.todaylife.R;
import com.ww.todaylife.adapter.ReplyAdapter;
import com.ww.todaylife.base.BaseFullBottomSheetFragment;
import com.ww.todaylife.bean.httpResponse.CommentData;
import com.ww.todaylife.bean.httpResponse.CommentReply;
import com.ww.todaylife.bean.httpResponse.HsVideoRootBean;
import com.ww.todaylife.bean.httpResponse.ReplyListResponse;
import com.ww.todaylife.presenter.NewsDetailPresenter;

import java.util.ArrayList;

import butterknife.BindView;


public class CommentDialogFragment extends BaseFullBottomSheetFragment {

    @BindView(R.id.replyRv)
    AutoLoadRecyclerView replyRv;
    private ReplyAdapter mAdapter;
    private ArrayList<CommentReply> mList = new ArrayList<>();
    private NewsDetailPresenter presenter;
    @BindView(R.id.replyCount)
    TextView replyCount;
    @BindView(R.id.reply)
    TextView reply;
    private LinearLayout loadLayout;
    private CommentData item;

    public void setPresenter(NewsDetailPresenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void initData() {
        Bundle bundle = getArguments();
        item = (CommentData) bundle.getSerializable("comment");
        if (item == null) {
            return;
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        replyRv.setLayoutManager(layoutManager);
        mAdapter = new ReplyAdapter(mContext, mList);
        replyRv.setAdapter(mAdapter);
        initHeaderView();
        reply.setOnClickListener(v -> {
            ReleaseCommentDialogFragment dialogFragment = new ReleaseCommentDialogFragment();
            dialogFragment.show(getChildFragmentManager(), "ReleaseCommentDialogFragment");
        });
        replyRv.setOnLoadMoreListener(() -> {
            presenter.getCommentReplyList(String.valueOf(item.comment.id), mList.size() - 1, CommonConstant.TYPE_REFRESH);
        }, layoutManager);
        replyCount.setText(item.comment.reply_count > 0 ? item.comment.reply_count + "条回复" : "暂无回复");
        presenter.getCommentReplyList(String.valueOf(item.comment.id), 0, CommonConstant.TYPE_INIT);
    }


    private void initHeaderView() {
        View hView = LayoutInflater.from(mContext).inflate(R.layout.common_detail_header, null);
        CircleImageView header = hView.findViewById(R.id.header);
        TextView author = hView.findViewById(R.id.author);
        LimitTextView content = hView.findViewById(R.id.content);
        TextView likeCount = hView.findViewById(R.id.likeCount);
        TextView diggCountTv = hView.findViewById(R.id.diggCountTv);
        TextView commentDate = hView.findViewById(R.id.commentDate);
        loadLayout = hView.findViewById(R.id.loadLayout);
        content.setCanExpand(true);
        Glide.with(mContext).load(item.comment.user_profile_image_url).into(header);
        author.setText(item.comment.user_name);
        content.setContent(item.comment.text, ScreenUtils.getScreenWidth() - ScreenUtils.dip2px(mContext, 75));
        diggCountTv.setText(item.comment.digg_count > 0 ? item.comment.digg_count + "人赞过 >" : "暂无人赞过");
        likeCount.setText(String.valueOf(item.comment.digg_count));
        commentDate.setText(TimeUtils.getShortTime(item.comment.create_time * 1000));
        mAdapter.setHeaderView(hView);
    }

    public void handleData(ReplyListResponse commentReply, int loadType, boolean hasMore) {
        loadLayout.setVisibility(View.GONE);
        if (commentReply == null) {
            replyRv.setNoMoreData(true);
            return;
        }
        if (commentReply.data.total_count == 0) {
            replyRv.setEmpty();
        } else {
            replyRv.setNoMoreData(hasMore);
            if (loadType == CommonConstant.TYPE_REFRESH) {
                this.mList.addAll(commentReply.data.data);
                mAdapter.notifyDataSetChanged();
            } else {
                mAdapter.addAll(commentReply.data.data);
            }
        }
    }

    @Override
    public int providerView() {
        return R.layout.comment_dialog_layout;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        mList.clear();
        super.onDismiss(dialog);
    }
}
