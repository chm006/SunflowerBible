package com.chm006.sunflowerbible.http;

import com.chm006.library.base.BaseApplication;
import com.chm006.library.utils.StringUtil;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求
 * Created by chenmin on 2016/9/27.
 */

public class RemoteHelper {
    private static Retrofit retrofit;

    public static RemoteService create() {
        return getRetrofit(Consts.ServersUrl.GANK_IO).create(RemoteService.class);
    }

    public static RemoteService create(String serversUrl) {
        return getRetrofit(serversUrl).create(RemoteService.class);
    }

    private static Retrofit getRetrofit(String serverUrl) {
        if (retrofit == null) {
            synchronized (RemoteHelper.class) {
                if (retrofit == null) {
                    if (StringUtil.isNotEmpty(serverUrl)) {
                        retrofit = new Retrofit.Builder()
                                .baseUrl(serverUrl)
                                .addConverterFactory(GsonConverterFactory.create())
                                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                                .client(BaseApplication.defaultOkHttpClient())
                                .build();
                    }
                }
            }
        }
        return retrofit;
    }
}
