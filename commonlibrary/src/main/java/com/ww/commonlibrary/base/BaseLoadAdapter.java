package com.ww.commonlibrary.base;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ww.commonlibrary.R;
import com.ww.commonlibrary.view.autoLoadMoreRecyclerView.RecyclerItemType;

import java.util.List;

import static com.ww.commonlibrary.view.autoLoadMoreRecyclerView.RecyclerItemType.TYPE_EMPTY;
import static com.ww.commonlibrary.view.autoLoadMoreRecyclerView.RecyclerItemType.TYPE_HEADER;
import static com.ww.commonlibrary.view.autoLoadMoreRecyclerView.RecyclerItemType.TYPE_LOADING;
import static com.ww.commonlibrary.view.autoLoadMoreRecyclerView.RecyclerItemType.TYPE_LOAD_COMPLETE;
import static com.ww.commonlibrary.view.autoLoadMoreRecyclerView.RecyclerItemType.TYPE_NETWORK;


/**
 * created by wang.wei on 2019-11-29
 */
public abstract class BaseLoadAdapter<E> extends BaseAdapter<E, RecyclerView.ViewHolder> {


    private View headerView;
    private HeaderHolder headerHolder;
    public BaseLoadAdapter(Context context, List list) {
        super(context, list);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_LOADING.type) {
            return new FootHolder(mInflater.inflate(R.layout.listview_load_footer, parent, false));
        } else if (viewType == TYPE_LOAD_COMPLETE.type) {
            View view = mInflater.inflate(R.layout.listview_load_footer, parent, false);
            TextView tv = view.findViewById(R.id.msgTv);
            view.findViewById(R.id.progressBar).setVisibility(View.GONE);
            tv.setText(TYPE_LOAD_COMPLETE.msgId);
            return new FootHolder(view);
        } else if (viewType == TYPE_EMPTY.type) {
            View view = mInflater.inflate(R.layout.listview_empty_footer, parent, false);
            return new FootHolder(view);
        } else if (viewType == TYPE_NETWORK.type) {
            View view = mInflater.inflate(R.layout.listview_load_footer, parent, false);
            TextView tv = view.findViewById(R.id.msgTv);
            view.findViewById(R.id.progressBar).setVisibility(View.GONE);
            tv.setText(TYPE_NETWORK.msgId);
            return new FootHolder(view);
        } else if (viewType == TYPE_HEADER.type) {
            if (headerView == null) {
                throw new NullPointerException("please set a header view !");
            }
            if(headerHolder==null){
                headerHolder=new HeaderHolder(headerView);
            }
            return headerHolder;
        }
        return onCreateItemVh(parent, viewType);
    }

    public void setLoadType(RecyclerItemType type) {
        int size = mItems.size();
        getItems().add(type);
        notifyItemInserted(size);
    }

    public void setHeaderView(View headerView) {
        getItems().add(0, TYPE_HEADER);
        this.headerView = headerView;
        notifyDataSetChanged();
    }

    public class HeaderHolder extends RecyclerView.ViewHolder {
        private HeaderHolder(View itemView) {
            super(itemView);
        }
    }

    public abstract RecyclerView.ViewHolder onCreateItemVh(ViewGroup parent, int viewType);

    @Override
    public int getItemViewType(int position) {
        Object object = getItem(position);
        if (object instanceof RecyclerItemType) {
            RecyclerItemType itemType = (RecyclerItemType) object;
            return itemType.type;
        }
        return getRecyclerItemType();
    }

    public abstract int getRecyclerItemType();


}
