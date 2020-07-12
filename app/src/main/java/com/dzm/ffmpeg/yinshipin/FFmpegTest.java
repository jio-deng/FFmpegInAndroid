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
     * @return MetaData
     */
    public static native String printMeta(String url);


    /**
     * 抽取音频数据
     *
     * @param in
     * @param out
     */
    public static native String getAudioTrack(String in, String out);
}
