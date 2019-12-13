package com.ww.todaylife.fragment.dialogfragment;

import android.content.DialogInterface;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.ww.commonlibrary.view.widget.RvItemAnimator;
import com.ww.todaylife.R;
import com.ww.todaylife.adapter.ChanelAdapter;
import com.ww.todaylife.base.BaseFullBottomSheetFragment;
import com.ww.todaylife.bean.ChanelCategory;
import com.ww.todaylife.bean.eventBean.NewsTabEvent;
import com.ww.todaylife.dao.ChanelDao;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;

public class ChanelDialogFragment extends BaseFullBottomSheetFragment {
    private ArrayList<ChanelCategory> allList = new ArrayList<>();
    private ArrayList<ChanelCategory> selectedList;
    @BindView(R.id.recyclerView)
    RecyclerView mRv;
    private ChanelAdapter chanelAdapter;

    @Override
    public void initData() {
        initChanel();
        initRv();
    }

    public void initChanel() {
        allList.clear();
        selectedList = (ArrayList<ChanelCategory>) ChanelDao.getInstance().getSelectedChanel(mContext);
        String[] newsCategory = getResources().getStringArray(R.array.all_category);
        String[] newsCategoryCode = getResources().getStringArray(R.array.all_code);
        for (int i = 0; i < newsCategory.length; i++) {
            ChanelCategory liveCategory = new ChanelCategory();
            liveCategory.name = newsCategory[i];
            liveCategory.typeCode = newsCategoryCode[i];
            allList.add(liveCategory);
        }
        //调整顺序
        allList.removeAll(selectedList);
        ChanelCategory chanel = new ChanelCategory();
        chanel.name = "频道推荐";
        chanel.typeCode = "recommend_chanel";
        chanel.isTitle = true;
        allList.add(0, chanel);
        allList.addAll(0, selectedList);
        ChanelCategory chanel2 = new ChanelCategory();
        chanel2.name = "我的频道";
        chanel2.isTitle = true;
        chanel2.typeCode = "my_chanel";
        allList.add(0, chanel2);

    }

    private void initRv() {
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 4);
        mRv.setLayoutManager(layoutManager);
        mRv.setItemAnimator(new RvItemAnimator());
        //  mRv.addItemDecoration(new DividerGridItemDecoration(mContext,R.drawable.chanel_divider_bg));
        chanelAdapter = new ChanelAdapter(mContext, allList, selectedList);
        chanelAdapter.setSpanSize(layoutManager);
        mRv.setAdapter(chanelAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRv);

    }

    @Override
    public int providerView() {
        return R.layout.chanel_dialog_layout;
    }

    private ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int position = viewHolder.getLayoutPosition();
            //不用交换
            if (position == 0 || position == 1) {
                return 0;
            }
            if (position >= selectedList.size() + 1) {
                return 0;
            }
            int swipeFlags = 0;
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            return makeMovementFlags(dragFlags, swipeFlags);

        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();   //拖动的position
            int toPosition = target.getAdapterPosition();
            int position = viewHolder.getLayoutPosition();
            int position2 = target.getLayoutPosition();
            //第一个item不用交换
            if (position == 1 || position2 == 1) {
                return false;
            }
            if (toPosition > selectedList.size()) {
                return false;
            }
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(selectedList, i-1, i );
                    Collections.swap(allList, i, i + 1);

                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(selectedList, i-1, i - 2);
                    Collections.swap(allList, i, i - 1);
                }
            }
            chanelAdapter.notifyItemMoved(fromPosition, toPosition);
            return true;
        }


        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            //暂不处理
        }


        @Override
        public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder current, RecyclerView.ViewHolder target) {
            return true;
        }


        @Override
        public boolean isLongPressDragEnabled() {
            //return true后，可以实现长按拖动排序和拖动动画了
            return true;
        }
    };

    @Override
    public void onDismiss(@NotNull DialogInterface dialog) {
        EventBus.getDefault().post(new NewsTabEvent(selectedList));
        super.onDismiss(dialog);
    }
}
