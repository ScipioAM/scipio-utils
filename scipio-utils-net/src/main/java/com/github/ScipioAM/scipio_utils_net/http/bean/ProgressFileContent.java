package com.github.ScipioAM.scipio_utils_net.http.bean;

import com.github.ScipioAM.scipio_utils_io.stream.OutputStreamProgress;
import com.github.ScipioAM.scipio_utils_net.http.listener.FileUploadListener;
import com.github.ScipioAM.scipio_utils_net.http.listener.UploadListener;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.FileContent;
import com.google.api.client.util.Preconditions;

import java.io.*;

/**
 * 方便统计进度的{@link FileContent}
 * @author Jacob Moshenko | Alan Scipio
 * @since 1.0.1-p1
 * @date 2021/8/24
 */
public class ProgressFileContent extends AbstractInputStreamContent {

    /** 过滤第一次输出(非真正上传) */
    private boolean isFirstOut = true;

    /** 第几个要上传的文件(0-based) */
    private int fileNo;

    private final File file;

    private OutputStreamProgress outProgress;

    private UploadListener uploadListener;

    /**
     * @param type Content type or {@code null} for none
     * @param file file
     */
    public ProgressFileContent(String type, File file) {
        super(type);
        this.file = Preconditions.checkNotNull(file);
    }

    public long getLength() {
        return file.length();
    }

    public boolean retrySupported() {
        return true;
    }

    @Override
    public InputStream getInputStream() throws FileNotFoundException {
        return new FileInputStream(file);
    }

    /**
     * Returns the file.
     */
    public File getFile() {
        return file;
    }

    @Override
    public ProgressFileContent setType(String type) {
        return (ProgressFileContent) super.setType(type);
    }

    @Override
    public ProgressFileContent setCloseInputStream(boolean closeInputStream) {
        return (ProgressFileContent) super.setCloseInputStream(closeInputStream);
    }

    @Override
    public void writeTo(OutputStream out) throws IOException {
        //准备装饰者(只针对第2次输出(真实上传))
        if(!isFirstOut && uploadListener instanceof FileUploadListener) { //uploadListener为null，结果也是false
            FileUploadListener fListener = (FileUploadListener) uploadListener;
            this.outProgress = new OutputStreamProgress(out) {
                @Override
                public void afterProcess(int processedBytes) {
                    int progress = (int) (100L * this.getProcessedBytes() / file.length());
                    fListener.onSingleUploading(fileNo,file,file.length(),this.getProcessedBytes(),progress);
                }
            };
        }
        else {
            this.outProgress = new OutputStreamProgress(out);
        }
        //执行写入
        super.writeTo(this.outProgress);
        if(isFirstOut) {
            isFirstOut = false;
        }
    }

    /**
     * 获取当前的字节写入进度
     * Progress: 0-100
     */
    public int getProgress() {
        if (outProgress == null) {
            return 0;
        }
        if (file.length() <= 0) {
            return 0;
        }
        long writtenLength = outProgress.getProcessedBytes();
        return (int) (100L * writtenLength / file.length());
    }

    /**
     * 获取已写入的字节数
     */
    public long getWrittenBytes() {
        return (outProgress == null ? 0L : outProgress.getProcessedBytes());
    }

    /**
     * 获取要写入的字节总长度
     */
    public long getContentLength() {
        return file.length();
    }

    public ProgressFileContent setUploadListener(UploadListener uploadListener) {
        this.uploadListener = uploadListener;
        return this;
    }

    public ProgressFileContent setFileNo(int fileNo) {
        this.fileNo = fileNo;
        return this;
    }

    public int getFileNo() {
        return fileNo;
    }
}
