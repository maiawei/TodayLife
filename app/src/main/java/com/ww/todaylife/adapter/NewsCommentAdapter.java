package com.ww.todaylife.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ww.commonlibrary.base.BaseAdapter;
import com.ww.commonlibrary.view.CircleImageView;
import com.ww.commonlibrary.view.ClickAnimImage;
import com.ww.todaylife.R;
import com.ww.todaylife.StartActivity;
import com.ww.todaylife.UserDetailActivity;
import com.ww.todaylife.bean.httpResponse.CommentData;

import java.util.List;


public class NewsCommentAdapter extends BaseAdapter<CommentData, RecyclerView.ViewHolder> {
    public NewsCommentAdapter(Context context, List<CommentData> list) {
        super(context, list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(mInflater.inflate(R.layout.common_item_layout, parent, false));

    }

    @Override
    public void bindViewHolderData(RecyclerView.ViewHolder holder, int position) {
        CommentData item = mItems.get(position);
        if (item != null) {
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            Glide.with(mContext).load(item.comment.user_profile_image_url).placeholder(R.mipmap.ic_default_avatar).into(viewHolder.headerImage);
            viewHolder.author.setText(item.comment.user_name);
            viewHolder.content.setText(item.comment.text);
            viewHolder.likeCount.setText(String.valueOf(item.comment.digg_count));
            viewHolder.likeLayout.setOnClickListener(v -> {
                viewHolder.likeImage.startAnimation();
            });
            viewHolder.author.setOnClickListener(v -> {
                gotoUserDetail(item);
            });
            viewHolder.headerImage.setOnClickListener(v -> {
                gotoUserDetail(item);
            });
        }
    }
    public void gotoUserDetail(CommentData item){
        Intent intent=new Intent(mContext, UserDetailActivity.class);
        intent.putExtra("userId",String.valueOf(item.comment.user_id));
        mContext.startActivity(intent);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView headerImage;
        private TextView author;
        private TextView content;
        private TextView likeCount;
        private ClickAnimImage likeImage;
        private LinearLayout likeLayout;
        public ItemViewHolder(View itemView) {
            super(itemView);
            likeImage=itemView.findViewById(R.id.likeImage);
            likeLayout=itemView.findViewById(R.id.likeLayout);
            headerImage = itemView.findViewById(R.id.header);
            author = itemView.findViewById(R.id.author);
            content = itemView.findViewById(R.id.content);
            likeCount = itemView.findViewById(R.id.likeCount);

        }
    }


}
