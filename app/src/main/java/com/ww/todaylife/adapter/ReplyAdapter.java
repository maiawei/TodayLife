package com.ww.todaylife.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ww.commonlibrary.base.BaseLoadAdapter;
import com.ww.commonlibrary.util.GlideUtils;
import com.ww.commonlibrary.util.TimeUtils;
import com.ww.commonlibrary.view.CircleImageView;
import com.ww.commonlibrary.view.ClickAnimImage;
import com.ww.commonlibrary.view.widget.LinkTouchMovementMethod;
import com.ww.commonlibrary.view.widget.TouchableSpan;
import com.ww.todaylife.R;
import com.ww.todaylife.UserDetailActivity;
import com.ww.todaylife.base.BaseViewHolder;
import com.ww.todaylife.bean.httpResponse.CommentReply;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;

import static com.ww.commonlibrary.view.autoLoadMoreRecyclerView.RecyclerItemType.TYPE_NORMAL;

/**
 * created by wang.wei on 2019-11-29
 */
public class ReplyAdapter extends BaseLoadAdapter<CommentReply> {


    public ReplyAdapter(Context context, List<CommentReply> list) {
        super(context, list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateItemVh(ViewGroup parent, int viewType) {
        return new ItemViewHolder(mInflater.inflate(R.layout.reply_item_layout, parent, false));
    }

    @Override
    public int getRecyclerItemType() {
        return TYPE_NORMAL.type;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void bindViewHolderData(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            CommentReply item = mItems.get(position);
            if (item != null) {
                ItemViewHolder vh = (ItemViewHolder) holder;
                GlideUtils.loadAvatar(mContext, item.user.avatar_url, vh.headerImage);
                vh.author.setText(item.user.screen_name);
                vh.likeCount.setText((item.digg_count > 0 ? item.digg_count + "" : "赞"));
                vh.commentDate.setText(TimeUtils.getShortTime(item.create_time * 1000));
                if (item.reply_to_comment != null) {
                    vh.content.setText(getReplyContent(item.text, item.reply_to_comment.user_name, item.reply_to_comment.text, item.reply_to_comment.user_id, vh.content));
                } else {
                    vh.content.setText(item.text);
                }
                vh.author.setOnClickListener(v -> {
                    gotoUserDetail(item.user.user_id);
                });
                vh.headerImage.setOnClickListener(v -> {
                    gotoUserDetail(item.user.user_id);
                });
            }
        }
    }

    private void gotoUserDetail(long id) {
        Intent intent = new Intent(mContext, UserDetailActivity.class);
        intent.putExtra("userId", String.valueOf(id));
        mContext.startActivity(intent);
    }

    public SpannableStringBuilder getReplyContent(String text, String userName, String Content, long id, TextView textView) {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(text + "//@" + userName);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#275E91"));
        stringBuilder.setSpan(foregroundColorSpan, text.length() + 2, stringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ClickableSpan clickableSpan = new TouchableSpan(ContextCompat.getColor(mContext,R.color.blue),
                ContextCompat.getColor(mContext,R.color.main_red),ContextCompat.getColor(mContext,R.color.transparent)) {
            @Override
            public void onClick(@NotNull View view) {
                gotoUserDetail(id);
            }

        };
        stringBuilder.setSpan(clickableSpan, text.length() + 2, stringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setMovementMethod(new LinkTouchMovementMethod());
        stringBuilder.append(":" + Content);
        return stringBuilder;
    }

    public static class ItemViewHolder extends BaseViewHolder {
        @BindView(R.id.header)
        CircleImageView headerImage;
        @BindView(R.id.author)
        TextView author;
        @BindView(R.id.content)
        TextView content;
        @BindView(R.id.likeImage)
        ClickAnimImage likeImage;
        @BindView(R.id.likeCount)
        TextView likeCount;
        @BindView(R.id.likeLayout)
        LinearLayout likeLayout;
        @BindView(R.id.commentDate)
        TextView commentDate;


        private ItemViewHolder(View itemView) {
            super(itemView);
        }
    }

}
