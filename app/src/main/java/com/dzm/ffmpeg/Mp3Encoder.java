package com.dzm.ffmpeg;

/**
 * @Description Convert PCM to MP3
 * mac环境ndk编译lame:https://blog.csdn.net/woai110120130/article/details/80668835
 *
 * Created by deng on 2019/7/14.
 */
public class Mp3Encoder {


    public static native String getVersion();
    public static native void encode();


}

