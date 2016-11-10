package com.chm006.sunflowerbible.fragment.home.five_in_a_row.view.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chm006.sunflowerbible.R;
import com.chm006.sunflowerbible.fragment.home.five_in_a_row.EventBus.BusProvider;
import com.chm006.sunflowerbible.fragment.home.five_in_a_row.EventBus.ExitGameAckEvent;
import com.chm006.sunflowerbible.fragment.home.five_in_a_row.view.fragment.NetGameFragment;
import com.gc.materialdesign.views.ButtonRectangle;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by Administrator on 2016/1/27.
 */
public class ExitAckDialog extends BaseDialog{
    public static final String TAG = "ExitAckDialog";
    public static NetGameFragment fragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_ack_exit, container, false);

        ButtonRectangle exit = (ButtonRectangle) view.findViewById(R.id.btn_exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragment == null) {
                    BusProvider.getInstance().post(new ExitGameAckEvent(true));
                } else {
                    NetGameFragment.isExit = true;
                }
            }
        });

        ButtonRectangle cancel = (ButtonRectangle) view.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BusProvider.getInstance().post(new ExitGameAckEvent(false));
            }
        });

        return view;
    }

}
