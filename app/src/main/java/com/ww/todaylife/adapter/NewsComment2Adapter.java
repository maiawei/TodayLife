package com.ww.todaylife.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ww.commonlibrary.MyApplication;
import com.ww.commonlibrary.base.BaseLoadAdapter;
import com.ww.commonlibrary.util.ScreenUtils;
import com.ww.commonlibrary.util.StringUtils;
import com.ww.commonlibrary.util.TimeUtils;
import com.ww.commonlibrary.util.UiUtils;
import com.ww.commonlibrary.view.CircleImageView;
import com.ww.commonlibrary.view.ClickAnimImage;
import com.ww.commonlibrary.view.LimitTextView;
import com.ww.commonlibrary.view.widget.CenterAlignImageSpan;
import com.ww.commonlibrary.view.widget.LinkTouchMovementMethod;
import com.ww.commonlibrary.view.widget.TouchableSpan;
import com.ww.todaylife.R;
import com.ww.todaylife.UserDetailActivity;
import com.ww.todaylife.base.BaseViewHolder;
import com.ww.todaylife.bean.httpResponse.CommentData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;

import static com.ww.commonlibrary.view.autoLoadMoreRecyclerView.RecyclerItemType.TYPE_NORMAL;

/**
 * created by wang.wei on 2019-11-29
 */
public class NewsComment2Adapter extends BaseLoadAdapter<CommentData> {


    public NewsComment2Adapter(Context context, List<CommentData> list) {
        super(context, list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateItemVh(ViewGroup parent, int viewType) {
        return new ItemViewHolder(mInflater.inflate(R.layout.common_item_layout, parent, false));
    }

    @Override
    public int getRecyclerItemType() {
        return TYPE_NORMAL.type;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void bindViewHolderData(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            CommentData item = mItems.get(position);
            if (item != null) {
                ItemViewHolder viewHolder = (ItemViewHolder) holder;
                Glide.with(mContext).load(item.comment.user_profile_image_url).placeholder(R.mipmap.ic_default_avatar).into(viewHolder.headerImage);
                viewHolder.author.setText(item.comment.user_name);
                viewHolder.content.setContent(item.comment.text, ScreenUtils.getScreenWidth()-ScreenUtils.dip2px(mContext,75));
                if (item.comment.reply_count > 0) {
                    viewHolder.commentDate.setText(TimeUtils.getShortTime(item.comment.create_time * 1000) + " · ");
                    viewHolder.replyCount.setVisibility(View.VISIBLE);
                    viewHolder.replyCount.setText(item.comment.reply_count + "回复");
                } else {
                    viewHolder.replyCount.setVisibility(View.GONE);
                    viewHolder.commentDate.setText(TimeUtils.getShortTime(item.comment.create_time * 1000) + " · 回复");
                }
                if (item.comment.reply_list != null && item.comment.reply_list.size() > 0) {
                    viewHolder.authorReplyLayout.setVisibility(View.VISIBLE);
                    viewHolder.allReplyTv.setText("查看全部" + item.comment.reply_count + "回复");
                    viewHolder.ReplyAuthorTv.setText(getAuthorReplayStr(item.comment.reply_list.get(0).user_name, item.comment.reply_list.get(0).text, item.comment.reply_list.get(0).user_id, viewHolder.ReplyAuthorTv));

                } else {
                    viewHolder.authorReplyLayout.setVisibility(View.GONE);
                }
                viewHolder.likeCount.setText((item.comment.digg_count > 0 ? item.comment.digg_count + "" : "赞"));
                viewHolder.likeLayout.setOnClickListener(v -> {
                    viewHolder.likeImage.startAnimation();
                });
                viewHolder.author.setOnClickListener(v -> {
                    gotoUserDetail(String.valueOf(item.comment.user_id));
                });
                viewHolder.headerImage.setOnClickListener(v -> {
                    gotoUserDetail(String.valueOf(item.comment.user_id));
                });
                viewHolder.content.setOnClickListener(v -> {
                    viewHolder.itemView.performClick();
                });
            }
        }
    }

    private void gotoUserDetail(String id) {
        Intent intent = new Intent(mContext, UserDetailActivity.class);
        intent.putExtra("userId", id);
        mContext.startActivity(intent);
    }

    public SpannableStringBuilder getAuthorReplayStr(String userName, String Content, String id, TextView textView) {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(userName + "作者");
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#275E91"));
        stringBuilder.setSpan(foregroundColorSpan, 0, stringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        Drawable drawable = MyApplication.getApp().getResources().getDrawable(com.ww.commonlibrary.R.mipmap.author_icon);
        drawable.setBounds(userName.length(), 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        CenterAlignImageSpan ab = new CenterAlignImageSpan(drawable);
        stringBuilder.setSpan(ab, userName.length(), stringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ClickableSpan clickableSpan = new TouchableSpan(ContextCompat.getColor(mContext,R.color.blue),
                ContextCompat.getColor(mContext,R.color.main_red),ContextCompat.getColor(mContext,R.color.translucent)) {
            @Override
            public void onClick(@NotNull View view) {
                gotoUserDetail(id);
            }

        };
        stringBuilder.setSpan(clickableSpan, 0, userName.length() + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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
        LimitTextView content;
        @BindView(R.id.likeImage)
        ClickAnimImage likeImage;
        @BindView(R.id.likeCount)
        TextView likeCount;
        @BindView(R.id.likeLayout)
        LinearLayout likeLayout;
        @BindView(R.id.commentDate)
        TextView commentDate;
        @BindView(R.id.replyCount)
        TextView replyCount;
        @BindView(R.id.authorReplyLayout)
        LinearLayout authorReplyLayout;
        @BindView(R.id.ReplyAuthorTv)
        TextView ReplyAuthorTv;
        @BindView(R.id.allReplyTv)
        TextView allReplyTv;

        private ItemViewHolder(View itemView) {
            super(itemView);
        }
    }

}
