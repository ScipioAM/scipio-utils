package com.github.ScipioAM.scipio_utils_net.http.listener;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * 上传监听器
 * @author Alan Scipio
 * @since 1.0.1-p1
 * @date 2021/8/24
 */
public interface UploadListener {

    /**
     * 上传中
     * @param totalBytes 总字节数
     * @param uploadedBytes 已上传字节数
     * @param progress 进度百分比(0-100)
     */
    default void onUploading(long totalBytes, long uploadedBytes, int progress) {
        System.out.println("Upload progress: " + progress + "%" + ", bytes: " + uploadedBytes + "/" + totalBytes);
    }

    /**
     * 上传完成时
     */
    default void onCompleted(Map<String, File> uploadFiles) {
        System.out.println("Upload completed, total upload count: " + uploadFiles.size());
    }

    /**
     * 上传出错时
     * @param e IO异常对象
     */
    default void onError(IOException e) {
        e.printStackTrace();
    }

    /**
     * 空实现
     */
    UploadListener EMPTY_IMPL = new UploadListener() {
        @Override
        public void onUploading(long totalBytes, long uploadedBytes, int progress) {
            UploadListener.super.onUploading(totalBytes, uploadedBytes, progress);
        }
        @Override
        public void onCompleted(Map<String, File> uploadFiles) {
            UploadListener.super.onCompleted(uploadFiles);
        }
        @Override
        public void onError(IOException e) {
            UploadListener.super.onError(e);
        }
    };

}
