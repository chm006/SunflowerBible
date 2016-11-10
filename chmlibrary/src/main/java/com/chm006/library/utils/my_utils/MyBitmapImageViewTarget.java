package com.chm006.library.utils.my_utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

/**
 * 为了解决Glide图片centerCrop()计算错误,导致图片显示变形,在不同的情况下把imageview设置不同的ScaleType
 * Created by chenmin on 2016/9/27.
 */

public class MyBitmapImageViewTarget extends BitmapImageViewTarget {
    private ImageView view;

    public MyBitmapImageViewTarget(ImageView view) {
        super(view);
        this.view = view;
    }

    @Override
    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
        if (bitmap != null && view.getScaleType() != ImageView.ScaleType.FIT_XY) {
            view.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        super.onResourceReady(bitmap, anim);
    }

    @Override
    protected void setResource(Bitmap resource) {
        super.setResource(resource);
    }

    @Override
    public void onLoadFailed(Exception e, Drawable errorDrawable) {
        if (errorDrawable != null && view != null && view.getScaleType() != ImageView.ScaleType.CENTER_CROP) {
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        super.onLoadFailed(e, errorDrawable);
    }

    @Override
    public void onLoadStarted(Drawable placeholder) {
        if (placeholder != null && placeholder != null && view != null && view.getScaleType() != ImageView.ScaleType.CENTER_CROP) {
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        super.onLoadStarted(placeholder);
    }

    @Override
    public void onLoadCleared(Drawable placeholder) {
        if (placeholder != null && placeholder != null && view != null && view.getScaleType() != ImageView.ScaleType.CENTER_CROP) {
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        super.onLoadCleared(placeholder);
    }

}