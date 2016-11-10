package com.chm006.sunflowerbible;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chm006.library.utils.ToastUtil;
import com.chm006.library.widget.ImageCarouselView;
import com.chm006.library.widget.ZoomableDraweeView;
import com.chm006.sunflowerbible.fragment.HomeFragment;
import com.chm006.sunflowerbible.fragment.test1.main.bean.GirlsBean;
import com.chm006.sunflowerbible.http.RemoteHelper;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.chm006.sunflowerbible.R.id.imageView;

/**
 * Created by chenmin on 2016/10/25.
 */

public class SplashActivity extends Activity {
    private Handler handler = new Handler();
    private ImageView imageView;
    private List<String> pics = new ArrayList<>();
    private ImageCarouselView imageCarouselView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageCarouselView = (ImageCarouselView) findViewById(R.id.imageCarouselView);
        //init1();
        init2();
    }

    //停留6秒，进入主页
    private void init1() {
        imageView.setVisibility(View.VISIBLE);
        imageCarouselView.setVisibility(View.GONE);

        RemoteHelper.create().getGirls("福利", 1, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GirlsBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(GirlsBean girlsBean) {
                        for (GirlsBean.ResultsEntity resultsEntity : girlsBean.getResults()) {
                            String url = resultsEntity.getUrl();
                            Glide.with(SplashActivity.this)
                                    .load(url)
                                    .into(imageView);
                        }
                    }
                });
        ToastUtil.showLong(this, "正在检查新版本...");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainDrawerActivity.class);
                startActivity(intent);
                finish();
            }
        }, 6000);
    }

    //滑动图片最后一张，点击按钮进入主页；或者点击右上角“跳过”直接进入主页
    private void init2() {
        imageView.setVisibility(View.GONE);
        imageCarouselView.setVisibility(View.VISIBLE);

        RemoteHelper.create().getGirls("福利", 6, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GirlsBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(GirlsBean girlsBean) {
                        for (GirlsBean.ResultsEntity resultsEntity : girlsBean.getResults()) {
                            String url = resultsEntity.getUrl();
                            pics.add(url);
                        }
                        initImageCarouselView();
                    }
                });
    }

    private void initImageCarouselView() {
        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this, MainDrawerActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //设置 是否自动轮播、轮播图的长度
        imageCarouselView.setIsUserTouchedAndTotalCount(false, pics.size());
        imageCarouselView.setAdapter(new ImageCarouselView.Adapter() {
            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public View getView(int position) {
                View view = SplashActivity.this.getLayoutInflater().inflate(R.layout.item_imageview3, null);
                ImageView imageView = (ImageView) view.findViewById(R.id.item_imageview);
                Button button = (Button) view.findViewById(R.id.item_button);
                Glide.with(SplashActivity.this)
                        .load(pics.get(position))
                        .into(imageView);
                if (pics.size() == position + 1) {
                    button.setVisibility(View.VISIBLE);
                } else {
                    button.setVisibility(View.INVISIBLE);
                }
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SplashActivity.this, MainDrawerActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                return view;
            }

            @Override
            public int getCount() {
                return pics.size();
            }
        });
    }
}
