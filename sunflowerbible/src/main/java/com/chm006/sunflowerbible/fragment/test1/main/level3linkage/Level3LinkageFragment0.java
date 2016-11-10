package com.chm006.sunflowerbible.fragment.test1.main.level3linkage;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.model.IPickerViewData;
import com.chm006.library.base.fragment.BaseFragment;
import com.chm006.sunflowerbible.R;
import com.chm006.sunflowerbible.fragment.test1.main.level3linkage.bean.PickerViewData;
import com.chm006.sunflowerbible.fragment.test1.main.level3linkage.bean.ProvinceBean;
import com.lljjcoder.citypickerview.widget.CityPickerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 三级联动（省市区、时间）
 * Created by chenmin on 2016/9/12.
 */
public class Level3LinkageFragment0 extends BaseFragment {

    private TextView time_tv, city_tv1, city_tv2;
    private TimePickerView pvTime;

    public static Level3LinkageFragment0 newInstance() {
        Bundle args = new Bundle();
        Level3LinkageFragment0 fragment = new Level3LinkageFragment0();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_level3linkage0;
    }

    @Override
    public void init() {
        initTimePickerView();
        initOptionsPickerView();
        initCityPicker();
    }

    private void initTimePickerView() {
         /*
        Gradle引用：
        compile 'com.bigkoo:pickerview:2.1.1'
        这个是支持农历的分支：
        compile 'com.bigkoo:pickerview:lunar.1.0'
        */
        time_tv = (TextView) rootView.findViewById(R.id.time_tv);
        //时间选择器
        pvTime = new TimePickerView(getActivity(), TimePickerView.Type.YEAR_MONTH_DAY);
        //控制时间范围
        //Calendar calendar = Calendar.getInstance();
        //pvTime.setRange(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.YEAR));//要在setTime 之前才有效果哦
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                time_tv.setText(format.format(date));
            }
        });
        //弹出时间选择器
        time_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime.show();
            }
        });
    }

    private ArrayList<ProvinceBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<IPickerViewData>>> options3Items = new ArrayList<>();
    private OptionsPickerView pvOptions;

    private void initOptionsPickerView() {
         /*
        Gradle引用：
        compile 'com.bigkoo:pickerview:2.1.1'
        */
        city_tv1 = (TextView) rootView.findViewById(R.id.city_tv1);
        initData();
        //选项选择器
        pvOptions = new OptionsPickerView(getActivity());
        //三级联动效果
        pvOptions.setPicker(options1Items, options2Items, options3Items, true);
        //设置选择的三级单位
//        pwOptions.setLabels("省", "市", "区");
        pvOptions.setTitle("选择城市");
        pvOptions.setCyclic(false, true, true);
        pvOptions.setCancelable(true);
        //设置默认选中的三级项目
        pvOptions.setSelectOptions(1, 1, 1);
        //监听确定选择按钮
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText()
                        + options2Items.get(options1).get(option2)
                        + options3Items.get(options1).get(option2).get(options3).getPickerViewText();
                city_tv1.setText(tx);
            }
        });
        //点击弹出选项选择器
        city_tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvOptions.show();
            }
        });
    }

    private void initData() {
        //选项1
        options1Items.add(new ProvinceBean(0, "广东", "广东省，以岭南东道、广南东路得名", "其他数据"));
        options1Items.add(new ProvinceBean(1, "湖南", "湖南省地处中国中部、长江中游，因大部分区域处于洞庭湖以南而得名湖南", "芒果TV"));
        options1Items.add(new ProvinceBean(3, "广西", "嗯～～", ""));
        //选项2
        ArrayList<String> options2Items_01 = new ArrayList<>();
        options2Items_01.add("广州");
        options2Items_01.add("佛山");
        options2Items_01.add("东莞");
        options2Items_01.add("阳江");
        options2Items_01.add("珠海");
        ArrayList<String> options2Items_02 = new ArrayList<>();
        options2Items_02.add("长沙");
        options2Items_02.add("岳阳");
        ArrayList<String> options2Items_03 = new ArrayList<>();
        options2Items_03.add("桂林");
        options2Items.add(options2Items_01);
        options2Items.add(options2Items_02);
        options2Items.add(options2Items_03);
        //选项3
        ArrayList<ArrayList<IPickerViewData>> options3Items_01 = new ArrayList<>();
        ArrayList<ArrayList<IPickerViewData>> options3Items_02 = new ArrayList<>();
        ArrayList<ArrayList<IPickerViewData>> options3Items_03 = new ArrayList<>();
        ArrayList<IPickerViewData> options3Items_01_01 = new ArrayList<>();
        options3Items_01_01.add(new PickerViewData("天河"));
        options3Items_01_01.add(new PickerViewData("黄埔"));
        options3Items_01_01.add(new PickerViewData("海珠"));
        options3Items_01_01.add(new PickerViewData("越秀"));
        options3Items_01.add(options3Items_01_01);
        ArrayList<IPickerViewData> options3Items_01_02 = new ArrayList<>();
        options3Items_01_02.add(new PickerViewData("南海"));
        options3Items_01_02.add(new PickerViewData("高明"));
        options3Items_01_02.add(new PickerViewData("禅城"));
        options3Items_01_02.add(new PickerViewData("桂城"));
        options3Items_01.add(options3Items_01_02);
        ArrayList<IPickerViewData> options3Items_01_03 = new ArrayList<>();
        options3Items_01_03.add(new PickerViewData("其他"));
        options3Items_01_03.add(new PickerViewData("常平"));
        options3Items_01_03.add(new PickerViewData("虎门"));
        options3Items_01.add(options3Items_01_03);
        ArrayList<IPickerViewData> options3Items_01_04 = new ArrayList<>();
        options3Items_01_04.add(new PickerViewData("其他"));
        options3Items_01_04.add(new PickerViewData("其他"));
        options3Items_01_04.add(new PickerViewData("其他"));
        options3Items_01.add(options3Items_01_04);
        ArrayList<IPickerViewData> options3Items_01_05 = new ArrayList<>();
        options3Items_01_05.add(new PickerViewData("其他1"));
        options3Items_01_05.add(new PickerViewData("其他2"));
        options3Items_01.add(options3Items_01_05);
        ArrayList<IPickerViewData> options3Items_02_01 = new ArrayList<>();
        options3Items_02_01.add(new PickerViewData("长沙1"));
        options3Items_02_01.add(new PickerViewData("长沙2"));
        options3Items_02_01.add(new PickerViewData("长沙3"));
        options3Items_02_01.add(new PickerViewData("长沙4"));
        options3Items_02_01.add(new PickerViewData("长沙5"));
        options3Items_02.add(options3Items_02_01);
        ArrayList<IPickerViewData> options3Items_02_02 = new ArrayList<>();
        options3Items_02_02.add(new PickerViewData("岳阳"));
        options3Items_02_02.add(new PickerViewData("岳阳1"));
        options3Items_02_02.add(new PickerViewData("岳阳2"));
        options3Items_02_02.add(new PickerViewData("岳阳3"));
        options3Items_02_02.add(new PickerViewData("岳阳4"));
        options3Items_02_02.add(new PickerViewData("岳阳5"));
        options3Items_02.add(options3Items_02_02);
        ArrayList<IPickerViewData> options3Items_03_01 = new ArrayList<>();
        options3Items_03_01.add(new PickerViewData("好山水"));
        options3Items_03.add(options3Items_03_01);
        options3Items.add(options3Items_01);
        options3Items.add(options3Items_02);
        options3Items.add(options3Items_03);
    }

    private void initCityPicker() {
        /*
        Gradle引用：
        compile 'liji.library.dev:citypickerview:0.2.0'
        */
        city_tv2 = (TextView) rootView.findViewById(R.id.city_tv2);
        final CityPickerView cityPickerView = new CityPickerView(getActivity());
        cityPickerView.setOnCityItemClickListener(new CityPickerView.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省份
                String province = citySelected[0];
                //城市
                String city = citySelected[1];
                //区县
                String district = citySelected[2];
                //邮编
                String code = citySelected[3];
                city_tv2.setText(province + "\t" + city + "\t" + district + "\t" + code);
            }
        });
        cityPickerView.setTextColor(R.color.font_color_b);//新增文字颜色修改
        cityPickerView.setTextSize(20);//新增文字大小修改
        cityPickerView.setVisibleItems(5);//新增滚轮内容可见数量
        cityPickerView.setIsCyclic(false);//滚轮是否循环滚动
        city_tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityPickerView.show();
            }
        });
    }

    @Override
    public boolean onBackPressedSupport() {
        if (pvTime.isShowing()) {
            pvTime.dismiss();
            return true;
        }
        if (pvOptions.isShowing()) {
            pvOptions.dismiss();
            return true;
        }
        return false;
    }

}
