package com.chm006.library.utils.my_utils;

import android.view.View;
import android.view.animation.AlphaAnimation;

/**
 * View的渐隐渐现动画效果
 * Created by chenmin on 2016/8/19.
 */
public class ViewAlphaAnimationUtil {
    private static AlphaAnimation mHideAnimation = null;
    private static AlphaAnimation mShowAnimation = null;

    /**
     * View渐隐动画效果
     */
    public static void setHideAnimation(View view, int duration) {
        if (null == view || duration < 0) {
            return;
        }
        if (null != mHideAnimation) {
            mHideAnimation.cancel();
        }
        mHideAnimation = new AlphaAnimation(1.0f, 0.0f);
        mHideAnimation.setDuration(duration);
        mHideAnimation.setFillAfter(true);
        view.startAnimation(mHideAnimation);
    }

    /**
     * View渐现动画效果
     */
    public static void setShowAnimation(View view, int duration) {
        if (null == view || duration < 0) {
            return;
        }
        if (null != mShowAnimation) {
            mShowAnimation.cancel();
        }
        mShowAnimation = new AlphaAnimation(0.0f, 1.0f);
        mShowAnimation.setDuration(duration);
        mShowAnimation.setFillAfter(true);
        view.startAnimation(mShowAnimation);
    }
}
