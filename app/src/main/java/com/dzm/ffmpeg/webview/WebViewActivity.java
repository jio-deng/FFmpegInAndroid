package com.dzm.ffmpeg.webview;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dzm.ffmpeg.R;
import com.itheima.view.BridgeWebView;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description web view
 * @date 2020/2/27 19:09
 */
public class WebViewActivity extends AppCompatActivity {
    private BridgeWebView mWebview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Intent intent = getIntent();
        String link = intent.getStringExtra("link");

        mWebview = findViewById(R.id.webview);//初始化BridgeWebView
        mWebview.loadUrl(link);//显示H5页面
        // mWebview.addBridgeInterface(new MyJavaSctiptInterface(mWebview, this));//注册桥梁类，该类负责H5和android通信
    }
}
