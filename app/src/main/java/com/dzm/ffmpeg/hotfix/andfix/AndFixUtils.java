package com.dzm.ffmpeg.hotfix.andfix;

import com.dzm.ffmpeg.BuildConfig;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description Utils for AndFix
 * @date 2019/11/20 11:27
 */
public class AndFixUtils {

    private AndFixUtils() {

    }

    public static int getVersionCode() {
        return BuildConfig.VERSION_CODE;
    }

    public static String getVersionName() {
        return BuildConfig.VERSION_NAME;
    }

}
