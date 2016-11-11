package com.xunlei.sdk.test.manager;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.junittest.utils.BaseCase;
import com.example.junittest.utils.CaseUtils;
import com.example.junittest.utils.log.DebugLog;

/*
 * getUriForDownloadedFile 获取下载任务的文件位置
 */
public class GetUriForDownloadedFile extends BaseCase {

    // 已下载完成的文件位置
    public void testGetUriForDownloadedFile() {
        printDivideLine();
        // 添加一条下载完成的任务
        Context context = this.getContext();
        long id = CaseUtils.insertSuccessfulTask(context,downloadManager);
        // 查询数据库获取文件位置
        Cursor cursor = CaseUtils.selectTask(context,downloadManager,id);
        String localUri = cursor.getString(cursor.getColumnIndex("hint"));
        DebugLog.d("Test_Debug", "本地Uri = " + localUri);
        // 调用接口获取文件位置
        Uri fileUri = downloadManager.getUriForDownloadedFile(id);
        DebugLog.d("Test_Debug", "接口返回Uri = " + fileUri);
        // 对比验证结果是否一致
        String localString = localUri.split("[.]")[0];
        String fileString = (fileUri.toString()).split("[-]")[0];
        assertEquals("本地Uri错误", localString, fileString);
    }

    // 下载中的任务文件位置
    public void testGetUriForRunningFile() {
        printDivideLine();
        // 建立一个下载任务
        long id = CaseUtils.insertDownloadTask(downloadManager);
        Context context = this.getContext();
        CaseUtils.startActivity(context);
        sleep(5);
        // 调用接口获取文件位置
        Uri fileUri = downloadManager.getUriForDownloadedFile(id);
        DebugLog.d("Test_Debug", "接口返回Uri = " + fileUri);
        // 验证结果应为null
        assertNull("本地Uri错误", fileUri);
        //删除下载任务，清理测试环境
        CaseUtils.deleteTasks(downloadManager, id);
    }

    // 未下载的任务文件位置应为null
    public void testGetUriForPendingFile() {
        printDivideLine();
        // 建立一个任务但不开始下载
        long id = CaseUtils.insertDownloadTask(downloadManager);
        // 调用接口获取文件位置
        Uri fileUri = downloadManager.getUriForDownloadedFile(id);
        DebugLog.d("Test_Debug", "接口返回Uri = " + fileUri);
        // 验证结果应为null
        assertNull("本地Uri错误", fileUri);
        //删除下载任务，清理测试环境
        CaseUtils.deleteTasks(downloadManager, id);
    }

}
