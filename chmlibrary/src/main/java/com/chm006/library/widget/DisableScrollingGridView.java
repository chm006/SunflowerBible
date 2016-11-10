package com.chm006.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 禁止滚动条的GridView
 * Created by chenmin on 2016/8/17.
 */
public class DisableScrollingGridView extends GridView {

	public DisableScrollingGridView(Context context) {
		super(context);
	}

	public DisableScrollingGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DisableScrollingGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
