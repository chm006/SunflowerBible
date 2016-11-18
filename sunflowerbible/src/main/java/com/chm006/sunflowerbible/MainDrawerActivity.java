package com.chm006.sunflowerbible;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mobads.AppActivity;
import com.chm006.library.base.activity.BaseDrawerActivity;
import com.chm006.library.utils.SPUtil;
import com.chm006.library.utils.ToastUtil;
import com.chm006.sunflowerbible.fragment.AboutFragment;
import com.chm006.sunflowerbible.fragment.HomeFragment;
import com.chm006.sunflowerbible.fragment.LoginFragment;
import com.chm006.sunflowerbible.fragment.Test1Fragment;
import com.chm006.sunflowerbible.fragment.Test2Fragment;

import java.util.Map;

import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public class MainDrawerActivity extends BaseDrawerActivity {
    // 屏幕宽度
    public float Width;
    // 屏幕高度
    public float Height;
    public static TextView tvName;
    public static TextView tvMail;
    public static ImageView imgNav;
    public static Context context;
    public static Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Map<String, String> data = (Map<String, String>) msg.obj;
                    tvName.setText(data.get("username"));
                    SPUtil.put(context, "username", data.get("username"));
                    break;
            }
            return false;
        }
    });

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main_drawer;
    }

    @Override
    protected SupportFragment newInstance() {
        return HomeFragment.newInstance();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        context = this;
        setFragmentClickable();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        Width = dm.widthPixels;
        Height = dm.heightPixels;
        //状态栏颜色
        //initStatusBar(R.color.toolbar_primary_dark);
        //initStatusBarFull();
        initDrawer();
        initAppStyle();
    }

    private void initAppStyle() {
        //设置app主题样式
        //setTheme(R.style.AppTheme2);
    }

    private void initDrawer() {
        setNavigationDrawerAttr(R.id.activity_main_drawer_dl, R.id.activity_main_drawer_nv);
        //默认选中的item
        navigationView.setCheckedItem(R.id.nav_home);
        //侧边栏：头像、登陆名、邮箱
        LinearLayout llNavHeader = (LinearLayout) navigationView.getHeaderView(0);
        imgNav = (ImageView) llNavHeader.findViewById(R.id.nav_header_main_imageView);
        tvName = (TextView) llNavHeader.findViewById(R.id.nav_header_main_textView_name);
        tvMail = (TextView) llNavHeader.findViewById(R.id.nav_header_main_textView_mail);
        imgNav.setImageResource(R.mipmap.s);
        tvName.setText((CharSequence) SPUtil.get(context, "username", "未登陆"));
        tvMail.setText("");
        imgNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭侧边栏
                onCloseDrawer();
                if ("未登陆".equals(tvName.getText().toString())) {
                    ToastUtil.showShort(MainDrawerActivity.this, "登陆");
                    LoginFragment loginFragment = findFragment(LoginFragment.class);
                    if (loginFragment == null) {
                        popTo(HomeFragment.class, false, new Runnable() {
                            @Override
                            public void run() {
                                start(LoginFragment.newInstance());
                            }
                        });
                    } else {
                        // 如果已经在栈内,则以SingleTask模式start,也可以用popTo
                        start(loginFragment, SupportFragment.SINGLETASK);
                    }
                } else {
                    ToastUtil.showShort(MainDrawerActivity.this, "个人中心");
                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        //关闭侧边栏
        onCloseDrawer();
        drawerLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                int id = item.getItemId();
                final SupportFragment topFragment = getTopFragment();
                switch (id) {
                    case R.id.nav_home:
                        ToastUtil.showShort(MainDrawerActivity.this, "首页");
                        HomeFragment homeFragment = findFragment(HomeFragment.class);
                        Bundle newBundle = new Bundle();
                        newBundle.putString("from", "主页-->来自:" + topFragment.getClass().getSimpleName());
                        homeFragment.putNewBundle(newBundle);
                        start(homeFragment, SupportFragment.SINGLETASK);
                        break;
                    case R.id.nav_test1:
                        ToastUtil.showShort(MainDrawerActivity.this, "测试1");
                        Test1Fragment test1Fragment = findFragment(Test1Fragment.class);
                        if (test1Fragment == null) {
                            popTo(HomeFragment.class, false, new Runnable() {
                                @Override
                                public void run() {
                                    start(Test1Fragment.newInstance());
                                }
                            });
                        } else {
                            // 如果已经在栈内,则以SingleTask模式start,也可以用popTo
                            start(test1Fragment, SupportFragment.SINGLETASK);
                            //popTo(Test1Fragment.class, false);
                        }
                        break;
                    case R.id.nav_test2:
                        ToastUtil.showShort(MainDrawerActivity.this, "测试2");
                        Test2Fragment test2Fragment = findFragment(Test2Fragment.class);
                        if (test2Fragment == null) {
                            popTo(HomeFragment.class, false, new Runnable() {
                                @Override
                                public void run() {
                                    start(Test2Fragment.newInstance());
                                }
                            });
                        } else {
                            // 如果已经在栈内,则以SingleTask模式start,也可以用popTo
                            start(test2Fragment, SupportFragment.SINGLETASK);
                        }
                        break;
                    case R.id.nav_login:
                        ToastUtil.showShort(MainDrawerActivity.this, "登陆");
                        LoginFragment loginFragment = findFragment(LoginFragment.class);
                        if (loginFragment == null) {
                            popTo(HomeFragment.class, false, new Runnable() {
                                @Override
                                public void run() {
                                    start(LoginFragment.newInstance());
                                }
                            });
                        } else {
                            // 如果已经在栈内,则以SingleTask模式start,也可以用popTo
                            start(loginFragment, SupportFragment.SINGLETASK);
                        }
                        break;
                    case R.id.nav_myhome:
                        ToastUtil.showShort(MainDrawerActivity.this, "个人中心");
                        break;
                    case R.id.nav_setting:
                        ToastUtil.showShort(MainDrawerActivity.this, "设置");
                        break;
                    case R.id.nav_share:
                        ToastUtil.showShort(MainDrawerActivity.this, "分享");
                        break;
                    case R.id.nav_about:
                        ToastUtil.showShort(MainDrawerActivity.this, "关于");
                        AboutFragment aboutFragment = findFragment(AboutFragment.class);
                        if (aboutFragment == null) {
                            popTo(HomeFragment.class, false, new Runnable() {
                                @Override
                                public void run() {
                                    start(AboutFragment.newInstance());
                                }
                            });
                        } else {
                            // 如果已经在栈内,则以SingleTask模式start,也可以用popTo
                            start(aboutFragment, SupportFragment.SINGLETASK);
                        }
                        break;
                }
            }
        }, 250);
        return true;
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置默认Fragment动画  默认竖向(和安卓5.0以上的动画相同)
        return super.onCreateFragmentAnimator();
        // 设置横向(和安卓4.x动画相同)
//        return new DefaultHorizontalAnimator();
        // 设置自定义动画
//        return new FragmentAnimator(enter,exit,popEnter,popExit);
    }

}
