package com.ww.todaylife.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ww.commonlibrary.base.BaseAdapter;
import com.ww.commonlibrary.util.LogUtils;
import com.ww.commonlibrary.view.CircleImageView;
import com.ww.todaylife.R;
import com.ww.todaylife.bean.httpResponse.CommentData;

import java.util.List;


public class HotKeywordAdapter extends BaseAdapter<String, RecyclerView.ViewHolder> {
    boolean hasEdit;

    public HotKeywordAdapter(Context context, List<String> list) {
        super(context, list);
    }

    public void setEdit(boolean b) {
        hasEdit = b;
        notifyDataSetChanged();
    }

    public boolean getHasEdit() {
        return hasEdit;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(mInflater.inflate(R.layout.hot_search_item_layout, parent, false));

    }

    @Override
    public void bindViewHolderData(RecyclerView.ViewHolder holder, int position) {
        String item = mItems.get(position);
        if (item != null) {
            ItemViewHolder vh = (ItemViewHolder) holder;
            vh.keywordTv.setText(item);
            vh.deletedImage.setVisibility(hasEdit?View.VISIBLE:View.GONE);

        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView keywordTv;
        private ImageView deletedImage;

        public ItemViewHolder(View view) {
            super(view);
            deletedImage = view.findViewById(R.id.deleteImage);
            keywordTv = view.findViewById(R.id.keywordTv);
        }
    }


}
