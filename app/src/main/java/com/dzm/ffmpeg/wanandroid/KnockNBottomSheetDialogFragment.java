package com.dzm.ffmpeg.wanandroid;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;

import com.dzm.ffmpeg.R;
import com.dzm.ffmpeg.databinding.FragmentKnockNBinding;
import com.dzm.ffmpeg.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description 敲7
 * @date 2020/7/12 15:20
 */
public class KnockNBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private FragmentKnockNBinding mDataBinding;
    private Set<Integer> mChosenNums;
    private KnockNAdapter mAdapter = new KnockNAdapter();
    private BottomSheetBehavior mBehavior;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog mBottomDialog = new BottomSheetDialog(getActivity(), R.style.DialogFullScreen);

        mDataBinding = DataBindingUtil.inflate(getActivity().getLayoutInflater(),
                R.layout.fragment_knock_n, null, false);

        initViews();

        mBottomDialog.setContentView(mDataBinding.getRoot());
        mBottomDialog.setCanceledOnTouchOutside(true);

        Window window = mBottomDialog.getWindow();
        if (window != null) {
            // 软键盘弹出时不会影响布局
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            // 设置进场/出场动画
            window.setWindowAnimations(R.style.DialogBottomAnim);
        }

        return mBottomDialog;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 设置高度
        Dialog dialog = getDialog();
        if (dialog != null) {
            View bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
            bottomSheet.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        }
    }

    private void initViews() {
        mChosenNums = new HashSet<>();

        mDataBinding.btn2.setOnClickListener(v -> {
            if (mChosenNums.contains(2)) {
                mChosenNums.remove(2);
            } else {
                mChosenNums.add(2);
            }

            refresh();
        });

        mDataBinding.btn3.setOnClickListener(v -> {
            if (mChosenNums.contains(3)) {
                mChosenNums.remove(3);
            } else {
                mChosenNums.add(3);
            }

            refresh();
        });

        mDataBinding.btn5.setOnClickListener(v -> {
            if (mChosenNums.contains(5)) {
                mChosenNums.remove(5);
            } else {
                mChosenNums.add(5);
            }

            refresh();
        });

        mDataBinding.btn7.setOnClickListener(v -> {
            if (mChosenNums.contains(7)) {
                mChosenNums.remove(7);
            } else {
                mChosenNums.add(7);
            }

            refresh();
        });


        mDataBinding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 10));
        mDataBinding.recyclerView.setAdapter(mAdapter);
        mDataBinding.recyclerView.setNestedScrollingEnabled(false);


        // behavior
        mDataBinding.getRoot().post(new Runnable() {
            @Override
            public void run() {
                mBehavior = BottomSheetBehavior.from((View) mDataBinding.getRoot().getParent());
                //默认全屏展开
                mBehavior.setPeekHeight(Utils.getApplication().getResources().getDisplayMetrics().heightPixels);
                mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }

    private void refresh() {
        StringBuilder sb = new StringBuilder();
        for (int i : mChosenNums) {
            sb.append(i).append(" ");
        }
        mDataBinding.tvChosenNums.setText(sb.toString());
        mAdapter.setNums(mChosenNums);
    }

    /**
     * 启动KnockNDialogFragment
     */
    public static void showKnockNDialogFragment(FragmentManager fragmentManager) {
        if (fragmentManager == null) {
            return;
        }
        KnockNBottomSheetDialogFragment dialogFragment = (KnockNBottomSheetDialogFragment) fragmentManager.findFragmentByTag("KnockN");
        if (dialogFragment == null) {
            dialogFragment = new KnockNBottomSheetDialogFragment();
            dialogFragment.show(fragmentManager, "KnockN");
        }
    }
}
