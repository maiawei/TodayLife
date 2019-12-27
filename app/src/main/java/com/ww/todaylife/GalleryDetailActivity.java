package com.ww.todaylife;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.ww.commonlibrary.MyApplication;
import com.ww.commonlibrary.util.FileUtils;
import com.ww.commonlibrary.util.SystemUtils;
import com.ww.commonlibrary.view.widget.DragCloseHelper;
import com.ww.todaylife.adapter.GalleryImageAdapter;
import com.ww.todaylife.base.BasePresenter;
import com.ww.todaylife.base.BaseSwipeActivity;
import com.ww.todaylife.bean.httpResponse.NewsDetail;
import com.ww.todaylife.util.IntentDataUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class GalleryDetailActivity extends BaseSwipeActivity {
    @BindView(R.id.viewPage)
    ViewPager mViewPager;
    ArrayList<String> mList = new ArrayList<>();
    GalleryImageAdapter mAdapter;
    @BindView(R.id.positionTv)
    TextView positionTv;
    @BindView(R.id.saveTv)
    TextView saveTv;
    private DragCloseHelper dragCloseHelper;
    private boolean scrolling;
    @BindView(R.id.rootLayout)
    RelativeLayout rootLayout;
    int currentPosition;
    GalleryImageAdapter.AdapterViewHolder currentVh;
    private Context mThis;
    @Override
    public int setContentId() {
        return R.layout.gallery_detail_layout;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        if (getIntent().getStringArrayListExtra("urls") != null) {
            mList = getIntent().getStringArrayListExtra("urls");
        } else {
            mList = getUrlList(IntentDataUtils.getNews());
        }
        currentPosition = getIntent().getIntExtra("position", 0);
        positionTv.setText(String.format("%1$d/%2$d", currentPosition + 1, mList.size()));
        mViewPager.setCurrentItem(currentPosition, false);
        mAdapter = new GalleryImageAdapter(mThis, mList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setCurrentItem(currentPosition);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @SuppressLint("DefaultLocale")
            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                positionTv.setText(String.format("%1$d/%2$d", position + 1, mList.size()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                scrolling = state != 0;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    public boolean getCurrentViewStatus() {
        currentVh = (GalleryImageAdapter.AdapterViewHolder) mAdapter.getCurrentView().getTag();
        if (currentVh.photoView.getVisibility() == View.VISIBLE && currentVh.photoView.getScale() != 1) {
            return true;
        }
        if (currentVh.scaleImageView.getVisibility() == View.VISIBLE) {
            return true;
        }
        return false;
    }

    @Override
    public void initView() {
        mThis = this;
        SystemUtils.setWindowStatusBarColor(this, R.color.transparent);
        dragCloseHelper = new DragCloseHelper(this);
        dragCloseHelper.setDragCloseViews(rootLayout, mViewPager);
        dragCloseHelper.setClickListener((view) -> {
            GalleryDetailActivity.this.finish();
        });
        dragCloseHelper.setDragCloseListener(new DragCloseHelper.DragCloseListener() {
            @Override
            public boolean intercept() {
                if (mAdapter.getCurrentView() != null) {
                    return scrolling || getCurrentViewStatus();

                } else {
                    return scrolling;
                }
            }

            @Override
            public void dragStart() {
            }

            @Override
            public void dragging(float percent) {
                //拖拽中。percent当前的进度，取值0-1，可以在此额外处理一些逻辑
            }

            @Override
            public void dragCancel() {
                //拖拽取消，会自动复原。可以在此额外处理一些逻辑
            }

            @Override
            public void dragClose(boolean isShareElementMode) {
                GalleryDetailActivity.this.finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.search_window_out);
    }

    @Override
    protected void onDestroy() {
        mThis = null;
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        rootLayout.getBackground().mutate().setAlpha(0);
        super.onBackPressed();
    }

    public ArrayList<String> getUrlList(ArrayList<NewsDetail> news) {
        ArrayList<String> urls = new ArrayList<>();
        for (int i = 0; i < news.size(); i++) {
            urls.add(news.get(i).img_url);
        }
        return urls;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (dragCloseHelper.handleEvent(event)) {
            return true;
        } else {
            return super.dispatchTouchEvent(event);
        }
    }

    @OnClick(R.id.saveTv)
    public void onViewClicked() {
        FileUtils.downImage(MyApplication.getApp(), mList.get(currentPosition));
    }
}
