package com.github.ScipioAM.scipio_utils_crypto.listener;

/**
 * Interface: OnProcessingListener
 * Description: 流加密或解密进行时的监听器
 * Author: Alan Min
 * Create Date: 2020/9/29
 */
public interface OnProcessingListener {

    /**
     * 进行时
     * @param readCount 读取并处理的字节总数
     */
    void onProcess(long readCount);

}
