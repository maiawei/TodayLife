package com.ww.todaylife;

import android.os.Bundle;
import android.widget.ImageView;

import com.ww.commonlibrary.util.GlideUtils;
import com.ww.todaylife.base.BasePresenter;
import com.ww.todaylife.base.BaseSwipeActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class PicWatchActivity extends BaseSwipeActivity {


    String u1;
    @BindView(R.id.image2)
    ImageView image2;
    ArrayList<Integer> photoList;

    @Override
    public int setContentId() {
        return R.layout.picture_layout;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        u1 = "http://p1.pstatp.com/large/pgc-image/28ebbfa7206646f085eff314664eb0b3";
        GlideUtils.loadImageList(this, u1, image2);
    }

    @Override
    public void initView() {
        photoList = new ArrayList<>();
        photoList.add(R.mipmap.mine_head_bg);
        photoList.add(R.mipmap.error_picture);
        photoList.add(R.mipmap.img_net_error);

    }


    @OnClick(R.id.image2)
    public void onViewClicked() {

    }
}
