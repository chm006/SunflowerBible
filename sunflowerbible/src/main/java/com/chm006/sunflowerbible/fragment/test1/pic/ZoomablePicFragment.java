package com.chm006.sunflowerbible.fragment.test1.pic;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.chm006.library.base.fragment.BaseBackFragment;
import com.chm006.library.widget.ImageCarouselView;
import com.chm006.library.widget.ZoomableDraweeView;
import com.chm006.sunflowerbible.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片放大缩小（多图滑动）
 * Created by chenmin on 2016/9/9.
 */
public class ZoomablePicFragment extends BaseBackFragment {
    private List<String> pics = new ArrayList<>();

    public static ZoomablePicFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        ZoomablePicFragment fragment = new ZoomablePicFragment();
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
        return R.layout.fragment_zoomable_pic;
    }

    @Override
    public void init() {
        initToolbar();
        initData();
        initImageCarouselView3DraweeView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /*
    http://, https://                   HttpURLConnection 或者参考 使用其他网络加载方案
    file://FileInputStream              本地文件
    content://ContentResolver           Content provider
    asset://	AssetManager            asset目录下的资源
    res://	Resources.openRawResource   res目录下的资源
    data:mime/type;base64,	            Uri中指定图片数据  数据类型必须符合 rfc2397规定 (仅支持 UTF-8)
    res 示例:
    Uri uri = Uri.parse("res://包名(实际可以是任何字符串甚至留空)/" + R.drawable.ic_launcher);
     */

    private void initImageCarouselView3DraweeView() {
        ImageCarouselView imagecarouselview = findViewById(R.id.zoomable_imagecarouselview);
        //设置 是否自动轮播、轮播图的长度
        imagecarouselview.setIsUserTouchedAndTotalCount(false, pics.size());
        imagecarouselview.setAdapter(new ImageCarouselView.Adapter() {
            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public View getView(int position) {
                View view = inflater.inflate(R.layout.item_zoomable_pic, null);
                ZoomableDraweeView draweeView = (ZoomableDraweeView) view.findViewById(R.id.draweeView);
                draweeView.setImageURI(Uri.parse(pics.get(position)));
                return view;
            }

            @Override
            public int getCount() {
                return pics.size();
            }
        });
    }

    private void initData() {
        pics.add("http://www.bz55.com/uploads/allimg/140919/138-140919162646.jpg");
        pics.add("http://www.bz55.com/uploads/allimg/140919/138-140919162638.jpg");
        pics.add("http://i.imgur.com/SWssO7A.gif");
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        initToolbarNav(toolbar, R.mipmap.ic_arrow_back_white_24dp, mTitle);
    }

}