package com.dzm.ffmpeg.yinshipin.activity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.dzm.ffmpeg.BaseActivity;
import com.dzm.ffmpeg.R;
import com.dzm.ffmpeg.databinding.ActivityFfmodBinding;
import com.dzm.ffmpeg.optimize.AopOnClick;
import com.dzm.ffmpeg.utils.LogUtils;
import com.dzm.ffmpeg.utils.ThreadUtils;
import com.dzm.ffmpeg.utils.ToastUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description FFMod
 * @date 2020/8/4 20:46
 */
public class FModActivity extends BaseActivity {
    public static final int MODE_ORIGN = 0;
    public static final int MODE_LOLI = 1;
    public static final int MODE_MAN = 2;
    public static final int MODE_GOAST = 3;

    private ActivityFfmodBinding mDataBinding;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        @AopOnClick
        public void onClick(View v) {
            /**
             * getCacheDir()                                    : /data/user/0/com.dzm.ffmpeg/cache
             * getExternalCacheDir()                            : /storage/emulated/0/Android/data/com.dzm.ffmpeg/cache
             * getExternalFilesDir(null)                        : /storage/emulated/0/Android/data/com.dzm.ffmpeg/files
             * getExternalFilesDir(Environment.DIRECTORY_MUSIC) : /storage/emulated/0/Android/data/com.dzm.ffmpeg/files/Music
             */
//            path = getExternalFilesDir(null) + File.separator +"10010.mp3";
            String path = Environment.getExternalStorageDirectory().getPath() + File.separator + "lingneng100.mp3";
            LogUtils.d("path : " + path);
            switch (v.getId()) {
                case R.id.iv_voice_orign:
                    ThreadUtils.getSingleThreadPool().submit(() -> {
                        fix(path, MODE_ORIGN);
                    });
                    break;
                case R.id.iv_voice_loli:
                    ThreadUtils.getSingleThreadPool().submit(() -> {
                        fix(path, MODE_LOLI);
                    });
                    break;
                case R.id.iv_voice_man:
                    ThreadUtils.getSingleThreadPool().submit(() -> {
                        fix(path, MODE_MAN);
                    });
                    break;
                case R.id.iv_voice_goast:
                    ThreadUtils.getSingleThreadPool().submit(() -> {
                        fix(path, MODE_GOAST);
                    });
                    break;
                default:
                    ToastUtil.makeText(FModActivity.this, "Mode is invalid!");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_ffmod);

        mDataBinding.ivVoiceOrign.setOnClickListener(mOnClickListener);
        mDataBinding.ivVoiceLoli.setOnClickListener(mOnClickListener);
        mDataBinding.ivVoiceMan.setOnClickListener(mOnClickListener);
        mDataBinding.ivVoiceGoast.setOnClickListener(mOnClickListener);

        mDataBinding.btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThreadUtils.getSingleThreadPool().submit(() -> {
                    try {
                        InputStream inputStream =  getResources().openRawResource(R.raw.lingneng100);
                        File file = new File(Environment.getExternalStorageDirectory().getPath(),"lingneng100.mp3");
                        if(!file.exists()){
                            file.createNewFile();
                        }
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        byte[] buffer = new byte[10];
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        int len = 0;
                        while ((len=inputStream.read(buffer))!=-1){
                            outputStream.write(buffer,0,len);
                        }
                        byte[] bs = outputStream.toByteArray();
                        fileOutputStream.write(bs);
                        outputStream.close();
                        inputStream.close();
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtils.d("getPath",e.getMessage());
                    }
                });
            }
        });
    }

    public static native void fix(String path, int mod);

}
