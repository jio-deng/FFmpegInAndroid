package com.dzm.ffmpeg.http;

import com.dzm.ffmpeg.data.WanHomeData;
import com.dzm.ffmpeg.http.base.BaseObserver;
import com.dzm.ffmpeg.http.base.BaseResponse;
import com.dzm.ffmpeg.utils.LogUtils;

import java.io.IOException;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description request util
 * @date 2020/2/27 16:09
 */
public class RequestUtil {

    /**
     * 首页文章列表
     * @param page 页码，拼接在连接中，从0开始。
     */
    public static void getWanAndroidHomePageData(int page, BaseObserver<BaseResponse<WanHomeData>> observer) {
        RetrofitFactory.getWanApiService()
                .getWanAndroidHomePageData(page)
                .compose(RxHelper.toBaseResponse())
                .subscribe(observer);
    }

    /**
     * 首页文章列表---同步
     * @param page 页码，拼接在连接中，从0开始。
     */
    public static BaseResponse<WanHomeData> getWanAndroidHomePageDataSync(int page) {
        BaseResponse<WanHomeData> baseResponse;
        try {
            baseResponse = RxHelper.parseResponse(RetrofitFactory.getWanApiService()
                    .getWanAndroidHomePageDataSync(page).execute());
        } catch (IOException e) {
            LogUtils.e(e);
            baseResponse = BaseResponse.build(e.getMessage());
        }
        return baseResponse;
    }
}
