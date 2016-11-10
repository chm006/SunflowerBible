package com.chm006.sunflowerbible.fragment.test1;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.chm006.library.base.fragment.BaseFragment;
import com.chm006.sunflowerbible.R;
import com.chm006.sunflowerbible.adapter.InfoBaseAdapter;
import com.chm006.sunflowerbible.fragment.Test1Fragment;
import com.chm006.sunflowerbible.fragment.test1.pic.CarouselPicFragment;
import com.chm006.sunflowerbible.fragment.test1.pic.PhotoViewFragment;
import com.chm006.sunflowerbible.fragment.test1.pic.RecyclerViewFragment;
import com.chm006.sunflowerbible.fragment.test1.pic.ZoomablePicFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片模块
 * Created by chenmin on 2016/8/12.
 */
public class PicFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private List<Object> data = new ArrayList<>();

    public static PicFragment newInstance() {
        Bundle args = new Bundle();
        PicFragment fragment = new PicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pic;
    }

    @Override
    public void init() {
        initData();
        initView();
    }

    private void initData() {
        data.add("首页轮播图（可以打开关闭自动轮播功能）");
        data.add("图片放大缩小（多图滑动）");
        data.add("PhotoView图片放大缩小");
        data.add("RecyclerView防止图片错乱、上拉加载下拉刷新");
    }

    private void initView() {
        ListView listView = findViewById(R.id.fragment_test1_pic_lv);
        listView.setAdapter(new InfoBaseAdapter(data, inflater, R.layout.item_textview) {
            @Override
            public void setData(ViewHolder vh, Object itemData) {
                vh.getTextView(R.id.item_textview_tv).setText((String) itemData);
            }
        });
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                startFragment(CarouselPicFragment.newInstance((String) data.get(position)));
                break;
            case 1:
                startFragment(ZoomablePicFragment.newInstance((String) data.get(position)));
                break;
            case 2:
                startFragment(PhotoViewFragment.newInstance((String) data.get(position)));
                break;
            case 3:
                startFragment(RecyclerViewFragment.newInstance((String) data.get(position)));
                break;
        }
    }

}
