//
// Created by Deng on 2019/7/14.
//

#include <stdio.h>
#include "libmp3lame/lame.h"

#ifndef FFMPEGINANDROID_MP3_ENCODER_H
#define FFMPEGINANDROID_MP3_ENCODER_H

#endif //FFMPEGINANDROID_MP3_ENCODER_H

class Mp3Encoder {
private:
    FILE* pcmFile;
    FILE* mp3File;
    lame_t lameClient;

public:
    Mp3Encoder();
    ~Mp3Encoder();
    int Init(const char* pcmFilePath, const char* mp3FilePath, int sampleRate, int channels, int bitRate);
    void Encode();
    void Destroy();
};