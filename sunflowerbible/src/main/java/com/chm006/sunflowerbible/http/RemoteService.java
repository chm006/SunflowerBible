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

    @GET
    Call<List<Map<String, Object>>> firstPay(
            @Query("service") String service,   // Y，接口名称 pay_req_h5_frontpage
            @Query("charset") String charset,   // Y，参数字符编码集 UTF-8、GBK、GB2312、GB18030
            @Query("mer_id") String mer_id,     // Y，商户编号 由平台统一分配合作商户唯一标识
            @Query("sign_type") String sign_type,   // Y，签名方式 暂只支持RSA必须大写
            @Query("sign") String sign,         // Y，签名 参见签名机制
            @Query("notify_url") String notify_url, // Y，服务器异步通知页面路径 交易完成后，联动优势支付平台会按照此地址将交易结果以后台的方式发送到商户网站因前端响应受用户网络波动因素较大，所以需要以后台结果通知接受订单支付结果。
            @Query("version") String version,   // Y，版本号 定值 4.0
            @Query("ret_url") String ret_url,   // Y，页面跳转同步通知页面路径 交易完成后，联动优势支付平台会按照此地址将用户的交易结果页面重定向到商户网站。1、传入正确的URL，在成功页面返回按钮使用。2、如果商户不需要成功页面的返回按钮，此字段传入NoRetUrl，页面则没有返回按钮。
            @Query("res_format") String res_format, // Y，响应数据格式 支持HTML、STRING格式见附录(默认为HTML)
            @Query("order_id") String order_id, // Y，商户唯一订单号 订单号码支持数字、英文字母、-、_、*、+、#，其他字符将不支持，不能小于4位。
            @Query("mer_date") String mer_date, // Y，商户订单日期 商户生成订单的日期，格式YYYYMMDD。订单日期必须为当日。
            @Query("amount") String amount,     // Y，付款金额 人民币，以分为单位
            @Query("amt_type") String amt_type, // Y，付款币种 取值范围：RMB

            @Query("goods_id") String goods_id, // 商品号 支持数字，字母 该参数为唯一一个对账文件返回的商户保留字段
            @Query("goods_inf") String goods_inf,   // 商品描述信息 如果传递中文，需确保该字段在传入接口前，编码正确，建议使用UTF-8编码（根据商户服务器编码环境而定）。
            @Query("card_id") String card_id,   // 卡号 使用联动公钥进行RSA加密后，BASE64(GBK编码)加密该字段
            // 同时传，或同时不传（下面 3 条）
            @Query("identity_type") String identity_type,   // 证件类型 身份证取值：1（证件类型、证件号码需同时传有值或者同时不传）
            @Query("identity_code") String identity_code,   // 证件号 使用联动公钥进行RSA加密后，BASE64(GBK编码)加密该字段（证件类型、证件号码需同时传有值或者同时不传）
            @Query("card_holder") String card_holder,   // 持卡人姓名 使用联动公钥进行RSA加密后，BASE64(GBK编码)加密该字段
            @Query("mer_cust_id") String mer_cust_id,   // 商户用户标识 用户在商户端的唯一标识。首次支付时：如果输入该值，且用户在联动的收银台上同意快捷服务协议，则会注册快捷用户业务协议；如果没有输入，平台不会注册用户业务协议，也不会返回相应的注册签约信息，用户再次进行支付时，则还需要走首次支付。如使用了银联前台页面完成支付，则支付成功后无法在联动优势进行绑卡，同时结果通知中也不会返回支付协议号
            @Query("mer_priv") String mer_priv, // 商户私有域 联动优势支付平台原样返回，用于商户的私有信息。
            @Query("user_ip") String user_ip,   // 用户IP地址 用户在创建交易时，该用户当前所使用机器的 IP。 用作防钓鱼校验
            @Query("expand") String expand,     // 业务扩展信息 主要保存扩展字段的信息,详见附录
            @Query("expire_time") String expire_time,   // 订单过期时长 单位为分钟，默认1440分钟（24小时）
            @Query("risk_expand") String risk_expand,   // 风控扩展信息 格式：代码:内容#代码:内容……如：A0001:二级商户号#A0002:二级商户名称……(此字段要使用UTF-8 urlencode编码)
            @Query("can_modify_flag") String can_modify_flag    // 是否允许用户修改支付要素 如果传入card_id、card_holder和卡号，是否允许用户在联动平台上修改。0：不允许修改
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

    @GET("/jpcf_zdgj_sgw/sgw/attendance/SignRecords")
    Call<List<Map<String, Object>>> getA(
            @Query("yearMonth") String yearMonth,
            @Query("startPos") int startPos,
            @Query("amount") int amount
    );
}
