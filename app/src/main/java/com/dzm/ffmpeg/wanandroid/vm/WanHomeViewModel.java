package com.dzm.ffmpeg.wanandroid.vm;

import androidx.lifecycle.ViewModel;

import com.dzm.ffmpeg.wanandroid.WanAndroidRepository;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description wan android home page vm
 * @date 2020/2/27 16:22
 */
public class WanHomeViewModel extends ViewModel {
    private WanAndroidRepository mRepository;

    public WanHomeViewModel() {
        mRepository = new WanAndroidRepository();
    }

    public WanAndroidRepository getRepository() {
        return mRepository;
    }
}
