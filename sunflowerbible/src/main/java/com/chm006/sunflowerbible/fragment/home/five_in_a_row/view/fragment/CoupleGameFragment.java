package com.chm006.sunflowerbible.fragment.home.five_in_a_row.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.chm006.library.base.fragment.BaseBackFragment;
import com.chm006.library.base.fragment.BaseFragment;
import com.chm006.library.utils.ToastUtil;
import com.chm006.sunflowerbible.R;
import com.chm006.sunflowerbible.fragment.home.five_in_a_row.bean.Point;
import com.chm006.sunflowerbible.fragment.home.five_in_a_row.util.GameJudger;
import com.chm006.sunflowerbible.fragment.home.five_in_a_row.widget.GoBangBoard;
import com.gc.materialdesign.views.ButtonRectangle;

/**
 * Created by Xuf on 2016/2/7.
 */
public class CoupleGameFragment extends BaseBackFragment implements GoBangBoard.PutChessListener, View.OnClickListener, View.OnTouchListener {

    private boolean mIsGameStarted = false;
    private boolean mIsWhiteFirst = true;
    private boolean mCurrentWhite;

    private GoBangBoard mGoBangBoard;
    private ButtonRectangle mStartGame;
    private ButtonRectangle mStartFirst;

    public static CoupleGameFragment newInstance() {
        Bundle args = new Bundle();
        CoupleGameFragment fragment = new CoupleGameFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_couple_game;
    }

    @Override
    public void init() {
        initToolbar();
        initView(rootView);
    }

    private void initView(View view) {
        mGoBangBoard = (GoBangBoard) view.findViewById(R.id.go_bang_board);
        mGoBangBoard.setOnTouchListener(this);
        mGoBangBoard.setPutChessListener(this);

        mCurrentWhite = mIsWhiteFirst;

        mStartGame = (ButtonRectangle) view.findViewById(R.id.btn_start_game);
        mStartGame.setOnClickListener(this);

        mStartFirst = (ButtonRectangle) view.findViewById(R.id.btn_start_first);
        mStartFirst.setOnClickListener(this);

        ButtonRectangle exitGame = (ButtonRectangle) view.findViewById(R.id.btn_exit_game);
        exitGame.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_game:
                if (!mIsGameStarted) {
                    mIsGameStarted = true;
                    mCurrentWhite = mIsWhiteFirst;
                    setWidgets();
                }
                break;
            case R.id.btn_start_first:
                mIsWhiteFirst = !mIsWhiteFirst;
                mCurrentWhite = mIsWhiteFirst;
                String buttonText;
                if (mCurrentWhite){
                    buttonText = "黑子先手";
                } else {
                    buttonText = "白子先手";
                }
                mStartFirst.setText(buttonText);
                break;
            case R.id.btn_exit_game:
                pop();
                break;
        }
    }

    @Override
    public void onPutChess(int[][] board, int x, int y) {
        if (GameJudger.isGameEnd(board, x, y)) {
            String msg = String.format("%s赢了", mCurrentWhite ? "黑棋" : "白棋");
            ToastUtil.showShort(getActivity(), msg);
            mIsGameStarted = false;
            resetWidgets();
        }
    }

    private void setWidgets() {
        mGoBangBoard.clearBoard();
        mStartGame.setEnabled(false);
        mStartFirst.setEnabled(false);
    }

    private void resetWidgets() {
        mStartGame.setEnabled(true);
        mStartFirst.setEnabled(true);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!mIsGameStarted) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                float y = event.getY();
                Point point = mGoBangBoard.convertPoint(x, y);
                if (mGoBangBoard.putChess(mCurrentWhite, point.x, point.y)) {
                    mCurrentWhite = !mCurrentWhite;
                }
                break;
        }
        return false;
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        initToolbarNav(toolbar, R.mipmap.ic_arrow_back_white_24dp, "五子棋 - 双人模式");
    }

}
