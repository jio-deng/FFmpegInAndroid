#include <jni.h>
#include <string>

#include "mp3_encoder.h"
#include "ffmpeg_jni_define.h"

extern "C"
{
#include "libavutil/log.h"
#include "libavformat/avformat.h"
#include "libavformat/avio.h"
#include "extra_audio.h"
#include "extra_video.h"
}

#define ADTS_HEADER_LEN  7;

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




/**
 * 获取meta-data
 */
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
        LOGI(LOG_TAG, "Can't open file : %s\n", av_err2str(ret));
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


/**
 * 获取音频数据
 */
extern "C"
JNIEXPORT jstring JNICALL
Java_com_dzm_ffmpeg_yinshipin_FFmpegTest_getAudioTrack(JNIEnv *env, jclass clazz,
                                                        jstring in,jstring out) {
    AVFormatContext *fmt_ctx = NULL;
    AVPacket pkt;

    int ret, len;
    int audio_index;
    char* res = (char*)malloc(1);

    const char* constUrl = env->GetStringUTFChars(in, 0);
    const char* destUtl = env->GetStringUTFChars(out, 0);

    av_log_set_level(AV_LOG_INFO);
    av_register_all();

    FILE* dest_fd = fopen(destUtl, "wb");
    if (!dest_fd) {
        LOGE(LOG_TAG, "File already exists : %s\n", destUtl);
        strcat(res, "File already exists : ");
        strcat(res, destUtl);
        strcat(res, "\n");
        return env->NewStringUTF(res);
    }

    // 传入上下文、文件名、后缀（不填则解析文件名后面的）、options
    ret = avformat_open_input(&fmt_ctx, constUrl, NULL, NULL);
    if (ret < 0) {
        LOGE(LOG_TAG, "Can't open file : %s\n", av_err2str(ret));
        avformat_close_input(&fmt_ctx);
        strcat(res, "Can't open file : ");
        strcat(res, av_err2str(ret));
        strcat(res, "\n");
        return env->NewStringUTF(res);
    }

    av_dump_format(fmt_ctx, 0, constUrl, 0); // 最后这个0代表dump输入信息；1代表dump输出信息

    // get stream
    audio_index = av_find_best_stream(fmt_ctx, AVMEDIA_TYPE_AUDIO, -1, -1, NULL, 0);
    if (audio_index < 0) {
        LOGE(LOG_TAG, "Can't find best stream!");
        avformat_close_input(&fmt_ctx);
        fclose(dest_fd);
        strcat(res, "Can't find best stream!\n");
        return env->NewStringUTF(res);
    }

    // read
    av_init_packet(&pkt);
    while (av_read_frame(fmt_ctx, &pkt) >= 0) {
        if (pkt.stream_index == audio_index) {
            // write adts header
            char adts_header_buf[7];
            adts_header(adts_header_buf, pkt.size);
            fwrite(adts_header_buf, 1, 7, dest_fd);

            // write audio data to 'out'
            len = fwrite(pkt.data, 1, pkt.size, dest_fd);
            if (len != pkt.size) {
                av_log(NULL, AV_LOG_WARNING, "Warning, length of data is not equal to size of pkt!");
            }
        }

        av_packet_unref(&pkt);
    }

    fclose(dest_fd);
    avformat_close_input(&fmt_ctx);

    strcat(res, "Succeed!");
    return env->NewStringUTF(res);
}



/**
 * 抽取视频数据
 */
extern "C"
JNIEXPORT jstring JNICALL
Java_com_dzm_ffmpeg_yinshipin_FFmpegTest_getVideoTrack(JNIEnv *env, jclass clazz, jstring in,
                                                       jstring out) {
    AVFormatContext *fmt_ctx = NULL;
    AVPacket pkt;

    int ret, len;
    int video_index;
    char* res = (char*)malloc(1);

    const char* constUrl = env->GetStringUTFChars(in, 0);
    const char* destUtl = env->GetStringUTFChars(out, 0);

    av_log_set_level(AV_LOG_INFO);
    av_register_all();

    FILE* dest_fd = fopen(destUtl, "wb");
    if (!dest_fd) {
        LOGE(LOG_TAG, "File already exists : %s\n", destUtl);
        strcat(res, "File already exists : ");
        strcat(res, destUtl);
        strcat(res, "\n");
        return env->NewStringUTF(res);
    }

    // 传入上下文、文件名、后缀（不填则解析文件名后面的）、options
    ret = avformat_open_input(&fmt_ctx, constUrl, NULL, NULL);
    if (ret < 0) {
        LOGE(LOG_TAG, "Can't open file : %s\n", av_err2str(ret));
        avformat_close_input(&fmt_ctx);
        strcat(res, "Can't open file : ");
        strcat(res, av_err2str(ret));
        strcat(res, "\n");
        return env->NewStringUTF(res);
    }

    av_dump_format(fmt_ctx, 0, constUrl, 0); // 最后这个0代表dump输入信息；1代表dump输出信息

    // get stream
    video_index = av_find_best_stream(fmt_ctx, AVMEDIA_TYPE_VIDEO, -1, -1, NULL, 0);
    if (video_index < 0) {
        LOGE(LOG_TAG, "Can't find best stream!");
        avformat_close_input(&fmt_ctx);
        fclose(dest_fd);
        strcat(res, "Can't find best stream!\n");
        return env->NewStringUTF(res);
    }

    // read
    av_init_packet(&pkt);
    while (av_read_frame(fmt_ctx, &pkt) >= 0) {
        if (pkt.stream_index == video_index) {
            h264_mp4toannexb(fmt_ctx, &pkt, dest_fd);
        }

        av_packet_unref(&pkt);
    }

    fclose(dest_fd);
    avformat_close_input(&fmt_ctx);

    strcat(res, "Succeed!");
    return env->NewStringUTF(res);
}



/**
 * 裁剪视频
 */
extern "C"
JNIEXPORT jint JNICALL
Java_com_dzm_ffmpeg_yinshipin_FFmpegTest_cutVideo(JNIEnv *env, jclass clazz, jdouble from_second,
                                                  jdouble to_second, jstring in, jstring out) {
    int ret = cut_video(from_second, to_second, env->GetStringUTFChars(in, 0),
                        env->GetStringUTFChars(out, 0));
    return (jint) ret;
}