package com.github.ScipioAM.scipio_utils_doc.csv.callback;

/**
 * @author Alan Scipio
 * @since 1.0.8
 * @date 2021/11/8
 */
@FunctionalInterface
public interface RowHandler {

    /**
     * 每行的回调处理
     * @param line 每行塑化剂
     * @param rowIndex 行下标(0-based)
     * @return true代表继续执行，false代表中断执行
     */
    boolean handle(String line, int rowIndex);

}
