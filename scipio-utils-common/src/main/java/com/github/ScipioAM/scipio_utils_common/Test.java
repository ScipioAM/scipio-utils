package com.github.ScipioAM.scipio_utils_common;

/**
 * 测试相关工具类
 * @author alan scipio
 * @since 2021/7/23
 */
public class Test {

    private Test() {}

    /**
     * 专门用于测试时中断
     * @param isNeedBreak 是否需要中断，true代表需要中断
     */
    @SuppressWarnings({"divzero", "NumericOverflow"})
    public static void breakHere(boolean isNeedBreak) {
        if(isNeedBreak) {
            int i = 1 / 0;
        }
    }

    public static void breakHere() {
        breakHere(true);
    }

}
