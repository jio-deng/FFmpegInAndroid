package com.dzm.ffmpeg;

/**
 * @Description Convert PCM to MP3
 *
 * Created by deng on 2019/7/14.
 */
public class Mp3Encoder {

    /**
     * 获取lame version
     */
    public static native String getVersion();

    /**
     * init
     *
     * @param pcmFilePath pcm file path
     * @param audioChannels 声道数
     * @param bitRate 比特率
     * @param sampleRate 采样率
     * @param mp3Path mp3 file path
     * @return success:0 failed:-1
     */
    public static native int init(String pcmFilePath, int audioChannels, int bitRate, int sampleRate, String mp3Path);

    /**
     * encode
     */
    public static native void encode();

    /**
     * destroy
     */
    public static native void destroy();
}

