#include <jni.h>
#include <string>

#include "mp3_encoder.h"

extern "C"
{
#include "libavutil/log.h"
#include "libavformat/avformat.h"
}


Mp3Encoder *encoder;

extern "C"
JNIEXPORT jstring JNICALL
Java_com_dzm_ffmpeg_MainActivity_stringFromJNI(JNIEnv *env, jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

/** ---------------------------------------- lame start ---------------------------------------- **/

/**
 * 返回lame version
 */
extern "C"
JNIEXPORT jstring JNICALL
Java_com_dzm_ffmpeg_Mp3Encoder_getVersion(JNIEnv *env, jclass instance) {
    return env->NewStringUTF(get_lame_version());
}


/**
 * init:实例化Mp3Encoder类，调用初始化方法
 */
extern "C"
JNIEXPORT jint JNICALL
Java_com_dzm_ffmpeg_Mp3Encoder_init(JNIEnv *env, jclass type, jstring pcmFilePath_,
                                    jint audioChannels, jint bitRate, jint sampleRate,
                                    jstring mp3Path_) {
    const char *pcmFilePath = env->GetStringUTFChars(pcmFilePath_, 0);
    const char *mp3Path = env->GetStringUTFChars(mp3Path_, 0);

    encoder = new Mp3Encoder();
    int result = encoder->Init(pcmFilePath, mp3Path, sampleRate, audioChannels, bitRate);

    env->ReleaseStringUTFChars(pcmFilePath_, pcmFilePath);
    env->ReleaseStringUTFChars(mp3Path_, mp3Path);

    return result;
}


/**
 * Mp3Encoder:将PCM格式转为MP3模式
 */
extern "C"
JNIEXPORT void JNICALL
Java_com_dzm_ffmpeg_Mp3Encoder_encode(JNIEnv *env, jclass instance) {
    encoder->Encode();
}

/**
 * destroy
 */
extern "C"
JNIEXPORT void JNICALL
Java_com_dzm_ffmpeg_Mp3Encoder_destroy(JNIEnv *env, jclass type) {
    encoder->Destroy();
}



/** ----------------------------------------- lame end ----------------------------------------- **/





extern "C"
JNIEXPORT jstring JNICALL
Java_com_dzm_ffmpeg_yinshipin_FFmpegTest_printMeta(JNIEnv *env, jclass clazz, jstring url) {
    AVFormatContext *fmt_ctx = NULL;
    AVDictionaryEntry *tag = NULL;
    int ret;
    const char* constUrl = env->GetStringUTFChars(url, 0);

    av_log_set_level(AV_LOG_INFO);
    av_register_all();

    // 传入上下文、文件名、后缀（不填则解析文件名后面的）、options
    ret = avformat_open_input(&fmt_ctx, constUrl, NULL, NULL);
    char* res = (char*)malloc(1);
    if (ret < 0) {
        av_log(NULL, AV_LOG_ERROR, "Can't open file : %s\n", av_err2str(ret));
        strcat(res, "Can't open file : %s\n");
        strcat(res, av_err2str(ret));
        return env->NewStringUTF(res);
    }

    av_dump_format(fmt_ctx, 0, constUrl, 0); // 最后这个0代表dump输入信息；1代表dump输出信息

    while ((tag = av_dict_get(fmt_ctx->metadata, "", tag, AV_DICT_IGNORE_SUFFIX))) {
        strcat(res, tag->key);
        strcat(res, "=");
        strcat(res, tag->value);
        strcat(res, "\n");
    }

    avformat_close_input(&fmt_ctx);

    return env->NewStringUTF(res);
}