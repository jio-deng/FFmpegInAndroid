package com.dzm.ffmpeg.http.api;

import com.dzm.ffmpeg.data.WanHomeData;
import com.dzm.ffmpeg.data.WxOfficialAccount;
import com.dzm.ffmpeg.http.base.BaseResponse;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description wan android
 * @date 2020/2/25 10:43
 */
public interface WanApiService {





    /**
     * 获取公众号列表
     */
    @GET(value = "wxarticle/chapters/json")
    Observable<BaseResponse<List<WxOfficialAccount>>> getWxOfficialAccounts();

    /**
     * TODO need to login first 查看某个公众号历史数据
     * @param accountId 公众号 ID：拼接在 url 中，eg:405
     * @param page 公众号页码：拼接在url 中，eg:1
     */
    @GET(value = "list/{accountId}/1/{page}")
    Observable<BaseResponse> getWxOfficialAccountData(@Path("accountId") int accountId, @Path("page") int page);


    /**
     * 首页文章列表
     * @param page 页码，拼接在连接中，从0开始。
     */
    @GET(value = "article/list/{page}/json")
    Observable<Response<BaseResponse<WanHomeData>>> getWanAndroidHomePageData(@Path("page") int page);

    /**
     * 首页文章列表---同步
     * @param page 页码，拼接在连接中，从0开始。
     */
    @GET(value = "article/list/{page}/json")
    Call<BaseResponse<WanHomeData>> getWanAndroidHomePageDataSync(@Path("page") int page);

}
