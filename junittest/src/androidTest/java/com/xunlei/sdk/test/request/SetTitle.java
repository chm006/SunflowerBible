package com.xunlei.sdk.test.request;

import android.database.Cursor;
import android.net.Uri;
import com.xunlei.download.DownloadManager.Request;
import com.example.junittest.utils.BaseCase;
import com.example.junittest.utils.CaseUtils;
import com.example.junittest.utils.log.DebugLog;

/*
 * setTitle 设置下载任务的标题
 */
public class SetTitle extends BaseCase {
	private Request request;

	public void testSetTitle() {
		printDivideLine();
		// 添加任务Request
		request = new Request(
				Uri.parse("http://cache.iruan.cn/201412/201412181_uc.apk"));
		String titleString = "Test Title";
		// 调用接口
		CharSequence titleChar = (CharSequence) titleString;
		request.setTitle(titleChar);
		// 建立下载任务
		long id = downloadManager.enqueue(request);
		DebugLog.d("Test_Debug", "Task ID = " + id);
		assertTrue("下载任务建立失败", id > 0);
		sleep(1);
		// 查询本地数据库验证结果
		Cursor cursor = CaseUtils.selectTask(this.getContext(),
				downloadManager, id);
		String titleColomn = cursor.getString(cursor.getColumnIndex("title"));
		DebugLog.d("Test_Debug", "Title = " + titleColomn);
		assertEquals("任务标题错误", titleString, titleColomn);
		//删除下载任务，清理测试环境
		CaseUtils.deleteTasks(downloadManager, id);
	}
}
