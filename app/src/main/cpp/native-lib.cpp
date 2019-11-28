#include <jni.h>
#include <string>
#include "libmp3lame/lame.h"
#include "mp3_encoder.h"

Mp3Encoder *encoder;

extern "C"
JNIEXPORT jstring

JNICALL
Java_com_dzm_ffmpeg_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
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