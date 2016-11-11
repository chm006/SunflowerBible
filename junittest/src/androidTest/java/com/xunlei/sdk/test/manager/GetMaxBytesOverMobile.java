package com.xunlei.sdk.test.manager;

import android.content.Context;
import com.xunlei.download.DownloadManager;
import com.example.junittest.utils.BaseCase;
import com.example.junittest.utils.CaseUtils;
import com.example.junittest.utils.log.DebugLog;

/*
 * getMaxBytesOverMobile 获取移动网络下载限制
 */
public class GetMaxBytesOverMobile extends BaseCase {

	public void testGetMaxBytesOverMobile() {
		printDivideLine();
		Context context = this.getContext();
		// 调用接口
		long bytes = DownloadManager.getMaxBytesOverMobile(context);
		DebugLog.d("Test_Debug",
				"MaxBytes OverMobile = " + bytes);
		// 验证结果
		assertEquals("移动网络下载限制异常", 4194304L, bytes);
	}

}
