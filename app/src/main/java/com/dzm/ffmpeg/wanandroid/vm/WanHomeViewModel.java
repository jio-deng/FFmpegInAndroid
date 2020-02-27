package com.dzm.ffmpeg.wanandroid.vm;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dzm.ffmpeg.data.WanHomeData;
import com.dzm.ffmpeg.http.RequestUtil;
import com.dzm.ffmpeg.http.base.BaseObserver;
import com.dzm.ffmpeg.http.base.BaseResponse;
import com.dzm.ffmpeg.wanandroid.bean.WanHomeStatus;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description wan android home page vm
 * @date 2020/2/27 16:22
 */
public class WanHomeViewModel extends ViewModel {
    private MutableLiveData<WanHomeData> mWanHomeData;
    private MutableLiveData<WanHomeStatus> mWanHomeStatus;

    public WanHomeViewModel() {
        mWanHomeData = new MutableLiveData<>();
        mWanHomeStatus = new MutableLiveData<>();
    }

    public MutableLiveData<WanHomeData> getWanHomeData() {
        return mWanHomeData;
    }

    public MutableLiveData<WanHomeStatus> getWanHomeStatus() {
        return mWanHomeStatus;
    }

    public void getWanAndroidHomePageData(int page) {
        RequestUtil.getWanAndroidHomePageData(page, new BaseObserver<BaseResponse<WanHomeData>>() {

            @Override
            public void onSuccess(BaseResponse<WanHomeData> baseResponse) {
                mWanHomeData.postValue(baseResponse.data);
            }

            @Override
            public void onError(BaseResponse error) {
                mWanHomeStatus.postValue(new WanHomeStatus(error));
            }
        });
    }
}
