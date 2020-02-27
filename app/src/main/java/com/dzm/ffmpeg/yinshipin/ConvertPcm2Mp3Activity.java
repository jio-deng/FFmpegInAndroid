package com.dzm.ffmpeg.yinshipin;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dzm.ffmpeg.Mp3Encoder;
import com.dzm.ffmpeg.R;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description Convert Pcm 2 Mp3
 * @date 2020/2/18 11:13
 */
public class ConvertPcm2Mp3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert_pcm_2_mp3);

        // lame version
        String versionText = Mp3Encoder.getVersion();
        ((TextView) findViewById(R.id.version)).setText(versionText);

        // lame convert pcm to mp3
        findViewById(R.id.convert_pcm_2_mp3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result = Mp3Encoder.init("file:///android_asset/pcmTestData.pcm",
                        1, 100, 100, "file:///android_asset/pcmTestData.mp3");
                if (result == 0) {
                    Mp3Encoder.encode();
                    Mp3Encoder.destroy();
                } else {
                    Toast.makeText(ConvertPcm2Mp3Activity.this, "error converting: result = " + result, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    static{
        System.loadLibrary("lame");
    }
}
