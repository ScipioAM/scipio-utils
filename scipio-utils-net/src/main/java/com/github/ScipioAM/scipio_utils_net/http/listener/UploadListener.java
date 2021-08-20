package com.github.ScipioAM.scipio_utils_net.http.listener;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Interface: UploadListener
 * Description: 文件上传时的监听器
 * Author: Alan Min
 * Create Date: 2019/9/3
 */
public interface UploadListener {

    /**
     * 上传时（文件层面）
     * @param totalFileCount 总文件数
     * @param uploadedFileCount 已上传的文件数
     */
    default void onFilesUploading(int totalFileCount, int uploadedFileCount) {
        System.out.println("File upload progress: " + uploadedFileCount + "/" + totalFileCount + " files");
    }

    /**
     * 上传时（单个文件的字节层面）
     * @param fileNo 第几个文件
     * @param uploadFile 上传文件
     * @param totalBytes 总字节数
     * @param uploadedBytes 已上传的字节数
     * @param uploadedPercent 上传百分比（0-1）
     */
    default void onSingleUploading(int fileNo, File uploadFile, long totalBytes, long uploadedBytes, BigDecimal uploadedPercent) {
        BigDecimal percent = uploadedPercent.multiply(new BigDecimal(100));//1-100的百分比
        System.out.println("[" + fileNo + "] File(" + uploadFile.getName() + ") uploading: " + percent +"%");
    }

    /**
     * 上传完成时
     */
    default void onCompleted(Map<String,File> uploadFiles) {
        System.out.println("File upload completed, total upload count: " + uploadFiles.size());
    }

    /**
     * 上传出错时
     * @param e IO异常对象
     */
    default void onError(IOException e) {
        e.printStackTrace();
    }

}
