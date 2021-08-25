package com.github.ScipioAM.scipio_utils_net.http.listener;

import com.github.ScipioAM.scipio_utils_net.http.AbstractHttpBase;

import java.io.File;

/**
 * 文件上传时的监听器
 * <p>(仅在{@link AbstractHttpBase}的{@code doMultipartOutput}方法中调用)</p>
 * @author Alan Min
 * @since 1.0.0
 * @date 2019/9/3
 */
public interface FileUploadListener extends UploadListener {

    /**
     * 上传时(文件层面)
     * <p>(仅在{@link AbstractHttpBase}的{@code doMultipartOutput}方法中调用)</p>
     * @param totalFileCount 总文件数
     * @param uploadedFileCount 已上传的文件数
     */
    default void onFilesUploading(int totalFileCount, int uploadedFileCount) {
        System.out.println("File upload progress: " + uploadedFileCount + "/" + totalFileCount + " files");
    }

    /**
     * 上传时(单个文件的字节层面)
     * @param fileNo 第几个文件(0-based)
     * @param uploadFile 上传文件
     * @param totalBytes 总字节数
     * @param uploadedBytes 已上传的字节数
     * @param progress 上传百分比(0-100)
     */
    default void onSingleUploading(int fileNo, File uploadFile, long totalBytes, long uploadedBytes, int progress) {
        System.out.println("[" + fileNo + "] File(" + uploadFile.getName() + ") uploading: " + progress + "%,  bytes: " + uploadedBytes + "/" + totalBytes);
    }

    /**
     * 空实现
     */
    FileUploadListener EMPTY_IMPL = new FileUploadListener() {
        @Override
        public void onFilesUploading(int totalFileCount, int uploadedFileCount) {
            FileUploadListener.super.onFilesUploading(totalFileCount, uploadedFileCount);
        }
        @Override
        public void onSingleUploading(int fileNo, File uploadFile, long totalBytes, long uploadedBytes, int progress) {
            FileUploadListener.super.onSingleUploading(fileNo, uploadFile, totalBytes, uploadedBytes, progress);
        }
    };

}
