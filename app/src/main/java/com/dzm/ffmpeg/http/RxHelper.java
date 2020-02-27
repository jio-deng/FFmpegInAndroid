package com.dzm.ffmpeg.http;

import com.dzm.ffmpeg.http.base.BaseResponse;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * 解析Response中的code
 */
public class RxHelper {
    private static final int CODE_OK = 0;

    private RxHelper() {}

    public static <T> ObservableTransformer<Response<BaseResponse<T>>, BaseResponse<T>> toBaseResponse() {
        return new ObservableTransformer<Response<BaseResponse<T>>, BaseResponse<T>>() {
            @Override
            public ObservableSource<BaseResponse<T>> apply(Observable<Response<BaseResponse<T>>> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .map(new Function<Response<BaseResponse<T>>, BaseResponse<T>>() {
                            @Override
                            public BaseResponse<T> apply(Response<BaseResponse<T>> response) throws Exception {
                                return parseResponse(response);
                            }
                        });
            }
        };
    }

    /**
     * 解析Response code，返回BaseResponse
     */
    public static <T> BaseResponse<T> parseResponse(Response<BaseResponse<T>> response) {
        BaseResponse<T> result;
        if (response.code() < 200 || response.code() >= 300) {
            // TODO:解析response code
            result = new BaseResponse<>();
            result.errorCode = response.code();
            result.errorMsg = response.message();
        } else {
            result = response.body();
            if (result != null) {
                int code = result.errorCode;
                if (code == CODE_OK) {
                    // TODO: 解密data字段下的数据
                } else {
                    // collect wrong message
                }
            }
        }
        return result;
    }

}
