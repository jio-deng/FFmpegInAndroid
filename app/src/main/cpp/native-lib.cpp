#include <jni.h>
#include <string>
#include <unistd.h>

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

#include "com_dzm_ffmpeg_yinshipin_activity_FModActivity.h"
#include "fmod/fmod.hpp"

#define ADTS_HEADER_LEN  7;

#define MODE_ORIGN 0
#define MODE_LOLI 1
#define MODE_MAN 2
#define MODE_GOAST 3

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

/**
 * TODO 视频转图片 update libffmpeg.so
 */
//extern "C"
//JNIEXPORT jint JNICALL
//Java_com_dzm_ffmpeg_yinshipin_FFmpegTest_convertVideo2Bitmap(JNIEnv *env, jclass clazz, jstring in,
//                                                             jstring out) {
//    int ret;
//    FILE *f;
//    const char *filename, *outfilename;
//    AVFormatContext *fmt_ctx = NULL;
//
//    const AVCodec *codec;
//    AVCodecContext *c= NULL;
//
//    AVStream *st = NULL;
//    int stream_index;
//
//    int frame_count;
//    AVFrame *frame;
//
//    struct SwsContext *img_convert_ctx;
//
//    //uint8_t inbuf[INBUF_SIZE + AV_INPUT_BUFFER_PADDING_SIZE];
//    AVPacket avpkt;
//
//    filename    = env->GetStringUTFChars(in,  0);
//    outfilename = env->GetStringUTFChars(out, 0);
//
//    /* register all formats and codecs */
//    av_register_all();
//
//    /* open input file, and allocate format context */
//    if (avformat_open_input(&fmt_ctx, filename, NULL, NULL) < 0) {
//        fprintf(stderr, "Could not open source file %s\n", filename);
//        exit(1);
//    }
//
//    /* retrieve stream information */
//    if (avformat_find_stream_info(fmt_ctx, NULL) < 0) {
//        fprintf(stderr, "Could not find stream information\n");
//        exit(1);
//    }
//
//    /* dump input information to stderr */
//    av_dump_format(fmt_ctx, 0, filename, 0);
//
//    av_init_packet(&avpkt);
//
//    /* set end of buffer to 0 (this ensures that no overreading happens for damaged MPEG streams) */
//    //memset(inbuf + INBUF_SIZE, 0, AV_INPUT_BUFFER_PADDING_SIZE);
//    //
//
//    ret = av_find_best_stream(fmt_ctx, AVMEDIA_TYPE_VIDEO, -1, -1, NULL, 0);
//    if (ret < 0) {
//        fprintf(stderr, "Could not find %s stream in input file '%s'\n",
//                av_get_media_type_string(AVMEDIA_TYPE_VIDEO), filename);
//        return ret;
//    }
//
//    stream_index = ret;
//    st = fmt_ctx->streams[stream_index];
//
//    /* find decoder for the stream */
//    codec = avcodec_find_decoder(st->codecpar->codec_id);
//    if (!codec) {
//        fprintf(stderr, "Failed to find %s codec\n",
//                av_get_media_type_string(AVMEDIA_TYPE_VIDEO));
//        return AVERROR(EINVAL);
//    }
//
//
//    /* find the MPEG-1 video decoder */
//    /*
//    codec = avcodec_find_decoder(AV_CODEC_ID_MPEG1VIDEO);
//    if (!codec) {
//        fprintf(stderr, "Codec not found\n");
//        exit(1);
//    }
//    */
//
//    c = avcodec_alloc_context3(NULL);
//    if (!c) {
//        fprintf(stderr, "Could not allocate video codec context\n");
//        exit(1);
//    }
//
//    /* Copy codec parameters from input stream to output codec context */
//    if ((ret = avcodec_parameters_to_context(c, st->codecpar)) < 0) {
//        fprintf(stderr, "Failed to copy %s codec parameters to decoder context\n",
//                av_get_media_type_string(AVMEDIA_TYPE_VIDEO));
//        return ret;
//    }
//
//
//    /*
//    if (codec->capabilities & AV_CODEC_CAP_TRUNCATED)
//        c->flags |= AV_CODEC_FLAG_TRUNCATED; // we do not send complete frames
//    */
//
//    /* For some codecs, such as msmpeg4 and mpeg4, width and height
//       MUST be initialized there because this information is not
//       available in the bitstream. */
//
//    /* open it */
//    if (avcodec_open2(c, codec, NULL) < 0) {
//        fprintf(stderr, "Could not open codec\n");
//        exit(1);
//    }
//
//    /*
//    f = fopen(filename, "rb");
//    if (!f) {
//        fprintf(stderr, "Could not open %s\n", filename);
//        exit(1);
//    }
//    */
//
//    img_convert_ctx = sws_getContext(c->width, c->height,
//                                     c->pix_fmt,
//                                     c->width, c->height,
//                                     AV_PIX_FMT_RGB24,
//                                     SWS_BICUBIC, NULL, NULL, NULL);
//
//    if (img_convert_ctx == NULL)
//    {
//        fprintf(stderr, "Cannot initialize the conversion context\n");
//        exit(1);
//    }
//
//    frame = av_frame_alloc();
//    if (!frame) {
//        fprintf(stderr, "Could not allocate video frame\n");
//        exit(1);
//    }
//
//    frame_count = 0;
//    while (av_read_frame(fmt_ctx, &avpkt) >= 0) {
//        /*
//        avpkt.size = fread(inbuf, 1, INBUF_SIZE, f);
//        if (avpkt.size == 0)
//            break;
//        */
//
//        /* NOTE1: some codecs are stream based (mpegvideo, mpegaudio)
//           and this is the only method to use them because you cannot
//           know the compressed data size before analysing it.
//
//           BUT some other codecs (msmpeg4, mpeg4) are inherently frame
//           based, so you must call them with all the data for one
//           frame exactly. You must also initialize 'width' and
//           'height' before initializing them. */
//
//        /* NOTE2: some codecs allow the raw parameters (frame size,
//           sample rate) to be changed at any frame. We handle this, so
//           you should also take care of it */
//
//        /* here, we use a stream based decoder (mpeg1video), so we
//           feed decoder and see if it could decode a frame */
//        //avpkt.data = inbuf;
//        //while (avpkt.size > 0)
//        if(avpkt.stream_index == stream_index){
//            if (decode_write_frame(outfilename, c, img_convert_ctx, frame, &frame_count, &avpkt, 0) < 0)
//                exit(1);
//        }
//
//        av_packet_unref(&avpkt);
//    }
//
//    /* Some codecs, such as MPEG, transmit the I- and P-frame with a
//       latency of one frame. You must do the following to have a
//       chance to get the last frame of the video. */
//    avpkt.data = NULL;
//    avpkt.size = 0;
//    decode_write_frame(outfilename, c, img_convert_ctx, frame, &frame_count, &avpkt, 1);
//
//    fclose(f);
//
//    avformat_close_input(&fmt_ctx);
//
//    sws_freeContext(img_convert_ctx);
//    avcodec_free_context(&c);
//    av_frame_free(&frame);
//
//    return 0;
//}

