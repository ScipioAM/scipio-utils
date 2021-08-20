package com.github.ScipioAM.scipio_utils_net.http.listener;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Interface: DownloadListener
 * Description: 文件下载时的监听器
 * Author: Alan Min
 * Create Date: 2020/9/25
 */
public interface DownloadListener {

    /**
     * 下载时
     * @param totalLength 总字节数
     * @param readLength 已下载字节数
     * @param downloadedPercent 下载进度（0-1的小数）（readLength / totalLength）
     */
    default void onDownloading(long totalLength, long readLength, BigDecimal downloadedPercent) {
        BigDecimal percent = downloadedPercent.multiply(new BigDecimal(100)); //转为1-100的百分比
        System.out.println("Download progress："+String.format("%.2f",(percent.doubleValue()))+"%");
    }

    /**
     * 下载完成时
     * @param isSuccess 是否下载成功，为true代表成功
     * @param outputFile 输出下载的文件
     * @param e 如果下载失败，则此项不为null
     */
    default void onFinished(boolean isSuccess, File outputFile, IOException e) {
        System.out.println("Download finished, File: " + outputFile.getAbsolutePath());
    }

}
