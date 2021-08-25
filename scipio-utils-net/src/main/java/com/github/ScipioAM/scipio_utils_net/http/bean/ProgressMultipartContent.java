package com.github.ScipioAM.scipio_utils_net.http.bean;

import com.github.ScipioAM.scipio_utils_io.stream.OutputStreamProgress;
import com.github.ScipioAM.scipio_utils_net.http.listener.UploadListener;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpMediaType;
import com.google.api.client.http.MultipartContent;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

/**
 * 方便统计进度的{@link MultipartContent}
 * @author Alan Scipio
 * @since 1.0.1-p1
 * @date 2021/8/24
 */
public class ProgressMultipartContent extends MultipartContent {

    /** 过滤第一次输出(非真正上传) */
    private boolean isFirstOut = true;

    private OutputStreamProgress outProgress;

    private long contentLength;

    private UploadListener uploadListener;

    public ProgressMultipartContent() { }

    public ProgressMultipartContent(String boundary) {
        super(boundary);
    }

    @Override
    public void writeTo(OutputStream out) throws IOException {
        outProgress = new OutputStreamProgress(out);
        //准备装饰者
        if(!isFirstOut && uploadListener == null) {
            outProgress = new OutputStreamProgress(out);
        }
        else {
            outProgress = new OutputStreamProgress(out) {
                @Override
                public void afterProcess(int processedBytes) {
                    int progress = (this.getProcessedBytes() > contentLength) ? 100 : (int) (100L * this.getProcessedBytes() / contentLength);
                    uploadListener.onUploading(contentLength,this.getProcessedBytes(),progress);
                }
            };
        }
        //执行写入
        super.writeTo(outProgress);
        if(isFirstOut) {
            isFirstOut = false;
        }
    }

    @Override
    public ProgressMultipartContent setMediaType(HttpMediaType mediaType) {
        super.setMediaType(mediaType);
        return this;
    }

    @Override
    public ProgressMultipartContent addPart(Part part) {
        super.addPart(part);
        return this;
    }

    @Override
    public ProgressMultipartContent setParts(Collection<Part> parts) {
        super.setParts(parts);
        return this;
    }

    @Override
    public ProgressMultipartContent setContentParts(Collection<? extends HttpContent> contentParts) {
        super.setContentParts(contentParts);
        return this;
    }

    @Override
    public ProgressMultipartContent setBoundary(String boundary) {
        super.setBoundary(boundary);
        return this;
    }

    /**
     * 获取当前的字节写入进度
     * Progress: 0-100
     */
    public int getProgress() {
        if (outProgress == null) {
            return 0;
        }
        if (contentLength <= 0L) {
            return 0;
        }
        long writtenLength = outProgress.getProcessedBytes();
        return (int) (100L * writtenLength / contentLength);
    }

    /**
     * 获取已写入的字节数
     */
    public long getWrittenBytes() {
        return (outProgress == null ? 0L : outProgress.getProcessedBytes());
    }

    /**
     * 要写入的字节总长度
     */
    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public ProgressMultipartContent setUploadListener(UploadListener uploadListener) {
        this.uploadListener = uploadListener;
        return this;
    }

}
