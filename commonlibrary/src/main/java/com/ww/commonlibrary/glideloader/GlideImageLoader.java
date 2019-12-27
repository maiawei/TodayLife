package com.ww.commonlibrary.glideloader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.github.chrisbanes.photoview.PhotoView;
import com.ww.commonlibrary.MyApplication;
import com.ww.commonlibrary.R;
import com.ww.commonlibrary.util.LogUtils;
import com.ww.commonlibrary.util.ScreenUtils;

import java.io.File;

import static com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.SCALE_TYPE_CUSTOM;

/**
 * created by wang.wei on 2019-12-16
 */
public class GlideImageLoader {

    private PhotoView mImageView;
    private ProgressBar mProgressBar;
    private SubsamplingScaleImageView mScaleImageView;
    private Context mContext;
    public GlideImageLoader(PhotoView imageView, SubsamplingScaleImageView scaleImageView, ProgressBar progressBar, Context context) {
        mImageView = imageView;
        mProgressBar = progressBar;
        mScaleImageView = scaleImageView;
        mContext=context;
    }

    public void load(final String url) {
        if (url == null) return;

        onConnecting();
        //set Listener & start
        ProgressAppGlideModule.expect(url, new ProgressAppGlideModule.UIonProgressListener() {
            @Override
            public void onProgress(long bytesRead, long expectedLength) {

                if (mProgressBar != null) {
                    mProgressBar.setProgress((int) (100 * bytesRead / expectedLength));
                }
            }

            @Override
            public float getGranualityPercentage() {
                return 1.0f;
            }
        });
        //Get Image
        Glide.with(mContext)
                .load(url)
                .error(R.mipmap.error_picture)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        onFinish();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        ProgressAppGlideModule.forget(url);
                        onFinish();
                        return false;
                    }
                })
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        showImage(resource, url);
                    }
                });
    }


    @SuppressLint("CheckResult")
    public void showImage(Drawable resource, String url) {
        final float scale = ScreenUtils.getScreenWidth() * 1.0f / resource.getIntrinsicWidth();
        if (resource.getIntrinsicHeight() * scale > ScreenUtils.getScreenHeight()) {
            // big pic
            mScaleImageView.setMaxScale(scale + 2);
            mScaleImageView.setMinScale(scale);
            mScaleImageView.setDoubleTapZoomScale(scale + 2);
            mScaleImageView.setMinimumScaleType(SCALE_TYPE_CUSTOM);
            Glide.with(mContext)
                    .load(url).downloadOnly(new SimpleTarget<File>() {
                @Override
                public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                    mScaleImageView.setImage(ImageSource.uri(Uri.fromFile(resource)), new ImageViewState(scale, new PointF(0, 0), 0));
                    mScaleImageView.setVisibility(View.VISIBLE);
                }
            });
            mProgressBar.setVisibility(View.GONE);
            mImageView.setVisibility(View.GONE);
            mScaleImageView.setVisibility(View.VISIBLE);

        } else {
            Glide.with(mContext).load(url).into(mImageView);
            mProgressBar.setVisibility(View.GONE);
            mScaleImageView.setVisibility(View.GONE);
            mImageView.setVisibility(View.VISIBLE);
        }
    }


    private void onConnecting() {
        if (mProgressBar != null) mProgressBar.setVisibility(View.VISIBLE);
        mImageView.setVisibility(View.GONE);
        mScaleImageView.setVisibility(View.GONE);
    }
    public void onFinish(){
        if (mProgressBar != null) mProgressBar.setVisibility(View.GONE);
    }

}
