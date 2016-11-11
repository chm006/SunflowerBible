package com.xunlei.sdk.test.request;

import android.database.Cursor;
import android.net.Uri;
import com.xunlei.download.DownloadManager.Request;
import com.example.junittest.utils.BaseCase;
import com.example.junittest.utils.CaseUtils;
import com.example.junittest.utils.log.DebugLog;
/*
 * setDescription 添加任务描述
 */
public class SetDescription extends BaseCase {
	private Request request;

	public void testSetDescription() {
		printDivideLine();
		// 添加任务Request
		request = new Request(
				Uri.parse("http://cache.iruan.cn/201412/201412181_uc.apk"));
		// 调用接口
		String descString = "Test Description";
		CharSequence descChar = (CharSequence) descString;
		request.setDescription(descChar);
		// 建立下载任务
		long id = downloadManager.enqueue(request);
		DebugLog.d("Test_Debug", "Task ID = " + id);
		assertTrue("下载任务建立失败", id > 0);
		sleep(1);
		// 查询本地数据库验证结果
		Cursor cursor = CaseUtils.selectTask(this.getContext(),
				downloadManager, id);
		String descColomn = cursor.getString(cursor
				.getColumnIndex("description"));
		DebugLog.d("Test_Debug", "任务描述 = " + descColomn);
		assertEquals("任务描述错误", descString, descColomn);
		//删除下载任务，清理测试环境
		CaseUtils.deleteTasks(downloadManager, id);
	}
}
