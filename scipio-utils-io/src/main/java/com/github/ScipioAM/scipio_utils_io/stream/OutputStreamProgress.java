package com.github.ScipioAM.scipio_utils_io.stream;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.LongAdder;

/**
 * 输出流装饰者 - 统计输出进度
 * <p>(该类线程安全)</p>
 * @author Alan Scipio
 * @since 1.0.1-p1
 * @date 2021/8/24
 */
public class OutputStreamProgress extends OutputStream implements ProgressIOStream {

    private final OutputStream outputStream;
    private final LongAdder totalBytesWritten = new LongAdder();//已读取的字节计数

    public OutputStreamProgress(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public long getProcessedBytes() {
        return totalBytesWritten.longValue();
    }

    @Override
    public void write(int b) throws IOException {
        beforeProcess(1);
        outputStream.write(b);
        totalBytesWritten.increment();
        afterProcess(1);
    }

    @Override
    public void write(byte[] b) throws IOException {
        beforeProcess(b.length);
        outputStream.write(b);
        totalBytesWritten.add(b.length);
        afterProcess(b.length);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        beforeProcess(b.length);
        outputStream.write(b, off, len);
        totalBytesWritten.add(len);
        afterProcess(len);
    }

    @Override
    public void flush() throws IOException {
        outputStream.flush();
    }

    @Override
    public void close() throws IOException {
        totalBytesWritten.reset();
        outputStream.close();
    }

}
