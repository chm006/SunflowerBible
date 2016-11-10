package com.chm006.sunflowerbible.fragment.home.five_in_a_row.view.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chm006.sunflowerbible.R;


/**
 * Created by Administrator on 2016/1/27.
 */
public class RestartWaitingDialog extends BaseDialog {

    public static final String TAG = "RestartWaitingDialog";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.dialog_waiting_restart, container, false);
    }
}
