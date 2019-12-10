package com.ww.todaylife.fragment.dialogfragment;


import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.ww.commonlibrary.util.UiUtils;
import com.ww.commonlibrary.view.CircleImageView;
import com.ww.commonlibrary.view.ClickAnimImage;
import com.ww.todaylife.R;
import com.ww.todaylife.base.BaseFullBottomSheetFragment;
import com.ww.todaylife.bean.httpResponse.CommentData;

import butterknife.BindView;


public class CommentDialogFragment extends BaseFullBottomSheetFragment {
    @BindView(R.id.replyCount)
    TextView replyCount;
    @BindView(R.id.header)
    CircleImageView header;
    @BindView(R.id.author)
    TextView author;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.likeImage)
    ClickAnimImage likeImage;
    @BindView(R.id.likeCount)
    TextView likeCount;
    @BindView(R.id.reply)
    TextView reply;
    private int KeyboardHeight = 0;
    private EditText replyEdit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.TransBottomSheetDialogStyle);
    }

    @Override
    public void initData() {
        Bundle bundle = getArguments();
        CommentData commentData = (CommentData) bundle.getSerializable("comment");
        Glide.with(getActivity()).load(commentData.comment.user_profile_image_url).into(header);
        author.setText(commentData.comment.user_name);
        content.setText(commentData.comment.text);
        likeCount.setText(String.valueOf(commentData.comment.digg_count));
        reply.setOnClickListener(v -> {
            UiUtils.showSoftInput(getActivity());
        });
    }

    @Override
    public int providerView() {
        return R.layout.comment_dialog_layout;
    }


}
