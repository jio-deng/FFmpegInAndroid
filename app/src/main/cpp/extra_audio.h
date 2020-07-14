//
// Created by A on 2020/7/14.
//

#ifndef FFMPEGINANDROID_EXTRA_AUDIO_H
#define FFMPEGINANDROID_EXTRA_AUDIO_H

#include <stdio.h>
#include "libavutil/log.h"
#include "libavformat/avio.h"
#include "libavformat/avformat.h"

#define ADTS_HEADER_LEN  7;

#endif //FFMPEGINANDROID_EXTRA_AUDIO_H

void adts_header(char *szAdtsHeader, int dataLen);
