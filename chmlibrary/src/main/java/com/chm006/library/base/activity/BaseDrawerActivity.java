package com.chm006.library.base.activity;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.chm006.library.R;
import com.chm006.library.base.fragment.BaseMainFragment;

/**
 * 带侧边栏（抽屉）的activity
 * Created by chenmin on 2016/9/10.
 */
public abstract class BaseDrawerActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, BaseMainFragment.OnFragmentOpenDrawerListener {
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;

    /**
     * 布局文件ID
     * 布局中必须加入：
     * <include layout="@layout/activity_base"/>
     */
    protected abstract int getContentViewId();

    /**
     * 布局中Fragment的ID（填充fragment）
     */
    protected int getFrameLayoutId() {
        return R.id.activity_base_fl;
    }

    /**
     * 设置 NavigationDrawer 的属性（如果不设置，int值传递0，string值传递null或""）
     *
     * @param drawerLayoutId   DrawerLayout 的 ID(带侧边栏activity的布局)
     * @param navigationViewId NavigationView 的 ID
     */
    public void setNavigationDrawerAttr(int drawerLayoutId, int navigationViewId) {
        drawerLayout = (DrawerLayout) findViewById(drawerLayoutId);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(navigationViewId);
        navigationView.setNavigationItemSelectedListener(this);
        //默认选中的item
        //navigationView.setCheckedItem(R.id.nav_home);
        //侧边栏：头像、登陆名、邮箱
        /*LinearLayout llNavHeader = (LinearLayout) navigationView.getHeaderView(0);
        ImageView imgNav = (ImageView) llNavHeader.findViewById(R.id.nav_header_main_imageView);
        TextView tvName = (TextView) llNavHeader.findViewById(R.id.nav_header_main_textView_name);
        TextView tvMail = (TextView) llNavHeader.findViewById(R.id.nav_header_main_textView_mail);*/
    }

    /**
     * 设置 NavigationDrawer 的属性（如果不设置，int值传递0，string值传递null或""）
     *
     * @param drawerLayoutId   DrawerLayout 的 ID(带侧边栏activity的布局)
     * @param toolbar          Toolbar对象
     * @param navigationViewId NavigationView 的 ID
     */
    public void setNavigationDrawerAttr(int drawerLayoutId, Toolbar toolbar, int navigationViewId) {
        drawerLayout = (DrawerLayout) findViewById(drawerLayoutId);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(navigationViewId);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * 打开抽屉
     */
    @Override
    public void onOpenDrawer() {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    /**
     * 关闭抽屉
     */
    public void onCloseDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    //回退键
    @Override
    public void onBackPressedSupport() {
        if (drawerLayout != null) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                    pop();
                } else {
                    if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
                        finish();
                    } else {
                        TOUCH_TIME = System.currentTimeMillis();
                        Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}
