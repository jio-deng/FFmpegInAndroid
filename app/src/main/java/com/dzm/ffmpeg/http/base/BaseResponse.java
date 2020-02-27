package com.dzm.ffmpeg.http.base;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description BaseResponse
 * @date 2020/2/25 11:06
 */
public class BaseResponse<T> {
    public int errorCode;
    public String errorMsg;
    public T data;

    /**
     * 发送exception使用的BaseResponse
     */
    public static BaseResponse build(String errorMsg, int code) {
        BaseResponse error = new BaseResponse();
        error.errorCode = code;
        error.errorMsg = errorMsg;
        return error;
    }

    public static BaseResponse build(String errorMsg) {
        BaseResponse error = new BaseResponse();
        error.errorCode = -1;
        error.errorMsg = errorMsg;
        return error;
    }
}
