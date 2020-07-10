package com.dzm.ffmpeg.yinshipin;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description test ffmpeg
 * @date 2020/7/10 19:18
 */
public class FFmpegTest {
    /**
     * 打印视频文件的Meta信息
     *
     * @param url 视频地址
     * @return 0：success   -1：failed
     */
    public static native int printMeta(String url);
}
