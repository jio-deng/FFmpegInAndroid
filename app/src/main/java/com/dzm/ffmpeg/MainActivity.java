package com.dzm.ffmpeg;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description TODO
 * @date 2019/11/28 9:50
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                    Toast.makeText(MainActivity.this, "error converting: result = " + result, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    static{
        System.loadLibrary("native-lib");
        System.loadLibrary("lame");
    }
}
