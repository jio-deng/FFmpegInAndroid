package com.dzm.ffmpeg.yinshipin.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.dzm.ffmpeg.R;
import com.dzm.ffmpeg.databinding.ActivityGetVideoMetaDataBinding;
import com.dzm.ffmpeg.utils.LogUtils;
import com.dzm.ffmpeg.utils.Utils;
import com.dzm.ffmpeg.yinshipin.FFmpegTest;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhihu.matisse.listener.OnConfirmedListener;

import java.util.List;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description Get Video Meta Data
 * @date 2020/7/11 9:19
 */
public class FetchMetaDataActivity extends AppCompatActivity {
    private static final int REQUEST_OPEN_MATISSE = 110;

    private Matisse matisse = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityGetVideoMetaDataBinding dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_get_video_meta_data);
        init(dataBinding);
    }

    private void init(ActivityGetVideoMetaDataBinding dataBinding) {
        // get meta data
        dataBinding.btnSelectVideo.setOnClickListener(v -> {
            getVideo(new OnConfirmedListener() {
                @Override
                public void onConfirm(@NonNull List<Uri> uriList, @NonNull List<String> pathList) {
                    String filePath = pathList.get(0);
                    LogUtils.d("filePath = " + filePath);
                    dataBinding.tvShowMetaData.setText(FFmpegTest.printMeta(filePath));
                    if (matisse != null) {
                        matisse.finish();
                    }
                }
            });
        });

        // get audio track
        dataBinding.btnGetAudioTrack.setOnClickListener(v -> {
            getVideo(new OnConfirmedListener() {
                @Override
                public void onConfirm(@NonNull List<Uri> uriList, @NonNull List<String> pathList) {
                    String filePath = pathList.get(0);
                    StringBuilder outPath = new StringBuilder();
                    outPath.append(getExternalCacheDir()).append("/").append(System.currentTimeMillis()).append(".aac");
                    String pp = "filePath = " + filePath + ", outPath = " + outPath;
                    LogUtils.d(pp);

                    dataBinding.tvShowMetaData.setText(pp + "\n" + FFmpegTest.getAudioTrack(filePath, outPath.toString()));
                    if (matisse != null) {
                        matisse.finish();
                    }
                }
            });
        });

        // get video track
        dataBinding.btnGetVideoTrack.setOnClickListener(v -> {
            getVideo(new OnConfirmedListener() {
                @Override
                public void onConfirm(@NonNull List<Uri> uriList, @NonNull List<String> pathList) {
                    String filePath = pathList.get(0);
                    StringBuilder outPath = new StringBuilder();
                    outPath.append(getExternalCacheDir()).append("/").append(System.currentTimeMillis()).append(".h264");
                    String pp = "filePath = " + filePath + ", outPath = " + outPath;
                    LogUtils.d(pp);

                    dataBinding.tvShowMetaData.setText(pp + "\n" + FFmpegTest.getVideoTrack(filePath, outPath.toString()));
                    if (matisse != null) {
                        matisse.finish();
                    }
                }
            });
        });

        // cut video
        dataBinding.btnCutVideo.setOnClickListener(v -> {
            getVideo(new OnConfirmedListener() {
                @Override
                public void onConfirm(@NonNull List<Uri> uriList, @NonNull List<String> pathList) {
                    String filePath = pathList.get(0);
                    StringBuilder outPath = new StringBuilder();
                    outPath.append(getExternalCacheDir()).append("/").append(System.currentTimeMillis()).append(".mp4");
                    String pp = "filePath = " + filePath + ", outPath = " + outPath;
                    LogUtils.d(pp);
                    // TODO
                    double fromSecond = 10, toSecond = 20;
                    int ret = FFmpegTest.cutVideo(fromSecond, toSecond, filePath, outPath.toString());
                    LogUtils.d("ret = " + ret);
                    dataBinding.tvShowMetaData.setText(pp + "\n" + ret);
                    if (matisse != null) {
                        matisse.finish();
                    }
                }
            });
        });
    }

    private void getVideo(OnConfirmedListener listener) {
        if (matisse == null) {
            matisse = Matisse.from(FetchMetaDataActivity.this);
        }

        matisse.choose(MimeType.ofVideo())
                .showSingleMediaType(true)
                .capture(true)
                .captureStrategy(new CaptureStrategy(true, Utils.FILE_PROVIDER_NAME))
                .countable(false)
                .maxSelectable(1)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .theme(R.style.Matisse_Dracula)
                .setOnConfirmedListener(listener)
                .forResult(REQUEST_OPEN_MATISSE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == REQUEST_OPEN_MATISSE) {
            LogUtils.d("REQUEST_OPEN_MATISSE = " + REQUEST_OPEN_MATISSE);
        }
    }
}
