package com.chm006.library.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Activity首页fragment用ViewPager切换
 * 可以手动设定ViewPager是否支持手动滑动
 * Created by chenmin on 2016/8/17.
 */
public class CustomViewPager extends ViewPager {
    private boolean noScroll = false;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置ViewPager能否滑动
     * @param noScroll
     */
    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (noScroll) {
            return false;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (noScroll){
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 切换当前要显示的页面（true：带动画，false：不带动画）
     * @param item
     * @param smoothScroll
     */
    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    /**
     * 切换当前要显示的页面（不带动画）
     * @param item
     */
    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }
}
