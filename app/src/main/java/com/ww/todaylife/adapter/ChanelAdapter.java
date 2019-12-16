package com.ww.todaylife.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ww.commonlibrary.base.BaseAdapter;
import com.ww.commonlibrary.util.LogUtils;
import com.ww.commonlibrary.util.TimeUtils;
import com.ww.todaylife.R;
import com.ww.todaylife.bean.ChanelCategory;

import java.util.ArrayList;
import java.util.List;

public class ChanelAdapter extends BaseAdapter<ChanelCategory, ChanelAdapter.ChanelVh> {

    private static final int CHANEL = 0;
    private static final int TITLE = 1;
    private boolean isEdit;
    ArrayList<ChanelCategory> selectedList;

    public ChanelAdapter(Context context, List list, ArrayList<ChanelCategory> selectedList) {
        super(context, list);
        this.selectedList = selectedList;
    }

    @SuppressLint("CheckResult")
    @Override
    public void bindViewHolderData(ChanelVh holder, int position) {
        ChanelCategory item = mItems.get(position);
        if (item != null) {
            if (item.isSelected && isEdit && !item.isTitle) {
                if (item.typeCode.equals("")) {
                    holder.deleteImage.setVisibility(View.GONE);

                } else {
                    holder.deleteImage.setVisibility(View.VISIBLE);
                }
            } else {
                holder.deleteImage.setVisibility(View.GONE);
            }

            if (!item.isSelected && !item.isTitle) {
                holder.chanelName.setBackgroundResource(R.mipmap.chanel_normal_bg);
                holder.chanelName.setText("+ " + item.name);

            } else {
                if(item.isSelected){
                    holder.chanelName.setBackgroundResource(R.drawable.chanel_item_bg);
                }else {
                    holder.chanelName.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
                }
                holder.chanelName.setText(item.name);
            }
            if (holder instanceof TitleVh) {
                TitleVh vh = (TitleVh) holder;
                if (item.typeCode.equals("my_chanel")) {
                    vh.editTv.setVisibility(View.VISIBLE);
                } else {
                    vh.editTv.setVisibility(View.GONE);
                }
                if (isEdit) {
                    vh.editTv.setText("完成");
                } else {
                    vh.editTv.setText("编辑");
                }
                vh.editTv.setOnClickListener(v -> {
                    isEdit = !isEdit;
                    notifyDataSetChanged();

                });
            }

            holder.chanelName.setOnLongClickListener(v -> {
                if (item.isSelected && !item.isTitle) {
                    isEdit = true;
                }
                notifyDataSetChanged();
                return true;
            });
            holder.chanelName.setOnClickListener(v -> {
                if (TimeUtils.isFastClick()) {
                    return;
                }
                if (!isEdit) {
                    return;
                }
                if (item.typeCode.equals("") || item.isTitle) {
                    return;
                }
                if (item.isSelected) {
                    dealRemove(holder.getAdapterPosition());
                } else {
                    dealAdd(holder.getAdapterPosition());
                }

            });

        }
    }


    public void dealRemove(int position) {
        if (position - 1 < 0 || position - 1 >= selectedList.size()) {
            return;
        }
        mItems.get(position).isSelected = false;
        //DataProcessUtils.swap(mItems, position, selectedList.size() + 1);
        ChanelCategory category = mItems.remove(position);
        mItems.add(selectedList.size() + 1, category);
        logList();
        selectedList.remove(position - 1);
        notifyItemMoved(position, selectedList.size() + 2);
        notifyItemRangeChanged(0, mItems.size());
    }

    public void dealAdd(int position) {
        mItems.get(position).isSelected = true;
        selectedList.add(mItems.get(position));
//        DataProcessUtils.swap(mItems, position, selectedList.size());
        ChanelCategory category = mItems.remove(position);
        mItems.add(selectedList.size(), category);
        notifyItemMoved(position, selectedList.size());
        notifyItemRangeChanged(0, mItems.size());

    }

    public void logList() {
        for (ChanelCategory chanelCategory : mItems) {
            LogUtils.e(chanelCategory.name);
        }
    }

    @NonNull
    @Override
    public ChanelVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == CHANEL) {
            view = mInflater.inflate(R.layout.chanel_item_layout, parent, false);
            return new ChanelVh(view);
        } else {
            view = mInflater.inflate(R.layout.chanel_title_item_layout, parent, false);
            return new TitleVh(view);

        }

    }

    public void setSpanSize(GridLayoutManager manager) {
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mItems.get(position).isTitle) {
                    return 4;
                }
                return 1;
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        ChanelCategory item = mItems.get(position);
        if (item.isTitle) {
            return TITLE;
        } else {
            return CHANEL;
        }
    }

    public class ChanelVh extends RecyclerView.ViewHolder {
        TextView chanelName;
        ImageView deleteImage;

        public ChanelVh(@NonNull View itemView) {
            super(itemView);
            chanelName = itemView.findViewById(R.id.chanelName);
            deleteImage = itemView.findViewById(R.id.deleteImage);
        }
    }

    public class TitleVh extends ChanelVh {
        TextView editTv;

        public TitleVh(@NonNull View itemView) {
            super(itemView);
            editTv = itemView.findViewById(R.id.editTv);
        }
    }

}
