package com.dzm.ffmpeg.tools;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dzm.ffmpeg.R;
import com.dzm.ffmpeg.utils.ToastUtil;
import com.dzm.ffmpeg.utils.Utils;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description morse-code translate
 * @date 2020/2/28 12:39
 */
public class MorseCodeActivity extends AppCompatActivity {
    private EditText editText;
    private TextView textView;
    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morse_code);

        editText = findViewById(R.id.et_morse_code);
        textView = findViewById(R.id.tv_morse_code);
        button = findViewById(R.id.btn_copy);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                textView.setText(getMorseCode(s));
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.clipToBoard(textView.getText().toString());
                ToastUtil.makeText(MorseCodeActivity.this, "摩斯密码已复制到剪切板");
            }
        });

    }

    private String getMorseCode(Editable s) {
        String string = s.toString().toUpperCase();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < string.length(); i ++) {
            sb.append(getMorseCodeCore(string.charAt(i))).append(" ");
        }

        return sb.toString();
    }

    private String getMorseCodeCore(char c) {
        switch (c) {
            case 'A':
                return ".-";
            case 'B':
                return "-...";
            case 'C':
                return "-.-.";
            case 'D':
                return "-..";
            case 'E':
                return ".";
            case 'F':
                return "..-.";
            case 'G':
                return "--.";
            case 'H':
                return "....";
            case 'I':
                return "..";
            case 'J':
                return ".---";
            case 'K':
                return "-.-";
            case 'L':
                return ".-..";
            case 'M':
                return "--";
            case 'N':
                return "-.";
            case 'O':
                return "---";
            case 'P':
                return ".--.";
            case 'Q':
                return "--.-";
            case 'R':
                return ".-.";
            case 'S':
                return "...";
            case 'T':
                return "-";
            case 'U':
                return "..-";
            case 'V':
                return "...-";
            case 'W':
                return ".--";
            case 'X':
                return "-..-";
            case 'Y':
                return "-.--";
            case 'Z':
                return "--..";
        }

        return String.valueOf(c);
    }
}
