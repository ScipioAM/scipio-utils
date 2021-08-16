package com.github.ScipioAM.scipio_utils_common.thread;

/**
 *
 * @author Dr. Heinz M. Kabutz
 * @date 2021/8/12
 * author from: [https://www.javaspecialists.eu]
 */
@FunctionalInterface
public interface ThreadLocalChangeListener {

    /**
     * 变化的回调
     * @param mode 变化模式（新增或删除）
     * @param thread 查找的线程
     * @param threadLocal 查找的ThreadLocal对象
     * @param value 变化的值（ThreadLocal的泛型类型）
     */
    void changed(Mode mode, Thread thread, ThreadLocal<?> threadLocal, Object value);

    /**
     * 空白listener实例
     */
    ThreadLocalChangeListener EMPTY = (mode, thread, threadLocal, value) -> {};

    /**
     * 仅打印信息的listener实例
     */
    ThreadLocalChangeListener PRINTER = (mode, thread, threadLocal, value) ->
            System.out.printf("Thread \"%s\" %s ThreadLocal \"%s\" with value \"%s\"%n",
                    thread, mode, threadLocal.getClass(), value);


    /**
     * ThreadLocal值的变化模式
     */
    enum Mode {
        ADDED, REMOVED
    }

}
