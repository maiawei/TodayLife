package com.ww.todaylife.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.ww.todaylife.R;
import com.ww.todaylife.base.BaseFragment;
import com.ww.todaylife.base.BasePresenter;

import butterknife.BindView;

/**
 * created by wang.wei on 2019-11-27
 */
public class GalleryItemFragment extends BaseFragment {

    @BindView(R.id.photoView)
    PhotoView photoView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private String imageUrl;

    @Override
    public int getLayoutId() {
        return R.layout.gallery_item_fragment_layout;
    }

    public static GalleryItemFragment newsInstance(String url) {
        GalleryItemFragment fragment = new GalleryItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString("imageUrl", url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initViews() {
        imageUrl = getArguments().getString("imageUrl");
        Glide.with(mBaseActivity).load(imageUrl).error(R.mipmap.ic_default).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                if(progressBar!=null)
                progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                if(progressBar!=null)
                progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(photoView);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
