package com.ww.todaylife.fragment.dialogfragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ww.commonlibrary.CommonConstant;
import com.ww.commonlibrary.view.autoLoadMoreRecyclerView.AutoLoadRecyclerView;
import com.ww.todaylife.R;
import com.ww.todaylife.adapter.NewsComment2Adapter;
import com.ww.todaylife.base.BaseFullBottomSheetFragment;
import com.ww.todaylife.bean.httpResponse.CommentData;
import com.ww.todaylife.bean.httpResponse.CommentResponse;
import com.ww.todaylife.bean.httpResponse.HsVideoRootBean;
import com.ww.todaylife.presenter.NewsDetailPresenter;

import java.util.ArrayList;

import butterknife.BindView;


public class HsVideoCommentDialogFragment extends BaseFullBottomSheetFragment {


    @BindView(R.id.replyCount)
    TextView replyCount;
    @BindView(R.id.commentRv)
    AutoLoadRecyclerView commentRv;
    @BindView(R.id.reply)
    TextView reply;
    HsVideoRootBean video;
    NewsComment2Adapter mAdapter;
    ArrayList<CommentData> mList = new ArrayList<>();

    public void setPresenter(NewsDetailPresenter presenter) {
        this.presenter = presenter;
    }

    NewsDetailPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.TransBottomSheetDialogStyle);
    }

    public static HsVideoCommentDialogFragment newInstance(HsVideoRootBean bean) {
        HsVideoCommentDialogFragment fragment = new HsVideoCommentDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("HsVideoRootBean", bean);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void initData() {
        commentRv.setItemAnimator(null);
        mAdapter = new NewsComment2Adapter(mContext, mList);
        LinearLayoutManager Manager = new LinearLayoutManager(mContext);
        commentRv.setLayoutManager(Manager);
        commentRv.setAdapter(mAdapter);
        video = (HsVideoRootBean) getArguments().getSerializable("HsVideoRootBean");
        replyCount.setText(video.raw_data.action.comment_count + "回复");
        commentRv.setOnLoadMoreListener(() -> {
            loadData(CommonConstant.TYPE_NEXT);

        }, Manager);
        loadData(CommonConstant.TYPE_REFRESH);
    }

    public void loadData(int type) {
        presenter.getNewsCommentList(String.valueOf(video.raw_data.group_id), String.valueOf(video.raw_data.item_id), mList.size() / CommonConstant.COMMENT_PAGE_SIZE + 1, type);

    }

    public void handleData(CommentResponse commentResponse, int loadType, boolean hasMore) {
        if(commentResponse==null){
            commentRv.setNoMoreData(true);
            return;
        }
        if (commentResponse.total_number == 0) {
            commentRv.setEmpty();
        } else {
            commentRv.setNoMoreData(hasMore);
            if (loadType == CommonConstant.TYPE_REFRESH) {
                this.mList.addAll(commentResponse.data);
                mAdapter.notifyDataSetChanged();
            } else {
                mAdapter.addAll(commentResponse.data);
            }
        }
    }

    @Override
    public int providerView() {
        return R.layout.hs_video_comment_dialog_layout;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mList.clear();
        super.onDismiss(dialog);
    }
}
