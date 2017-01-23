package com.chm006.sunflowerbible.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.chm006.library.base.fragment.BaseMainFragment;
import com.chm006.library.utils.ToastUtil;
import com.chm006.sunflowerbible.R;
import com.chm006.sunflowerbible.adapter.InfoBaseAdapter;
import com.chm006.sunflowerbible.fragment.home.five_in_a_row.view.fragment.FiveInARowFragment;
import com.chm006.sunflowerbible.fragment.test1.main.EditTextStyleFragment;
import com.chm006.sunflowerbible.fragment.test2.Linkage2PayFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chm00 on 2016/9/11.
 */
public class Test2Fragment extends BaseMainFragment implements AdapterView.OnItemClickListener {
    private List<Object> data = new ArrayList<>();
    private InfoBaseAdapter adapter;

    public static Test2Fragment newInstance() {
        Bundle args = new Bundle();
        Test2Fragment fragment = new Test2Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test2;
    }

    @Override
    public void init() {
        initToolbar();
        initListView();
        initData();
    }

    private void initListView() {
        adapter = new InfoBaseAdapter(data, inflater, R.layout.item_textview) {
            @Override
            public void setData(ViewHolder vh, Object itemData) {
                vh.getTextView(R.id.item_textview_tv).setText((String) itemData);
            }
        };
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    private void initData() {
        data.add("联动支付");
        adapter.notifyDataSetChanged(data);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        initToolbarNav(toolbar, R.mipmap.ic_menu_white_24dp, "Test2");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0://EditText样式
                start(Linkage2PayFragment.newInstance((String) data.get(position)));
                break;
        }
    }
}
