package com.github.ScipioAM.scipio_utils_io.stream;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.LongAdder;

/**
 * 输入流装饰者 - 统计输入进度
 * <p>(该类线程安全)</p>
 * @author Alan Scipio
 * @since 1.0.1-p1
 * @date 2021/8/24
 */
public class InputStreamProgress extends InputStream implements ProgressIOStream {

    private final InputStream in;
    private final LongAdder totalBytesRead = new LongAdder();//已写入的字节计数

    public InputStreamProgress(InputStream in) {
        this.in = in;
    }

    @Override
    public long getProcessedBytes() {
        return totalBytesRead.longValue();
    }

    @Override
    public int read() throws IOException {
        beforeProcess(1);
        int readByte = in.read();//读取的一个字节本身
        totalBytesRead.increment();//计数加1
        afterProcess(1);
        return readByte;
    }

    @Override
    public int read(byte[] b) throws IOException {
        beforeProcess(b.length);
        int readCount = in.read(b);
        totalBytesRead.add(b.length);
        afterProcess(b.length);
        return readCount;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        beforeProcess(b.length);
        int readCount = in.read(b, off, len);
        totalBytesRead.add(len);
        afterProcess(len);
        return readCount;
    }

    @Override
    public long skip(long n) throws IOException {
        return in.skip(n);
    }

    @Override
    public int available() throws IOException {
        return in.available();
    }

    @Override
    public void close() throws IOException {
        totalBytesRead.reset();
        in.close();
    }

    @Override
    public synchronized void mark(int readlimit) {
        in.mark(readlimit);
    }

    @Override
    public synchronized void reset() throws IOException {
        totalBytesRead.reset();
        in.reset();
    }

    @Override
    public boolean markSupported() {
        return in.markSupported();
    }

}
