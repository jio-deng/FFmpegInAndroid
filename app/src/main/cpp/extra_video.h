//
// Created by A on 2020/7/14.
//

#ifndef FFMPEGINANDROID_EXTRA_VIDEO_H
#define FFMPEGINANDROID_EXTRA_VIDEO_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "libavutil/log.h"
#include "libavformat/avio.h"
#include "libavformat/avformat.h"
#include "libavcodec/avcodec.h"
#include "libswscale/swscale.h"
#include "libavutil/timestamp.h"

#define INBUF_SIZE 4096

#define WORD uint16_t
#define DWORD uint32_t
#define LONG int32_t

#ifndef AV_WB32
#   define AV_WB32(p, val) do {                 \
        uint32_t d = (val);                     \
        ((uint8_t*)(p))[3] = (d);               \
        ((uint8_t*)(p))[2] = (d)>>8;            \
        ((uint8_t*)(p))[1] = (d)>>16;           \
        ((uint8_t*)(p))[0] = (d)>>24;           \
    } while(0)
#endif

#ifndef AV_RB16
#   define AV_RB16(x)                           \
    ((((const uint8_t*)(x))[0] << 8) |          \
      ((const uint8_t*)(x))[1])
#endif

#endif //FFMPEGINANDROID_EXTRA_VIDEO_H

static int alloc_and_copy(AVPacket *out,
                          const uint8_t *sps_pps, uint32_t sps_pps_size,
                          const uint8_t *in, uint32_t in_size);

int h264_extradata_to_annexb(const uint8_t *codec_extradata, const int codec_extradata_size, AVPacket *out_extradata, int padding);

int h264_mp4toannexb(AVFormatContext *fmt_ctx, AVPacket *in, FILE *dst_fd);

int cut_video(double from_seconds, double end_seconds, const char* in_filename, const char* out_filename);

void saveBMP(struct SwsContext *img_convert_ctx, AVFrame *frame, char *filename);

void pgm_save(unsigned char *buf, int wrap, int xsize, int ysize,
              char *filename);

int decode_write_frame(const char *outfilename, AVCodecContext *avctx,
                              struct SwsContext *img_convert_ctx, AVFrame *frame, int *frame_count, AVPacket *pkt, int last);