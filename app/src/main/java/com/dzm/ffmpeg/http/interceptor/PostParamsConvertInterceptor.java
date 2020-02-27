package com.dzm.ffmpeg.http.interceptor;

import androidx.annotation.NonNull;
import androidx.collection.ArrayMap;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * FormBody转为json
 */
public class PostParamsConvertInterceptor implements Interceptor {
    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";
    private Gson mGson = new Gson();

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        // 将post的FormBody转为json
        if (request.method().equals("POST")) {
            RequestBody body = request.body();
            if (body instanceof FormBody) {
                FormBody formBody = (FormBody) body;
                ArrayMap<String, Object> params = new ArrayMap<>();
                for (int i = 0; i < formBody.size(); i ++) {
                    params.put(formBody.name(i), formBody.value(i));
                }
                request = request.newBuilder().post(getRequestBody(params)).build();
            }
        }

        return chain.proceed(request);
    }

    /**
     * 新建RequestBody
     */
    private RequestBody getRequestBody(ArrayMap<String, Object> params) {
        return RequestBody.create(MediaType.parse(CONTENT_TYPE), mGson.toJson(params));
    }
}
