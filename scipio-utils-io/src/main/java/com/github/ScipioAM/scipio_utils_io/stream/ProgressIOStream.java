package com.github.ScipioAM.scipio_utils_io.stream;

/**
 * 输入输出流装饰者
 * @author Alan Scipio
 * @since 1.0.1-p1
 * @date 2021/8/24
 */
public interface ProgressIOStream {

    /**
     * IO操作之前
     * @param readyBytes 要处理的字节数
     *                   <p>注意：在IO方法(byte[] b, int off, int len)里会与after的参数不一样</p>
     */
    default void beforeProcess(int readyBytes) {}

    /**
     * IO操作之后
     * @param processedBytes 已操作的字节数
     */
    default void afterProcess(int processedBytes) {}

    /**
     * 已处理的字节总数
     */
    long getProcessedBytes();

}
