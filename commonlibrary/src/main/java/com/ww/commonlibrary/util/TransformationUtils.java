package com.ww.commonlibrary.util;


import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.request.target.ImageViewTarget;

public class TransformationUtils extends ImageViewTarget<Bitmap> {

    private ImageView target;

    public TransformationUtils(ImageView target) {
        super(target);
        this.target = target;
    }

    @Override
    protected void setResource(Bitmap resource) {
        target.setImageBitmap(resource);

        //获取原图的宽高
        int width = resource.getWidth();
        int height = resource.getHeight();

        int imageViewWidth = target.getWidth();

        float sy = (float) (imageViewWidth * 0.1) / (float) (width * 0.1);

        int imageViewHeight = (int) (height * sy);
        ViewGroup.LayoutParams params = target.getLayoutParams();
        params.height = imageViewHeight;
        target.setLayoutParams(params);
    }
}