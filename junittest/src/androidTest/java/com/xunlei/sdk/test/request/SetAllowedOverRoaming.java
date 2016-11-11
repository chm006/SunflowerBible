package com.xunlei.sdk.test.request;

import android.database.Cursor;
import android.net.Uri;

import com.example.junittest.utils.ReflectUtils;
import com.xunlei.download.DownloadManager.Request;
import com.example.junittest.utils.BaseCase;
import com.example.junittest.utils.CaseUtils;
import com.example.junittest.utils.log.DebugLog;

/*
 * setAllowedOverRoaming 设置是否允许数据漫游时下载
 */
public class SetAllowedOverRoaming extends BaseCase {
	private Request request;

	// 设置为不允许漫游
	public void testSetNotAllowedOverRoamingFalse() {
		printDivideLine();
		// 添加测试Request
		request = new Request(
				Uri.parse("http://cache.iruan.cn/201412/201412181_uc.apk"));
		request.setDestinationInExternalPublicDir(DOWNLOADPATH,
				"201412181_uc.apk");
		// 调用接口
		request.setAllowedOverRoaming(false);
		// 通过反射验证设置成功
		Object mRoamingAllowed = ReflectUtils.getPrivateAttr(request,
				"mRoamingAllowed");
		assertEquals("设置属性失败", false, mRoamingAllowed);
		// 建立测试任务
		long id = downloadManager.enqueue(request);
		DebugLog.d("Test_Debug", "Task ID = " + id);
		assertTrue("下载任务建立失败", id > 0);
		// 查询本地数据库验证结果
		Cursor cursor = CaseUtils.selectTask(this.getContext(),
				downloadManager, id);
		int allowRoaming = cursor
				.getInt(cursor.getColumnIndex("allow_roaming"));
		assertEquals("任务属性异常", 0, allowRoaming);
		//删除下载任务，清理测试环境
		CaseUtils.deleteTasks(downloadManager, id);
	}

	// 设置为允许漫游
	public void testSetAllowedOverRoamingTrue() {
		printDivideLine();
		// 添加测试Request
		request = new Request(
				Uri.parse("http://cache.iruan.cn/201412/201412181_uc.apk"));
		request.setDestinationInExternalPublicDir(DOWNLOADPATH,
				"201412181_uc.apk");
		// 调用接口
		request.setAllowedOverRoaming(true);
		// 通过反射验证设置成功
		Object mRoamingAllowed = ReflectUtils.getPrivateAttr(request,
				"mRoamingAllowed");
		assertEquals("设置属性失败", true, mRoamingAllowed);
		// 建立测试任务
		long id = downloadManager.enqueue(request);
		DebugLog.d("Test_Debug", "Task ID = " + id);
		assertTrue("下载任务建立失败", id > 0);
		// 查询本地数据库验证结果
		Cursor cursor = CaseUtils.selectTask(this.getContext(),
				downloadManager, id);
		int allowRoaming = cursor
				.getInt(cursor.getColumnIndex("allow_roaming"));
		assertEquals("任务属性异常", 1, allowRoaming);
		//删除下载任务，清理测试环境
		CaseUtils.deleteTasks(downloadManager, id);
	}
}
