#include <jni.h>
#include <string>
#include "libmp3lame/lame.h"

extern "C"
JNIEXPORT jstring

JNICALL
Java_com_dzm_ffmpeg_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

/** lame **/

/**
 * 返回lame version
 */
extern "C"
JNIEXPORT jstring JNICALL
Java_com_dzm_ffmpeg_Mp3Encoder_getVersion(JNIEnv *env, jobject instance) {
    return env->NewStringUTF(get_lame_version());
}

/**
 * Mp3Encoder:将PCM格式转为MP3模式
 */
extern "C"
JNIEXPORT void

JNICALL
Java_com_dzm_ffmpeg_Mp3Encoder_encode(JNIEnv *env, jobject instance) {

    // TODO


}
