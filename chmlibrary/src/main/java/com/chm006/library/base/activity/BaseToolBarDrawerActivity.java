package com.chm006.library.base.activity;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.chm006.library.R;

/**
 * 带侧边栏（抽屉）和toolbar的activity
 * Created by chenmin on 2016/8/23.
 */
public abstract class BaseToolBarDrawerActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener, NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;

    /**
     * 获取 toolbar 的 menu ID
     *
     * @return
     */
    public abstract int getMenu();

    // 为了让 Toolbar 的 Menu 有作用，这个是必须的
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(getMenu(), menu);
        return true;
    }

    /**
     * 设置 toolbar 的属性（如果不设置，int值传递0，string值传递null或""）
     *
     * @param toolbar        toolbar对象
     * @param logoId         logo图标
     * @param title          标题
     * @param subTitle       副标题
     * @param navigationIcon 导航栏图标（左上角）
     * @param isBackIcon     点击是否后退（如果导航栏图标为空，添加一个返回图标（左上角））
     */
    public void setToolbarAttr(Toolbar toolbar, int logoId, String title, String subTitle, int navigationIcon, boolean isBackIcon) {
        assert toolbar != null;
        // App Logo
        if (logoId != 0)
            toolbar.setLogo(logoId);
        // Title
        if (!TextUtils.isEmpty(title))
            toolbar.setTitle(title);
       /* toolbar.setTitleTextColor(getResources().getColor(R.color.toolbar_background));
        toolbar.setBackgroundColor(getResources().getColor(R.color.toolbar_background));*/
        // Sub Title
        if (!TextUtils.isEmpty(subTitle))
            toolbar.setSubtitle(subTitle);

        setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(this);

        //左上角返回按钮图标
        if (isBackIcon) {
            // 给左上角logo的左边加上一个返回的图标，对应ActionBar.DISPLAY_HOME_AS_UP
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // 使左上角图标可点击，对应id为android.R.id.home，对应ActionBar.DISPLAY_SHOW_HOME
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            /*
            // 使自定义的普通View能在title栏显示，即actionBar.setCustomView能起作用，对应ActionBar.DISPLAY_SHOW_CUSTOM
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            // 使自定义的普通View可点击，对应ActionBar.DISPLAY_SHOW_TITLE
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            */
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
        //左上角导航栏图标
        if (navigationIcon != 0)
            toolbar.setNavigationIcon(navigationIcon);

    }

    /**
     * 设置 NavigationDrawer 的属性（如果不设置，int值传递0，string值传递null或""）
     *
     * @param drawerLayout     DrawerLayout(带侧边栏activity的布局)
     * @param toolbar          Toolbar对象
     * @param navigationViewId NavigationView 的 ID
     */
    public void setNavigationDrawerAttr(DrawerLayout drawerLayout, Toolbar toolbar, int navigationViewId) {
        this.drawerLayout = drawerLayout;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(navigationViewId);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);
    }

    //回退键
    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 菜单点击事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (false/*id == R.id.action_edit || id == R.id.action_share || id == R.id.action_settings*/) {
            //返回 true 表示处理完这次点击事件，不需要再往下（父类）传递下去
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
