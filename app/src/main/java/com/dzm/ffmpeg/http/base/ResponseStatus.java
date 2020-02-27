package com.dzm.ffmpeg.http.base;

public class ResponseStatus {
    public int code;
    public String msg;

    public ResponseStatus() {
    }

    public ResponseStatus(BaseResponse baseResponse) {
        update(baseResponse);
    }

    public ResponseStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public void update(BaseResponse baseResponse) {
        this.code = baseResponse.errorCode;
        this.msg = baseResponse.errorMsg;
    }

//    public boolean isSuccess() {
//        return this.code == ApiCode.CODE_SUCCESS;
//    }
//
//    public boolean isTokenOverTime() {
//        return this.code == ApiCode.ERROR_TOKEN_OVER_TIME;
//    }

//    public String getErrorMessage() {
//        String ret = msg;
//        switch (code) {
//            case ApiCode.ERROR_NETWORK_NOT_CONNECTED:
//                ret = Utils.getString(R.string.network_error);
//                break;
//            case ApiCode.ERROR_SERVER_OFFLINE:
//                ret = Utils.getString(R.string.server_error);
//                break;
//            default:
//                break;
//        }
//        return ret;
//    }
}
