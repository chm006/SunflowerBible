package com.xunlei.sdk.test.manager;

import android.content.Context;
import android.database.Cursor;

import com.example.junittest.utils.BaseCase;
import com.example.junittest.utils.CaseUtils;
import com.example.junittest.utils.log.DebugLog;

/*
 * pauseDownload 暂停下载任务
 */
public class PauseDownload extends BaseCase {

    // 暂停下载中的任务
    public void testPauseRunningDownload() {
        printDivideLine();
        // 建立下载任务
        long id = CaseUtils.insertDownloadTask(downloadManager);
        Context context = this.getContext();
        CaseUtils.startActivity(context);
        sleep(3);
        // 查询数据库获取此时下载状态并验证
        int downloadStatus = CaseUtils.selectDownloadStatus(context,
                downloadManager, id);
        assertEquals("下载状态异常", 192, downloadStatus);
        // 调用接口
        int result = downloadManager.pauseDownload(id);
        assertEquals("暂停失败", 1, result);
        sleep(1);
        // 查询数据库验证此时下载状态
        int pauseStatus = CaseUtils.selectDownloadStatus(context,
                downloadManager, id);
        assertEquals("暂停状态异常", 193, pauseStatus);
        // 查询数据库获取此时下载速度并验证应为0
        int speed = CaseUtils.selectDownloadSpeed(context, downloadManager, id);
        assertEquals("下载速度异常", 0, speed);
        //删除下载任务，清理测试环境
        CaseUtils.deleteTasks(downloadManager, id);
    }

    // 暂停未开始下载的任务
    public void testPausePendingDownload() {
        printDivideLine();
        // 建立一个下载任务
        long id = CaseUtils.insertDownloadTask(downloadManager);
        // 调用接口
        int result = downloadManager.pauseDownload(id);
        assertEquals("暂停失败", 1, result);
        sleep(1);
        // 查询数据库验证此时下载状态
        int pauseStatus = CaseUtils.selectDownloadStatus(this.getContext(),
                downloadManager, id);
        assertEquals("暂停状态异常", 193, pauseStatus);
        // 查询数据库获取此时下载速度并验证应为0
        int speed = CaseUtils.selectDownloadSpeed(this.getContext(),
                downloadManager, id);
        assertEquals("下载速度异常", 0, speed);
        //删除下载任务，清理测试环境
        CaseUtils.deleteTasks(downloadManager, id);
    }

    // 重复暂停一个已暂停的任务
    public void testRepeatPausedDownload() {
        printDivideLine();
        // 建立一个下载任务
        long id = CaseUtils.insertDownloadTask(downloadManager);
        Context context = this.getContext();
        CaseUtils.startActivity(context);
        sleep(3);
        // 调用接口暂停该任务
        int pauseResult = downloadManager.pauseDownload(id);
        assertEquals("暂停失败", 1, pauseResult);
        sleep(1);
        // 查询数据库验证此时下载状态
        int pauseStatus = CaseUtils.selectDownloadStatus(this.getContext(),
                downloadManager, id);
        assertEquals("暂停状态异常", 193, pauseStatus);
        // 再次调用接口暂停该任务(第二次返回结果应为0)
        int pauseResult2 = downloadManager.pauseDownload(id);
        assertEquals("重复暂停异常", 0, pauseResult2);
        sleep(1);
        // 再次验证下载状态
        int pauseStatus2 = CaseUtils.selectDownloadStatus(this.getContext(),
                downloadManager, id);
        assertEquals("暂停状态异常", 193, pauseStatus2);
        //删除下载任务，清理测试环境
        CaseUtils.deleteTasks(downloadManager, id);
    }

    // 暂停一个已下载成功的任务
    public void testPauseSuccessfulDownload() {
        printDivideLine();
        // 查询本地数据库获取已下载完成的任务
        long id;
        Cursor cursor = CaseUtils.selectTaskByStatus(this.getContext(),
                downloadManager, 200);
        if (cursor.getCount() > 0) {
            // 获取其中一条任务
            cursor.moveToLast();
            id = cursor.getLong(cursor.getColumnIndex("_id"));
            DebugLog.d("Test_Debug", "Task ID = " + id);
        } else {
            id = CaseUtils.insertSuccessfulTask(this.getContext(),downloadManager);
        }
        // 调用接口
        int pauseResult = downloadManager.pauseDownload(id);
        assertEquals("暂停失败", 0, pauseResult);
        sleep(1);
        // 查询数据库验证此时下载状态
        int pauseStatus = CaseUtils.selectDownloadStatus(this.getContext(),
                downloadManager, id);
        assertEquals("任务状态异常", 200, pauseStatus);
        //删除下载任务，清理测试环境
        CaseUtils.deleteTasks(downloadManager, id);
    }

    // 暂停多个任务
    public void testPauseMultiDownload() {
        printDivideLine();
        // 建立下载任务
        long id1 = CaseUtils.insertDownloadTask(downloadManager);
        long id2 = CaseUtils.insertDownloadTask(downloadManager);
        Context context = this.getContext();
        CaseUtils.startActivity(context);
        sleep(3);
        // 查询数据库获取此时下载状态并验证
        int downloadStatus1 = CaseUtils.selectDownloadStatus(context,
                downloadManager, id1);
        assertEquals("下载状态异常", 192, downloadStatus1);
        int downloadStatus2 = CaseUtils.selectDownloadStatus(context,
                downloadManager, id2);
        assertEquals("下载状态异常", 192, downloadStatus2);
        // 调用接口
        int result = downloadManager.pauseDownload(id1, id2);
        assertEquals("暂停失败", 2, result);
        sleep(1);
        // 查询数据库验证此时下载状态
        int pauseStatus1 = CaseUtils.selectDownloadStatus(context,
                downloadManager, id1);
        assertEquals("暂停状态异常", 193, pauseStatus1);
        int pauseStatus2 = CaseUtils.selectDownloadStatus(context,
                downloadManager, id2);
        assertEquals("暂停状态异常", 193, pauseStatus2);
        // 查询数据库获取此时下载速度并验证应为0
        int speed1 = CaseUtils.selectDownloadSpeed(context, downloadManager,
                id1);
        assertEquals("下载速度异常", 0, speed1);
        int speed2 = CaseUtils.selectDownloadSpeed(context, downloadManager,
                id2);
        assertEquals("下载速度异常", 0, speed2);
        //删除下载任务，清理测试环境
        CaseUtils.deleteTasks(downloadManager, id1, id2);
    }
}
