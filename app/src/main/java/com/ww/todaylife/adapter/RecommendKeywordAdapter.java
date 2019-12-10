package com.ww.todaylife.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ww.commonlibrary.base.BaseAdapter;
import com.ww.todaylife.R;
import com.ww.todaylife.base.BaseViewHolder;
import com.ww.todaylife.bean.httpResponse.KeyWordDetail;
import com.ww.todaylife.bean.httpResponse.NewsDetail;

import java.util.List;

import butterknife.BindView;

public class RecommendKeywordAdapter extends BaseAdapter<KeyWordDetail, RecyclerView.ViewHolder> {


    public RecommendKeywordAdapter(Context context, List<KeyWordDetail> list) {
        super(context, list);
    }

    @Override
    public void bindViewHolderData(RecyclerView.ViewHolder holder, int position) {
        KeyWordDetail item = mItems.get(position);
        if (item != null) {
            ItemHorViewHolder viewHolder = (ItemHorViewHolder) holder;
            viewHolder.keywordTv.setText(item.keyword);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.recommend_keyword_item_layout, parent, false);
        return new ItemHorViewHolder(view);

    }


    public static class ItemHorViewHolder extends BaseViewHolder {

        @BindView(R.id.keywordTv)
        TextView keywordTv;

        public ItemHorViewHolder(View itemView) {
            super(itemView);

        }
    }
}
