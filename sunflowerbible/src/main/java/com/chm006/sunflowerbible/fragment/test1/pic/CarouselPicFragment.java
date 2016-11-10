package com.chm006.sunflowerbible.fragment.test1.pic;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chm006.library.base.fragment.BaseBackFragment;
import com.chm006.library.utils.my_utils.MyBitmapImageViewTarget;
import com.chm006.library.widget.ImageCarouselView;
import com.chm006.sunflowerbible.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

/**
 * 首页轮播图（可以打开关闭自动轮播功能）
 * Fresco 图片加载框架、Glide 图片加载框架、Picasso 图片加载框架
 * Created by chenmin on 2016/9/9.
 */
public class CarouselPicFragment extends BaseBackFragment {

    private String[] pics = new String[]{
            "http://www.bz55.com/uploads/allimg/140919/138-140919162646.jpg",
            "http://www.bz55.com/uploads/allimg/140919/138-140919162638.jpg",
            "http://i.imgur.com/SWssO7A.gif",
            "http://love.doghouse.com.tw/image/wallpaper/011102/bf1554.jpg",
            "http://www.feizl.com/upload2007/2011_05/110505164429412.jpg",
            "http://img5.imgtn.bdimg.com/it/u=713241191,1130951418&fm=206&gp=0.jpg"
    };

    public static CarouselPicFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        CarouselPicFragment fragment = new CarouselPicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mTitle = bundle.getString(ARG_TITLE);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_carousel_pic;
    }

    @Override
    public void init() {
        initToolbar();
        initImageCarouselView();
        initImageCarouselView2();
        initImageCarouselView3();
    }

    // Fresco 图片加载框架
    private void initImageCarouselView3() {
        ImageCarouselView imagecarouselview = findViewById(R.id.test1_imagecarouselview3);
        //设置 是否自动轮播、轮播图的长度
        imagecarouselview.setIsUserTouchedAndTotalCount(false, pics.length);
        imagecarouselview.setAdapter(new ImageCarouselView.Adapter() {
            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public View getView(int position) {
                View view = inflater.inflate(R.layout.item_drawee_view, null);
                SimpleDraweeView draweeView = (SimpleDraweeView) view.findViewById(R.id.draweeView);
                draweeView.setImageURI(Uri.parse(pics[position]));
                return view;
            }

            @Override
            public int getCount() {
                return pics.length;
            }
        });
    }

    // Glide 图片加载框架
    private void initImageCarouselView2() {
        ImageCarouselView imagecarouselview = findViewById(R.id.test1_imagecarouselview2);
        //设置 是否自动轮播、轮播图的长度
        imagecarouselview.setIsUserTouchedAndTotalCount(false, pics.length);
        imagecarouselview.setAdapter(new ImageCarouselView.Adapter() {
            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public View getView(int position) {
                View view = inflater.inflate(R.layout.item_imageview, null);
                ImageView imageView = (ImageView) view.findViewById(R.id.item_imageview_iv);
                if (pics[position].contains(".gif")) {
                    Glide.with(getActivity())
                            .load(pics[position])
                            //.centerCrop()
                            //.fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)//下次加载图片的时候，全尺寸的图片将从缓存中取出，重新调整大小，然后缓存。
                            .placeholder(R.mipmap.default_bg)//请求等待时的图片
                            .error(R.mipmap.default_bg)//请求失败时的图片
                            .into(imageView);
                } else {
                    Glide.with(getActivity())
                            .load(pics[position])
                            .asBitmap()
                            .centerCrop()
                            //.fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)//下次加载图片的时候，全尺寸的图片将从缓存中取出，重新调整大小，然后缓存。
                            .placeholder(R.mipmap.default_bg)//请求等待时的图片
                            .error(R.mipmap.default_bg)//请求失败时的图片
                            .into(new MyBitmapImageViewTarget(imageView));
                }
                return view;
            }

            @Override
            public int getCount() {
                return pics.length;
            }
        });
    }

    // Picasso 图片加载框架
    private void initImageCarouselView() {
        ImageCarouselView imagecarouselview = findViewById(R.id.test1_imagecarouselview);
        //设置 是否自动轮播、轮播图的长度
        imagecarouselview.setIsUserTouchedAndTotalCount(false, pics.length);
        imagecarouselview.setAdapter(new ImageCarouselView.Adapter() {
            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public View getView(int position) {
                View view = inflater.inflate(R.layout.item_imageview, null);
                ImageView imageView = (ImageView) view.findViewById(R.id.item_imageview_iv);
                Picasso.with(getActivity())
                        .load(pics[position])
                        .fit()
                        .centerCrop()
                        .placeholder(R.mipmap.default_bg)//请求等待时的图片
                        .error(R.mipmap.default_bg)//请求失败时的图片
                        .into(imageView);
                return view;
            }

            @Override
            public int getCount() {
                return pics.length;
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        initToolbarNav(toolbar, R.mipmap.ic_arrow_back_white_24dp, mTitle);
    }

}