/**
 * 声音转换
 * 采集设备 ==》 采集声音的原始数据 ==》 mp3 aac （音频编码 ：采样率 比特率） 数据化模拟处理
 * dsp单元（模拟处理器的单元）
 */
using namespace FMOD;

extern "C"
JNICALL
JNIEXPORT void JNICALL
Java_com_dzm_ffmpeg_yinshipin_activity_FModActivity_fix(JNIEnv *env, jclass clazz, jstring path,
                                                        jint mod) {
    const char* path1 = env->GetStringUTFChars(path, nullptr);

    System       *system        = nullptr;
    Sound        *sound         = nullptr;
    Channel      *channel       = nullptr;
    DSP          *dsplowpass    = nullptr;
    bool isPlaying = true;

    /*
        Create a System object and initialize
    */
    System_Create(&system);
    LOGI(LOG_TAG, "System created");
    // 初始化system
    system->init(32, FMOD_INIT_NORMAL, nullptr);
    LOGI(LOG_TAG, "System init");
    // 将声音文件加载到sound中
    system->createSound(path1, FMOD_DEFAULT, nullptr, &sound);
    LOGI(LOG_TAG, "sound created");

    try {
        switch (mod) {
            case MODE_ORIGN:
                system->playSound(sound, nullptr, false, &channel);
                LOGI(LOG_TAG, "sound play");
                break;
            case MODE_LOLI:
                system->createDSPByType(FMOD_DSP_TYPE_PITCHSHIFT, &dsplowpass);
                // 2.0代表音调调高一个8度
                dsplowpass->setParameterFloat(FMOD_DSP_PITCHSHIFT_PITCH, 2.5);
                system->playSound(sound, nullptr, false, &channel);
                channel->addDSP(0, dsplowpass);
                LOGI(LOG_TAG, "sound play");
                break;
            case MODE_MAN:
                system->createDSPByType(FMOD_DSP_TYPE_PITCHSHIFT, &dsplowpass);
                // 2.0代表音调调高一个8度
                dsplowpass->setParameterFloat(FMOD_DSP_PITCHSHIFT_PITCH, 0.8);
                system->playSound(sound, nullptr, false, &channel);
                channel->addDSP(0, dsplowpass);
                LOGI(LOG_TAG, "sound play");
                break;
            case MODE_GOAST:
                system->createDSPByType(FMOD_DSP_TYPE_TREMOLO,&dsplowpass);
                dsplowpass->setParameterFloat(FMOD_DSP_TREMOLO_SKEW, 0.5);
                system->playSound(sound, nullptr, false, &channel);
                channel->addDSP(0, dsplowpass);
                LOGI(LOG_TAG, "sound play");
                break;
        }
    } catch (...) {
        LOGE("%s","发生异常");
        goto end;
    }

    system->update();
    LOGI(LOG_TAG, "System update");

    while (isPlaying) {
        LOGI(LOG_TAG, isPlaying ? "1" : "0");
        channel->isPlaying(&isPlaying);
        LOGI(LOG_TAG, isPlaying ? "1" : "0");
        usleep(1000);
    }

end:
    if (dsplowpass) {
        dsplowpass->release();
    }
    sound->release();
    system->close();
    system->release();
    LOGI(LOG_TAG, "release");

    env->ReleaseStringUTFChars(path, path1);
}