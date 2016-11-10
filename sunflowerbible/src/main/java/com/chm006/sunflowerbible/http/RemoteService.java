package com.chm006.sunflowerbible.http;

import com.chm006.sunflowerbible.fragment.test1.main.bean.GirlsBean;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 网络请求url
 * Created by chenmin on 2016/9/27.
 */

public interface RemoteService {

    @GET("api/data/{type}/{count}/{page}")
    Observable<GirlsBean> getGirls(
            @Path("type") String type,
            @Path("count") int count,
            @Path("page") int page
    );

    @GET("/jpcf_zdgj_sgw/sgw/attendance/SignRecords")
    Call<List<Map<String, Object>>> getA(
            @Query("yearMonth") String yearMonth,
            @Query("startPos") int startPos,
            @Query("amount") int amount
    );

    @POST("/jpcf_zdgj_sgw/sgw/zdgj/user/sms/secretKey/{telNo}")
    Call<Void> postA(@Path("telNo") String telNo);

    @POST("/jpcf_zdgj_sgw/sgw/zdgj/user/setPassword")
    Call<Map<String, String>> postB(@Body Map<String, String> params);

    /** 文件上传 */
    @Multipart
    @POST("/jpcf_fs_sgw/sgw/fileUpload")
    Call<List<Map<String, Object>>> fileUpload(
            @PartMap Map<String, RequestBody> params
    );
}
