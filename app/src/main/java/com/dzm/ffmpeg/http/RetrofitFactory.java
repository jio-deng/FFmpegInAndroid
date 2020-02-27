package com.dzm.ffmpeg.http;

import com.dzm.ffmpeg.BuildConfig;
import com.dzm.ffmpeg.http.api.ApiService;
import com.dzm.ffmpeg.http.api.WanApiService;
import com.dzm.ffmpeg.http.interceptor.LogInterceptor;
import com.dzm.ffmpeg.http.interceptor.PostParamsConvertInterceptor;
import com.dzm.ffmpeg.utils.Utils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description retrofit + rxjava util
 * @date 2020/2/25 10:19
 */
public class RetrofitFactory {
    private static final String BASE_URL_SERVER = BuildConfig.BASE_URL_SERVER;  // 服务器
    private static final String BASE_URL_WANANDROID = "https://www.wanandroid.com/";

    /**
     * 初始化时创建ApiService
     */
    private RetrofitFactory() {
        mApiService = createRetrofit(BASE_URL_SERVER).create(ApiService.class);
        mWanApiService = createRetrofit(BASE_URL_WANANDROID).create(WanApiService.class);
    }

    public static RetrofitFactory getInstance() {
        return RetrofitFactoryInner.INSTANCE;
    }

    private static class RetrofitFactoryInner {
        private static RetrofitFactory INSTANCE = new RetrofitFactory();
    }

    private static final int DEFAULT_CONNECT_TIMEOUT = 15;
    private static final int DEFAULT_WRITE_TIMEOUT = 20;
    private static final int DEFAULT_READ_TIMEOUT = 20;

    private ApiService mApiService;
    private WanApiService mWanApiService;

    /**
     * 创建加密的Retrofit
     * @param baseUrl baseUrl
     */
    private Retrofit createRetrofit(String baseUrl) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new PostParamsConvertInterceptor());// post请求时，将FormBody转为json格式
        if (Utils.isAppDebug()) {
            builder.addInterceptor(new LogInterceptor());          // 打印日志 拦截器: 在中间添加LogInterceptor，打印出来的url有公共参数，且返回数据已解密
        }
        builder.retryOnConnectionFailure(true)
                .connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS);

        return new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }

    /**
     * api调用
     * @return ApiService
     */
    public static ApiService getApiService(){
        return getInstance().mApiService;
    }

    public static WanApiService getWanApiService(){
        return getInstance().mWanApiService;
    }

}
