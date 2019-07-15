package com.dzm.ffmpeg

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        version.text = Mp3Encoder.getVersion()
        convert_pcm_2_mp3.setOnClickListener {
            val result = Mp3Encoder.init("file:///android_asset/pcmTestData.pcm",
                    1, 100, 100, "file:///android_asset/pcmTestData.mp3")
            if (result == 0) {
                Mp3Encoder.encode()
                Mp3Encoder.destroy()
            } else {
                Toast.makeText(this, "error converting!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
            System.loadLibrary("lame")
        }
    }
}
