package com.chm006.library.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.chm006.library.R;
import com.chm006.library.utils.DensityUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 图片轮播
 * Created by chenmin on 2016/8/22.
 */
public class ImageCarouselView extends FrameLayout implements ViewPager.OnPageChangeListener {
    private Context context;
    private int totalCount = Integer.MAX_VALUE;//总数，这是为实现无限滑动设置的
    private int showCount;//要显示的轮播图数量
    private int currentPosition = 0;//当前ViewPager的位置
    private ViewPager viewPager;
    private LinearLayout linearLayout;//展示指示器的布局
    private Adapter adapter;
    private int pageItemWidth;//每个指示器的宽度
    private boolean isUserTouched = false;//手指触碰状态
    private boolean isCarousel = false;//设置是否自动轮播
    private Timer mTimer = new Timer();

    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            if ((!isUserTouched) && isCarousel) {
                currentPosition = (currentPosition + 1) % totalCount;
                handler.sendEmptyMessage(100);
            }
        }
    };

    public void cancelTimer() {
        if (this.mTimer != null) {
            this.mTimer.cancel();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (currentPosition == totalCount - 1) {
                viewPager.setCurrentItem(showCount - 1, false);
            } else {
                viewPager.setCurrentItem(currentPosition);
            }
        }
    };

    public ImageCarouselView(Context context) {
        super(context);
        this.context = context;
    }

    public ImageCarouselView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    //获取到个ViewPager对象和LinearLayout对象，LinearLayout对象是轮播图底部的指示器
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View view = LayoutInflater.from(context).inflate(R.layout.image_carousel_layout, null);
        viewPager = (ViewPager) view.findViewById(R.id.image_carousel_viewpager);
        linearLayout = (LinearLayout) view.findViewById(R.id.image_carousel_linearlayout);
        pageItemWidth = DensityUtil.dp2px(context, 5);
        viewPager.addOnPageChangeListener(this);
        addView(view);
    }

    /**
     * 轮播图适配器
     */
    public interface Adapter {
        boolean isEmpty();

        View getView(int position);

        int getCount();
    }

    /**
     * 设置数据源,同时为ViewPager做展示
     */
    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
        if (adapter != null) {
            init();
        }
    }

    /**
     * 设置自动轮播是否生效 和 可滑动页面的数量
     */
    public void setIsUserTouchedAndTotalCount(boolean isCarousel, int totalCount) {
        this.isCarousel = isCarousel;
        this.totalCount = totalCount;
    }

    private void init() {
        viewPager.setAdapter(null);
        linearLayout.removeAllViews();//清空之前的数据
        if (adapter.isEmpty()) {
            return;
        }
        showCount = adapter.getCount();
        int f = currentPosition;
        for (int i = 0; i < showCount; i++) {
            View view = new View(context);
            //用来做指示器的View,通过state来做展示效果
            if (currentPosition == i) {
                view.setSelected(true);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(pageItemWidth + DensityUtil.dp2px(context, 3), pageItemWidth + DensityUtil.dp2px(context, 3));
                params.setMargins(pageItemWidth, 0, 0, 0);
                view.setLayoutParams(params);
            } else {
                view.setSelected(false);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(pageItemWidth, pageItemWidth);
                params.setMargins(pageItemWidth, 0, 0, 0);
                view.setLayoutParams(params);
            }
            view.setBackgroundResource(R.drawable.selector_carousel_layout_page);
            linearLayout.addView(view);
        }

        viewPager.setAdapter(new ViewPagerAdapter());
        viewPager.setCurrentItem(0);

        //让手指触碰到的时候自动轮播不起效
        viewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        isUserTouched = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        isUserTouched = false;
                        break;
                }
                return false;
            }
        });
        mTimer.schedule(mTimerTask, 3000, 3000);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.d("CarouselView", "onPageScrolled was invoke()");
    }

    @Override
    public void onPageSelected(int position) {
        currentPosition = position;
        int count = linearLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = linearLayout.getChildAt(i);
            if (position % showCount == i) {
                view.setSelected(true);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(pageItemWidth + DensityUtil.dp2px(context, 3), pageItemWidth + DensityUtil.dp2px(context, 3));
                params.setMargins(pageItemWidth, 0, 0, 0);
                view.setLayoutParams(params);
            } else {
                view.setSelected(false);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(pageItemWidth, pageItemWidth);
                params.setMargins(pageItemWidth, 0, 0, 0);
                view.setLayoutParams(params);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.d("CarouselView", "onPageScrollStateChanged was invoke()");
    }

    //ViewPager的适配器
    class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return totalCount;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position %= showCount;
            //调用接口的getView()获取使用者要展示的View;
            View view = adapter.getView(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);
           /* int position = viewPager.getCurrentItem();
            //实现Viewpager到第一页的实现能向左滑动
            if (position == 0) {
                position = showCount;
                viewPager.setCurrentItem(position, false);
            } else if (position == totalCount - 1) {//ViewPager到最后一页的实现向右滑动
                position = showCount - 1;
                viewPager.setCurrentItem(position, false);
            }*/
        }
    }

}
