package com.ww.todaylife.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.ww.todaylife.HistoryAndStarNewsActivity;
import com.ww.todaylife.PicWatchActivity;
import com.ww.todaylife.R;
import com.ww.todaylife.SystemSettingActivity;
import com.ww.todaylife.TestActivity;
import com.ww.todaylife.base.BaseFragment;
import com.ww.todaylife.base.BasePresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class MeFragment extends BaseFragment {

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.noticesLayout)
    RelativeLayout noticesLayout;

    @Override
    public int getLayoutId() {
        return R.layout.me_fragment_layout;
    }

    @Override
    protected void initViews() {
        refreshLayout.setEnableLoadMore(false);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @OnClick({R.id.noticesLayout, R.id.starLayout, R.id.historyLayout, R.id.settingLayout,R.id.userCallBack})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.starLayout || view.getId() == R.id.historyLayout) {
            Intent intent = new Intent(mBaseActivity, HistoryAndStarNewsActivity.class);
            if (view.getId() == R.id.starLayout) {
                intent.putExtra("position", 0);
            } else {
                intent.putExtra("position", 1);
            }
            startActivity(intent);
        } else if (view.getId() == R.id.settingLayout) {
            startActivity(new Intent(mBaseActivity, SystemSettingActivity.class));
        }else if(view.getId() == R.id.userCallBack){
          //  startActivity(new Intent(mBaseActivity, TestActivity.class));

        }

    }
}
