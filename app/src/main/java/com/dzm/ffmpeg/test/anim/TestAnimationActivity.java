package com.dzm.ffmpeg.test.anim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.dzm.ffmpeg.BaseActivity;
import com.dzm.ffmpeg.R;
import com.dzm.ffmpeg.databinding.ActivityAnimatorTestBinding;
import com.dzm.ffmpeg.utils.ToastUtil;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description test Animator / Animation
 * @date 2020/8/10 9:06
 */
public class TestAnimationActivity extends BaseActivity {
    private ActivityAnimatorTestBinding mDataBinding;
    private boolean isMenuOpen = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_animator_test);

        mDataBinding.btnScaleX.setOnClickListener(v -> testAnimatorScaleX());
        mDataBinding.btnInterpolator1.setOnClickListener(v -> startAnimation(mDataBinding.ivGoast4anim, R.anim.test1));
        mDataBinding.btnInterpolator2.setOnClickListener(v -> startAnimation(mDataBinding.ivGoast4anim, R.anim.test2));
        mDataBinding.btnInterpolator3.setOnClickListener(v -> startAnimation(mDataBinding.ivGoast4anim, R.anim.test3));
        mDataBinding.btnInterpolator4.setOnClickListener(v -> startAnimation(mDataBinding.ivGoast4anim, R.anim.test4));
        mDataBinding.btnInterpolator5.setOnClickListener(v -> startAnimation(mDataBinding.ivGoast4anim, R.anim.test5));
        mDataBinding.btnInterpolator6.setOnClickListener(v -> startAnimation(mDataBinding.ivGoast4anim, R.anim.test6));
        mDataBinding.btnInterpolator7.setOnClickListener(v -> startAnimation(mDataBinding.ivGoast4anim, R.anim.test7));
        mDataBinding.btnInterpolator8.setOnClickListener(v -> startAnimation(mDataBinding.ivGoast4anim, R.anim.test8));
        mDataBinding.btnInterpolator9.setOnClickListener(v -> startAnimation(mDataBinding.ivGoast4anim, R.anim.test9));

        mDataBinding.btnArgb.setOnClickListener(v -> argb(mDataBinding.ivArgb));
        mDataBinding.btnCombine.setOnClickListener(v -> combine(mDataBinding.ivArgb));
        mDataBinding.btnRotateRepeat.setOnClickListener(v -> rotateLeftRightRepeat(mDataBinding.ivArgb));
        mDataBinding.btnMenu.setOnClickListener(v -> {
            if (!isMenuOpen) {
                doMenuOpen();
                isMenuOpen = true;
            } else {
                doMenuClose();
                isMenuOpen = false;
            }
        });
        mDataBinding.btnMenuItem1.setOnClickListener(v -> mDataBinding.drawView.reset());
        mDataBinding.btnMenuItem2.setOnClickListener(v -> ToastUtil.makeText(this, "This is a loli!"));

        ScaleAnimation animation = new ScaleAnimation(0f, 1f, 0f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(700);

    }

    private void testAnimatorScaleX() {
        ValueAnimator animator = ObjectAnimator.ofFloat(mDataBinding.ivGoast4anim, "scaleX", 0f, 1f);
        animator.setDuration(2000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(-1);
        animator.start();
    }

    private void startAnimation(View view, int resId) {
        Animation animation = AnimationUtils.loadAnimation(this, resId);
        view.startAnimation(animation);
    }

    private void argb(View view) {
        ValueAnimator animator = ValueAnimator.ofInt(0xffffff00,0xff0000ff);
        animator.setEvaluator(new ArgbEvaluator());
        animator.setDuration(3000);

        animator.addUpdateListener(animation -> {
            int curValue = (int) animation.getAnimatedValue();
            view.setBackgroundColor(curValue);
        });

        animator.start();
    }

    private void combine(View view) {
        PropertyValuesHolder rotationHolder = PropertyValuesHolder.ofFloat("Rotation", 60f, -60f, 40f, -40f, -20f, 20f, 10f, -10f, 0f);
        PropertyValuesHolder colorHolder = PropertyValuesHolder.ofInt("BackgroundColor", 0xffffffff, 0xffff00ff, 0xffffff00, 0xffffffff);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view, rotationHolder, colorHolder);
        animator.setDuration(3000);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.start();
    }

    private void rotateLeftRightRepeat(View view){
        Keyframe frame0 = Keyframe.ofFloat(0f, 0);
        Keyframe frame1 = Keyframe.ofFloat(0.1f, -20f);
        Keyframe frame2 = Keyframe.ofFloat(0.2f, 20f);
        Keyframe frame3 = Keyframe.ofFloat(0.3f, -20f);
        Keyframe frame4 = Keyframe.ofFloat(0.4f, 20f);
        Keyframe frame5 = Keyframe.ofFloat(0.5f, -20f);
        Keyframe frame6 = Keyframe.ofFloat(0.6f, 20f);
        Keyframe frame7 = Keyframe.ofFloat(0.7f, -20f);
        Keyframe frame8 = Keyframe.ofFloat(0.8f, 20f);
        Keyframe frame9 = Keyframe.ofFloat(0.9f, -20f);
        Keyframe frame10 = Keyframe.ofFloat(1, 0);
        frame10.setInterpolator(new BounceInterpolator());

        PropertyValuesHolder frameHolder = PropertyValuesHolder.ofKeyframe("rotation",
                frame0,frame1,frame2,frame3,frame4,frame5,frame6,frame7,frame8,frame9,frame10);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view, frameHolder);
        animator.setDuration(1000);
        animator.start();
    }

    //        ValueAnimator valueAnimator = (ValueAnimator) AnimatorInflater.loadAnimator(this, R.animator.animation_combine_1);
    private void doMenuOpen() {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(mDataBinding.btnMenuItem1, "translationX", 0, -200),
                ObjectAnimator.ofFloat(mDataBinding.btnMenuItem1, "scaleX", 0f, 1f),
                ObjectAnimator.ofFloat(mDataBinding.btnMenuItem1, "scaleY", 0f, 1f),
                ObjectAnimator.ofFloat(mDataBinding.btnMenuItem1, "alpha", 0f, 1f),
                ObjectAnimator.ofFloat(mDataBinding.btnMenuItem2, "translationY", 0, -200),
                ObjectAnimator.ofFloat(mDataBinding.btnMenuItem2, "scaleX", 0f, 1f),
                ObjectAnimator.ofFloat(mDataBinding.btnMenuItem2, "scaleY", 0f, 1f),
                ObjectAnimator.ofFloat(mDataBinding.btnMenuItem2, "alpha", 0f, 1f));
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mDataBinding.btnMenuItem1.setVisibility(View.VISIBLE);
                mDataBinding.btnMenuItem2.setVisibility(View.VISIBLE);
            }
        });
        set.setDuration(500).start();
    }

    private void doMenuClose() {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(mDataBinding.btnMenuItem1, "translationX", -200, 0),
                ObjectAnimator.ofFloat(mDataBinding.btnMenuItem1, "scaleX", 1f, 0f),
                ObjectAnimator.ofFloat(mDataBinding.btnMenuItem1, "scaleY", 1f,0f),
                ObjectAnimator.ofFloat(mDataBinding.btnMenuItem1, "alpha", 1f, 0f),
                ObjectAnimator.ofFloat(mDataBinding.btnMenuItem2, "translationY", -200, 0),
                ObjectAnimator.ofFloat(mDataBinding.btnMenuItem2, "scaleX", 1f, 0f),
                ObjectAnimator.ofFloat(mDataBinding.btnMenuItem2, "scaleY", 1f,0f),
                ObjectAnimator.ofFloat(mDataBinding.btnMenuItem2, "alpha", 1f, 0f));
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mDataBinding.btnMenuItem1.setVisibility(View.GONE);
                mDataBinding.btnMenuItem2.setVisibility(View.GONE);
            }
        });
        set.setDuration(500).start();
    }

}
