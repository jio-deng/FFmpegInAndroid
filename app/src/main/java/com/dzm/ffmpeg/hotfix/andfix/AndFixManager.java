package com.dzm.ffmpeg.hotfix.andfix;

import android.content.Context;
import android.util.Log;

import com.alipay.euler.andfix.patch.PatchManager;

import java.io.IOException;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description AndFix组件
 * @date 2019/11/20 11:24
 */
public class AndFixManager {
    private static final String TAG = AndFixManager.class.getSimpleName();

    private PatchManager mPatchManager;

    private AndFixManager() {

    }

    /**
     * init PatchManager
     *
     * @param context context
     */
    public void init(Context context) {
        mPatchManager = new PatchManager(context);
        mPatchManager.init(AndFixUtils.getVersionName());
    }

    /**
     * load patch
     */
    public void loadPatch() {
        if (mPatchManager == null) {
            throw new RuntimeException("AndFixManager::init() must be called first!");
        }

        mPatchManager.loadPatch();
    }

    /**
     * add patch
     *
     * @param path path of patch
     * @throws RuntimeException AndFixManager::init() must be called first
     */
    public void addPatch(String path) throws RuntimeException {
        if (mPatchManager == null) {
            throw new RuntimeException("AndFixManager::init() must be called first!");
        }

        try {
            mPatchManager.addPatch(path);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "addPatch: " + e.getMessage());
        }
    }







    public static AndFixManager getInstance() {
        return AndFixManagerInner.sInstance;
    }

    private static class AndFixManagerInner {
        private static AndFixManager sInstance = new AndFixManager();
    }

}
