package com.dzm.ffmpeg.http.api;

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
import retrofit2.http.Query;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description api声明
 * @date 2020/2/25 10:25
 */
public interface ApiService {
//    /**
//     * 获取主题列表
//     */
//    @GET(value = "tag/list/v1")
//    Observable<Response<BaseResponse<List<Tag>>>> getTagList();
//
//    /**
//     * 关注/取消关注主题
//     *
//     * @param tagId  主题id
//     * @param status 状态
//     */
//    @POST(value = "tag/follow/v1")
//    @FormUrlEncoded
//    Observable<Response<BaseResponse<Object>>> followTag(@Field("tid") long tagId,
//                                                         @Field("follow_status") int status,
//                                                         @Field("source") int source);
}
