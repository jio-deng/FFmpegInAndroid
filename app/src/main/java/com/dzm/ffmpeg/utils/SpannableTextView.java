package com.dzm.ffmpeg.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import com.dzm.ffmpeg.R;

/**
 * 可配置的TextView
 *
 * 支持部分文字颜色修改、增加下划线、添加点击事件
 * @author dzm
 */
public class SpannableTextView extends AppCompatTextView {
    private static int defaultColor = Color.parseColor("#d5d5d5");

    private String text;
    private int textColor;
    /**
     * 需要单独定制的String：多个String用“;”隔开
     */
    private String[] difText;
    private int difTextColor;
    private boolean hasUnderLine;
    /**
     * 点击事件
     */
    private ClickCallBack mClickCallBack;

    public SpannableTextView(Context context) {
        super(context);
    }

    public SpannableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public SpannableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SpannableTextView, defStyleAttr, 0);

        text = getText().toString();
        textColor = typedArray.getColor(R.styleable.SpannableTextView_textColor, defaultColor);
        hasUnderLine = typedArray.getBoolean(R.styleable.SpannableTextView_underLine, false);

        String difTextTemp = typedArray.getString(R.styleable.SpannableTextView_difText);
        if (!TextUtils.isEmpty(difTextTemp)) {
            difText = difTextTemp.split(";");
            difTextColor = typedArray.getColor(R.styleable.SpannableTextView_difTextColor, defaultColor);
        }

        parseSpannable();

        typedArray.recycle();
    }

    private void parseSpannable() {
        SpannableString spannableString = new SpannableString(text);

        if (difText != null && difText.length != 0) {
            int curStart = 0;
            for (int i = 0; i < difText.length; i ++) {
                final int start = text.indexOf(difText[i]);
                final int end = start + difText[i].length();

                setClickableSpan(spannableString, curStart, start, Spanned.SPAN_INCLUSIVE_INCLUSIVE,
                        -1, textColor);

                setClickableSpan(spannableString, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
                        i, difTextColor);

                curStart = end;
            }

            if (curStart < text.length() - 1) {
                setClickableSpan(spannableString, curStart, text.length() - 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE,
                        -1, textColor);
            }
        }

        setText(spannableString);
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * 设置Span
     *
     * @param spannableString spannableString
     * @param start start
     * @param end end
     * @param style Spanned style
     * @param callbackPosition position:未处理部分回调-1
     * @param textColor text color
     */
    private void setClickableSpan(SpannableString spannableString, int start, int end, int style,
                                  int callbackPosition, int textColor) {
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                if (mClickCallBack != null) {
                    mClickCallBack.onClick(callbackPosition);
                }
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(textColor);
                ds.setUnderlineText(hasUnderLine);
            }
        }, start, end, style);
    }

    public void setOnClickCallBack(ClickCallBack clickCallBack) {
        this.mClickCallBack = clickCallBack;
    }

    public interface ClickCallBack {
        /**
         * difText点击时触发的函数
         * @param position num of difText
         */
        void onClick(int position);
    }
}
