package com.ww.commonlibrary.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ww.commonlibrary.R;
import com.ww.commonlibrary.util.LogUtils;

import java.util.List;

/**
 * Created by wang.wei on 2015/10/29.
 */
public abstract class BaseAdapter<E, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected Context mContext;
    protected List<E> mItems;
    protected LayoutInflater mInflater;

    public void setItemClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    protected ItemClickListener clickListener;

    public BaseAdapter(Context context, List<E> list) {
        mContext = context;
        mItems = list;
        mInflater = LayoutInflater.from(context);

    }

    public List getItems() {
        return mItems;
    }

    @Override
    public int getItemCount() {
        if (mItems != null) {
            return mItems.size();
        } else {
            return 0;
        }
    }

    public E getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull final VH holder, final int position) {
        bindViewHolderData(holder, position);
        if (holder instanceof FootHolder) {
            return;
        }
        if (isSetItemBackground()) {
            holder.itemView.setBackgroundResource(R.drawable.item_click_style);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.itemClick(holder.getAdapterPosition());
                }
            }
        });
    }

    public boolean isSetItemBackground() {
        return false;
    }


    public abstract void bindViewHolderData(VH holder, final int position);

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * 刷新列表
     * refreshList
     *
     * @param list
     * @since 1.0
     */
    public void refreshList(List<E> list) {
        mItems.clear();
        mItems.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 删除一条数据
     * remove
     *
     * @param e
     * @since 1.0
     */
    public boolean remove(E e) {
        int pos = mItems.indexOf(e);
        if (pos >= 0) {
            mItems.remove(e);
            notifyItemRemoved(pos);
            return true;
        }
        return false;
    }

    /**
     * 删除一条数据
     * remove
     * 注意删除之后的通知后面数据刷新  可能会出现indexout
     *
     * @since 1.0
     */
    public boolean remove(int position) {
        if (mItems.get(position) != null && position != -1) {
            mItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mItems.size() - position);
            return true;
        }
        return false;
    }

    /**
     * 添加一条数据
     * add
     *
     * @param e
     * @since 1.0
     */
    public void add(E e) {
        int size = mItems.size();
        mItems.add(e);
        notifyItemInserted(size);
    }

    /**
     * 刷新一条数据
     *
     * @param position
     * @since 1.0
     */
    public void refreshItem(int position) {
        notifyItemChanged(position);
    }

    public void addTop(E e) {
        mItems.add(0, e);
        notifyItemInserted(0);
    }

    public void addAll(List<E> list) {
        int size = mItems.size();
        mItems.addAll(list);
        notifyItemRangeInserted(size, list.size());
    }

    public void addAllWithX(List<E> list) {
        int size = mItems.size();
        mItems.addAll(list);
        notifyItemRangeInserted(size - 1, list.size());
    }

    public void addAllFromTop(List<E> list) {
        mItems.addAll(0, list);
        notifyItemRangeInserted(0, list.size());
    }


    public interface ItemClickListener {
        void itemClick(int position);
    }


    public static class FootHolder extends RecyclerView.ViewHolder {

        public FootHolder(View itemView) {
            super(itemView);
        }
    }
}
