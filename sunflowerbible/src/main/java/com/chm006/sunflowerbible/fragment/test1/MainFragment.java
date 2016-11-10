package com.chm006.sunflowerbible.fragment.test1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.chm006.library.base.activity.BaseActivity;
import com.chm006.library.base.fragment.BaseFragment;
import com.chm006.sunflowerbible.R;
import com.chm006.sunflowerbible.adapter.InfoBaseAdapter;
import com.chm006.sunflowerbible.fragment.Test1Fragment;
import com.chm006.sunflowerbible.fragment.test1.main.CollapsingToolbarLayoutFragment;
import com.chm006.sunflowerbible.fragment.test1.main.ImageViewStyleFragment;
import com.chm006.sunflowerbible.fragment.test1.main.Level3LinkageFragment;
import com.chm006.sunflowerbible.fragment.test1.main.ProgressBarStyleFragment;
import com.chm006.sunflowerbible.fragment.test1.main.EditTextStyleFragment;
import com.chm006.sunflowerbible.fragment.test1.main.ScrollViewListenedFragment;
import com.chm006.sunflowerbible.fragment.test1.main.ToastStyleFragment;
import com.chm006.sunflowerbible.fragment.test1.main.WebViewFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页
 * Created by chenmin on 2016/8/12.
 */
public class MainFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private List<Object> data = new ArrayList<>();

    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public void init() {
        initData();
        initListView();
    }

    private void initData() {
        data.add("EditText样式");
        data.add("ImageView样式");
        data.add("ProgressBar样式");
        data.add("Toast样式");
        data.add("三级联动（省市区）");
        data.add("CollapsingToolbarLayout");
        data.add("滚动监听的ScrollView（title渐隐渐显）");
        data.add("WebView");
    }

    private void initListView() {
        ListView listView = findViewById(R.id.fragment_test1_home_lv);
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
            case 0://EditText样式
                startFragment(EditTextStyleFragment.newInstance((String) data.get(position)));
                break;
            case 1://ImageView样式
                startFragment(ImageViewStyleFragment.newInstance((String) data.get(position)));
                break;
            case 2://ProgressBar样式
                startFragment(ProgressBarStyleFragment.newInstance((String) data.get(position)));
                break;
            case 3://Toast样式
                startFragment(ToastStyleFragment.newInstance((String) data.get(position)));
                break;
            case 4://三级联动（省市区）
                startFragment(Level3LinkageFragment.newInstance((String) data.get(position)));
                break;
            case 5://CollapsingToolbarLayout
                startFragment(CollapsingToolbarLayoutFragment.newInstance((String) data.get(position)));
                break;
            case 6://滚动监听的ScrollView（title渐隐渐显）
                startFragment(ScrollViewListenedFragment.newInstance((String) data.get(position)));
                break;
            case 7://WebView
                startFragment(WebViewFragment.newInstance((String) data.get(position)));
                break;
        }
    }

}
