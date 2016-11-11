package com.xunlei.sdk.test.request;

import android.database.Cursor;
import android.net.Uri;
import com.xunlei.download.DownloadManager.Request;
import com.example.junittest.utils.BaseCase;
import com.example.junittest.utils.CaseUtils;
import com.example.junittest.utils.log.DebugLog;

/*
 * setDestinationInExternalPublicDir 设置下载到外置存储的路径
 */
public class SetDestinationInExternalPublicDir extends BaseCase {
	private Request request;

	public void testSetDestinationInExternalPublicDir() {
		printDivideLine();
		// 添加测试Request
		request = new Request(
				Uri.parse("http://cache.iruan.cn/201412/201412181_uc.apk"));
		// 调用接口
		request.setDestinationInExternalPublicDir("Download/sdk_test",
				"201412181_uc.apk");
		// 建立下载任务
		long id = downloadManager.enqueue(request);
		DebugLog.d("Test_Debug", "Task ID = " + id);
		assertTrue("下载任务建立失败", id > 0);
		// 查询本地数据库验证结果
		Cursor cursor = CaseUtils.selectTask(this.getContext(),
				downloadManager, id);
		String hint = cursor.getString(cursor.getColumnIndex("hint"));
		DebugLog.d("Test_Debug", "存储路径 = " + hint);
		assertEquals(
				"存储路径错误",
				"file:///storage/emulated/0/Download/sdk_test/201412181_uc.apk",
				hint);
		//删除下载任务，清理测试环境
		CaseUtils.deleteTasks(downloadManager, id);
	}
}
