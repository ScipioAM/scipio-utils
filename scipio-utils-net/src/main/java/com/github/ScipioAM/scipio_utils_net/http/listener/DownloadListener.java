package com.github.ScipioAM.scipio_utils_net.http.listener;

import java.io.File;
import java.io.IOException;

/**
 * 文件下载时的监听器
 * @author Alan Scipio
 * @since 1.0.0
 * @date 2020/9/25
 */
public interface DownloadListener {

    /**
     * 下载时
     * @param totalLength 总字节数
     * @param readLength 已下载字节数
     * @param progress 下载进度(0-100)(Content-Length未知时，永远为0)
     */
    default void onDownloading(long totalLength, long readLength, int progress) {
        System.out.println("Download progress：" + progress + "%  bytes: " + readLength + "/" + totalLength);
    }

    /**
     * 下载完成时
     * @param isSuccess 是否下载成功，为true代表成功
     * @param outputFile 输出下载的文件
     * @param e 如果下载失败，则此项不为null
     */
    default void onFinished(boolean isSuccess, File outputFile, IOException e) {
        System.out.println("Download finished, Success: " + isSuccess + ", File: " + outputFile.getAbsolutePath());
    }

    /**
     * 空实现
     */
    DownloadListener EMPTY_IMPL = new DownloadListener() {
        @Override
        public void onDownloading(long totalLength, long readLength, int downloadedPercent) {
            DownloadListener.super.onDownloading(totalLength, readLength, downloadedPercent);
        }
        @Override
        public void onFinished(boolean isSuccess, File outputFile, IOException e) {
            DownloadListener.super.onFinished(isSuccess, outputFile, e);
        }
    };

}
