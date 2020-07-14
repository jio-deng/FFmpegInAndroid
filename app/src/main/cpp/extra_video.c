//
// extra video info
// Created by A on 2020/7/14.
//

#include "extra_video.h"

/**
 * 添加start code
 * 对H264 startcode一些粗浅的认识 ： https://blog.csdn.net/qq_24384939/article/details/88764744
 *
 * 为什么需要startcode
 * 主要是为了将相邻两个NALU划分开，让他们有一个界线,方便解码，比如将h264的数据存储在一个文件当中，解码器无法从数据流中分别每个NALU的起始位置。
 * 在编码时，每个NALU前面添加startcode(占4字节0x00000001或者3字节0x000001)，这里有人会想到万一中间出现0x000001怎么办呢，h264有个防止竞争的机制，在编码完一个NAL时，如果出现有连续两个0x00字节，就在后面插入一个0x03（解码的时候这个0x03会被丢弃）。
 *
 * 决定起始部分占4字节还是3字节
 * 实际上startcode只占3字节，4字节的起始部分 = zero_byte + start_code_prefix_one_3bytes，就是说无论啥时候其实startcode都是3字节，关键就在于zero_byte。
 * 包含sps,pps的NALU前面要加zero_byte（4字节）。
 * 当一帧被分为多个slice时，首个NALU前面要加zero_byte（4字节）。
 *
 * @param out
 * @param sps_pps
 * @param sps_pps_size
 * @param in
 * @param in_size
 * @return success ： 0； fail ： errorCode
 */
static int alloc_and_copy(AVPacket *out,
                          const uint8_t *sps_pps, uint32_t sps_pps_size,
                          const uint8_t *in, uint32_t in_size)
{
    uint32_t offset         = out->size;
    uint8_t nal_header_size = offset ? 3 : 4;
    int err;

    err = av_grow_packet(out, sps_pps_size + in_size + nal_header_size);
    if (err < 0)
        return err;

    if (sps_pps)
        memcpy(out->data + offset, sps_pps, sps_pps_size);
    memcpy(out->data + sps_pps_size + nal_header_size + offset, in, in_size);
    if (!offset) {
        AV_WB32(out->data + sps_pps_size, 1);
    } else {
        (out->data + offset + sps_pps_size)[0] =
        (out->data + offset + sps_pps_size)[1] = 0;
        (out->data + offset + sps_pps_size)[2] = 1;
    }

    return 0;
}

/**
 * H264码流中SPS PPS详解 ： https://zhuanlan.zhihu.com/p/27896239
 *
 * @param codec_extradata
 * @param codec_extradata_size
 * @param out_extradata
 * @param padding
 * @return
 */
int h264_extradata_to_annexb(const uint8_t *codec_extradata, const int codec_extradata_size, AVPacket *out_extradata, int padding)
{
    uint16_t unit_size;
    uint64_t total_size                 = 0;
    uint8_t *out                        = NULL, unit_nb, sps_done = 0,
            sps_seen                   = 0, pps_seen = 0, sps_offset = 0, pps_offset = 0;

    // 跳过前四个字节
    const uint8_t *extradata            = codec_extradata + 4;
    static const uint8_t nalu_header[4] = { 0, 0, 0, 1 };
    int length_size = (*extradata++ & 0x3) + 1; // retrieve length coded size, 用于指示表示编码数据长度所需字节数

    sps_offset = pps_offset = -1;

    /* retrieve sps and pps unit(s) */
    unit_nb = *extradata++ & 0x1f; /* number of sps unit(s) */
    if (!unit_nb) {
        goto pps;
    }else {
        sps_offset = 0;
        sps_seen = 1;
    }

    while (unit_nb--) {
        int err;

        unit_size   = AV_RB16(extradata);
        // sps和pps前也有start code
        total_size += unit_size + 4;
        if (total_size > INT_MAX - padding) {
            av_log(NULL, AV_LOG_ERROR,
                   "Too big extradata size, corrupted stream or invalid MP4/AVCC bitstream\n");
            av_free(out);
            return AVERROR(EINVAL);
        }

        if (extradata + 2 + unit_size > codec_extradata + codec_extradata_size) {
            av_log(NULL, AV_LOG_ERROR, "Packet header is not contained in global extradata, "
                                       "corrupted stream or invalid MP4/AVCC bitstream\n");
            av_free(out);
            return AVERROR(EINVAL);
        }

        // 重新分配空间，防止空间不够
        if ((err = av_reallocp(&out, total_size + padding)) < 0)
            return err;

        //拷贝start code
        memcpy(out + total_size - unit_size - 4, nalu_header, 4);
        // 拷贝sps，pps数据
        memcpy(out + total_size - unit_size, extradata + 2, unit_size);
        extradata += 2 + unit_size;
        pps:
        if (!unit_nb && !sps_done++) {
            unit_nb = *extradata++; /* number of pps unit(s) */
            if (unit_nb) {
                pps_offset = total_size;
                pps_seen = 1;
            }
        }
    }

    if (out)
        memset(out + total_size, 0, padding);

    if (!sps_seen)
        av_log(NULL, AV_LOG_WARNING,
               "Warning: SPS NALU missing or invalid. "
               "The resulting stream may not play.\n");

    if (!pps_seen)
        av_log(NULL, AV_LOG_WARNING,
               "Warning: PPS NALU missing or invalid. "
               "The resulting stream may not play.\n");

    out_extradata->data      = out;
    out_extradata->size      = total_size;

    return length_size;
}

