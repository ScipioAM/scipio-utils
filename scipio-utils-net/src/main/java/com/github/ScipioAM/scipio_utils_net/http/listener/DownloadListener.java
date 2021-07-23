package com.github.ScipioAM.scipio_utils_net.http.listener;

import java.io.IOException;

/**
 * Interface: DownloadListener
 * Description: 文件下载时的监听器
 * Author: Alan Min
 * Create Date: 2020/9/25
 */
public interface DownloadListener {

    /**
     * 下载时
     * @param downloadedPercent 百分比进度(0-1)
     */
    void onDownloading(double downloadedPercent);

    /**
     * 下载完成时
     * @param isSuccess 是否下载成功，为true代表成功
     * @param e 如果下载失败，则此项不为null
     */
    void onFinished(boolean isSuccess, IOException e);

}
