package com.chm006.sunflowerbible.fragment.test1;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.chm006.library.base.fragment.BaseFragment;
import com.chm006.library.widget.DisableScrollingGridView;
import com.chm006.sunflowerbible.R;
import com.chm006.sunflowerbible.adapter.InfoBaseAdapter;
import com.chm006.sunflowerbible.fragment.test1.map.BaiduMapFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 地图模块
 * Created by chenmin on 2016/8/12.
 */
public class MapFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private List<Object> data = new ArrayList<>();

    public static MapFragment newInstance() {
        Bundle args = new Bundle();
        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_map;
    }

    @Override
    public void init() {
        initGridView();
    }

    private void initGridView() {
        data.add("百度地图");
        DisableScrollingGridView listView = findViewById(R.id.fragment_test1_map_gv);
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
                startFragment(BaiduMapFragment.newInstance((String) data.get(position)));
                break;
        }
    }
}