int h264_mp4toannexb(AVFormatContext *fmt_ctx, AVPacket *in, FILE *dst_fd)
{

    AVPacket *out = NULL;
    AVPacket spspps_pkt;

    int len;
    uint8_t unit_type;
    int32_t nal_size;
    uint32_t cumul_size    = 0;
    const uint8_t *buf;
    const uint8_t *buf_end;
    int            buf_size;
    int ret = 0, i;

    out = av_packet_alloc();

    buf      = in->data;           // head
    buf_size = in->size;           // size
    buf_end  = in->data + in->size;// end

    do {
        ret= AVERROR(EINVAL);
        if (buf + 4 /*s->length_size*/ > buf_end)
            goto fail;

        // 获取size大小：前四个字节，从前往后是高位到低位
        for (nal_size = 0, i = 0; i<4/*s->length_size*/; i++)
            nal_size = (nal_size << 8) | buf[i];

        buf += 4; /*s->length_size;*/

        // 这一字节的后五位：NAL unit
        // H264(NAL简介与I帧判断) ： https://www.cnblogs.com/yjg2014/p/6144977.html
        /*Type: 5个比特.
           nal_unit_type. 这个NALU单元的类型,1～12由H.264使用，24～31由H.264以外的应用使用,简述如下:

           0     没有定义
           1-23  NAL单元  单个 NAL 单元包
           1     不分区，非IDR图像的片
           2     片分区A
           3     片分区B
           4     片分区C
           5     IDR图像中的片
           6     补充增强信息单元（SEI）
           7     SPS
           8     PPS
           9     序列结束
           10    序列结束
           11    码流借宿
           12    填充
           13-23 保留

           24    STAP-A   单一时间的组合包
           25    STAP-B   单一时间的组合包
           26    MTAP16   多个时间的组合包
           27    MTAP24   多个时间的组合包
           28    FU-A     分片的单元
           29    FU-B     分片的单元
           30-31 没有定义
         */
        unit_type = *buf & 0x1f;

        if (nal_size > buf_end - buf || nal_size < 0)
            goto fail;

        /*
        if (unit_type == 7)
            s->idr_sps_seen = s->new_idr = 1;
        else if (unit_type == 8) {
            s->idr_pps_seen = s->new_idr = 1;
            */
        /* if SPS has not been seen yet, prepend the AVCC one to PPS */
        /*
        if (!s->idr_sps_seen) {
            if (s->sps_offset == -1)
                av_log(ctx, AV_LOG_WARNING, "SPS not present in the stream, nor in AVCC, stream may be unreadable\n");
            else {
                if ((ret = alloc_and_copy(out,
                                     ctx->par_out->extradata + s->sps_offset,
                                     s->pps_offset != -1 ? s->pps_offset : ctx->par_out->extradata_size - s->sps_offset,
                                     buf, nal_size)) < 0)
                    goto fail;
                s->idr_sps_seen = 1;
                goto next_nal;
            }
        }
    }
    */

        /* if this is a new IDR picture following an IDR picture, reset the idr flag.
         * Just check first_mb_in_slice to be 0 as this is the simplest solution.
         * This could be checking idr_pic_id instead, but would complexify the parsing. */
        /*
        if (!s->new_idr && unit_type == 5 && (buf[1] & 0x80))
            s->new_idr = 1;

        */
        /* prepend only to the first type 5 NAL unit of an IDR picture, if no sps/pps are already present */
        if (/*s->new_idr && */unit_type == 5 /*&& !s->idr_sps_seen && !s->idr_pps_seen*/) {

            h264_extradata_to_annexb( fmt_ctx->streams[in->stream_index]->codec->extradata,
                                      fmt_ctx->streams[in->stream_index]->codec->extradata_size,
                                      &spspps_pkt,
                                      AV_INPUT_BUFFER_PADDING_SIZE);

            if ((ret=alloc_and_copy(out,
                                    spspps_pkt.data, spspps_pkt.size,
                                    buf, nal_size)) < 0)
                goto fail;
            /*s->new_idr = 0;*/
            /* if only SPS has been seen, also insert PPS */
        }
            /*else if (s->new_idr && unit_type == 5 && s->idr_sps_seen && !s->idr_pps_seen) {
                if (s->pps_offset == -1) {
                    av_log(ctx, AV_LOG_WARNING, "PPS not present in the stream, nor in AVCC, stream may be unreadable\n");
                    if ((ret = alloc_and_copy(out, NULL, 0, buf, nal_size)) < 0)
                        goto fail;
                } else if ((ret = alloc_and_copy(out,
                                            ctx->par_out->extradata + s->pps_offset, ctx->par_out->extradata_size - s->pps_offset,
                                            buf, nal_size)) < 0)
                    goto fail;
            }*/ else {
            if ((ret=alloc_and_copy(out, NULL, 0, buf, nal_size)) < 0)
                goto fail;
            /*
            if (!s->new_idr && unit_type == 1) {
                s->new_idr = 1;
                s->idr_sps_seen = 0;
                s->idr_pps_seen = 0;
            }
            */
        }


        len = fwrite( out->data, 1, out->size, dst_fd);
        if(len != out->size){
            av_log(NULL, AV_LOG_DEBUG, "warning, length of writed data isn't equal pkt.size(%d, %d)\n",
                   len,
                   out->size);
        }
        fflush(dst_fd);

        next_nal:
        buf        += nal_size;
        cumul_size += nal_size + 4;//s->length_size;
    } while (cumul_size < buf_size);

    /*
    ret = av_packet_copy_props(out, in);
    if (ret < 0)
        goto fail;

    */
    fail:
    av_packet_free(&out);

    return ret;
}