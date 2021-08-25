package com.github.ScipioAM.scipio_utils_common.thread;

import java.lang.ref.Reference;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.function.BiConsumer;

import static com.github.ScipioAM.scipio_utils_common.thread.ThreadLocalChangeListener.Mode.ADDED;
import static com.github.ScipioAM.scipio_utils_common.thread.ThreadLocalChangeListener.Mode.REMOVED;

/**
 * ThreadLocal清理工具类（保证不会内存泄漏）                                <br/><br/>
 *
 *  <li>类初始化时，准备好反射要用的工具字段（field和class）</li>
 *  <li>类被构造时，保存一份当前线程的ThreadLocal.ThreadLocalMap的拷贝</li>
 *  <li>cleanup为调用入口（也可以用try-with-resources的close方法调起cleanup）</li>
 *
 * @author Dr. Heinz M. Kabutz
 * @since 1.0.0-alpha01
 * @date 2021/8/12
 * author from: [https://www.javaspecialists.eu]
 * article from(repeated): [https://www.cnblogs.com/princessd8251/articles/5186286.html]
 */
public class ThreadLocalCleaner implements AutoCloseable{

    private final ThreadLocalChangeListener listener;

    public ThreadLocalCleaner() {
        this(ThreadLocalChangeListener.EMPTY);
    }

    public ThreadLocalCleaner(ThreadLocalChangeListener listener) {
        this.listener = listener;
        saveOldThreadLocals();
    }

    //======================================== ↓↓↓ 调用入口 ↓↓↓ ========================================

    /**
     * try-with-resources的关闭调用
     */
    @Override
    public void close() {
        cleanup();
    }

    /**
     * 清除当前线程的threadLocals字段的值       <br/>
     *
     * <p>"threadLocals字段的值"是指：ThreadLocalMap的table字段（Entry型），也就是存放实际值的地方</p>
     */
    public void cleanup() {
        //先检查当前线程的所有ThreadLocal值的变化
        diff(threadLocalsField, copyOfThreadLocals.get());
        diff(inheritableThreadLocalsField, copyOfInheritableThreadLocals.get());
        //还原ThreadLocal值为类被构造时的状态（期间新增的ThreadLocal的值都会被解除引用）
        restoreOldThreadLocals();
    }

    /**
     * 清除指定线程的threadLocals字段
     * @param thread 指定的线程
     */
    public static void cleanup(Thread thread) {
        try {
            threadLocalsField.set(thread, null);
            inheritableThreadLocalsField.set(thread, null);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Could not clear thread locals: " + e);
        }
    }

    /**
     * 遍历指定线程的threadLocals字段
     * @param thread 指定的线程
     * @param consumer 每次遍历的具体执行
     */
    public static void forEach(Thread thread, BiConsumer<ThreadLocal<?>, Object> consumer) {
        forEach(thread, threadLocalsField, consumer);
        forEach(thread, inheritableThreadLocalsField, consumer);
    }

    /**
     * 遍历当前线程的threadLocals字段
     * @param consumer 每次遍历的具体执行
     */
    public static void forEach(BiConsumer<ThreadLocal<?>, Object> consumer) {
        Thread thread = Thread.currentThread();
        forEach(thread, threadLocalsField, consumer);
        forEach(thread, inheritableThreadLocalsField, consumer);
    }

    //======================================== ↓↓↓ 内部使用的工具字段 ↓↓↓ ========================================

    //当前线程的ThreadLocalMap.Entry的引用的拷贝
    private static final ThreadLocal<Reference<?>[]> copyOfThreadLocals = new ThreadLocal<>();

    //当前线程的内部类ThreadLocalMap.Entry的引用的拷贝
    private static final ThreadLocal<Reference<?>[]> copyOfInheritableThreadLocals = new ThreadLocal<>();

    //======================================== ↓↓↓ 准备好反射用的工具字段 ↓↓↓ ========================================

    //反射用的工具变量
    private static final Field threadLocalsField; //Thread.class里的ThreadLocal.ThreadLocalMap型字段

    private static final Field inheritableThreadLocalsField;
    private static final Class<?> threadLocalMapClass;
    private static final Field tableField; //Thread.class里的ThreadLocal.ThreadLocalMap里的Entry型字段
    private static final Class<?> threadLocalMapEntryClass;

    private static final Field threadLocalEntryValueField;//Thread.class里的ThreadLocal.ThreadLocalMap里的Entry里的value字段

    static {
        try {
            threadLocalsField = field(Thread.class, "threadLocals");
            inheritableThreadLocalsField =
                    field(Thread.class, "inheritableThreadLocals");

            threadLocalMapClass =
                    inner(ThreadLocal.class, "ThreadLocalMap");

            tableField = field(threadLocalMapClass, "table");
            threadLocalMapEntryClass =
                    inner(threadLocalMapClass, "Entry");

            threadLocalEntryValueField =
                    field(threadLocalMapEntryClass, "value");
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException(
                    "Could not locate threadLocals field in Thread.  " +
                            "Will not be able to clear thread locals: " + e);
        }
    }

    //根据name获取目标类里的字段
    private static Field field(Class<?> c, String name) throws NoSuchFieldException {
        Field field = c.getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }

    //根据name获取目标类里的内部类
    private static Class<?> inner(Class<?> clazz, String name) {
        for (Class<?> c : clazz.getDeclaredClasses()) {
            if (c.getSimpleName().equals(name)) {
                return c;
            }
        }
        throw new IllegalStateException("Could not find inner class " + name + " in " + clazz);
    }

    //======================================== ↓↓↓ 核心内部方法diff - 及其附属工具方法 ↓↓↓ ========================================

    /**
     * 检查当前线程的ThreadLocal值是否有变化
     *
     * ThreadLocal值是指：ThreadLocalMap的table字段（Entry型）
     *
     * @param field 指定的threadLocals字段
     * @param backup table备份
     */
    private void diff(Field field, Reference<?>[] backup) {
        try {
            Thread thread = Thread.currentThread();
            Object threadLocals = field.get(thread);
            if (threadLocals == null) {
                if (backup != null) {
                    for (Reference<?> reference : backup) {
                        changed(thread, reference, REMOVED);
                    }
                }
                return;
            }

            Reference<?>[] current = (Reference<?>[]) tableField.get(threadLocals);
            if (backup == null) {
                for (Reference<?> reference : current) {
                    changed(thread, reference, ADDED);
                }
            }
            else {
                // nested loop - both arrays *should* be relatively small
                //当前引用与备份引用比较，在备份引用里找不到，则说明该当前引用是新增的
                next:
                for (Reference<?> curRef : current) {
                    if (curRef != null) {
                        if (curRef.get() == copyOfThreadLocals || curRef.get() == copyOfInheritableThreadLocals) {
                            continue;
                        }
                        for (Reference<?> backupRef : backup) {
                            if (curRef == backupRef) {
                                continue next;
                            }
                        }
                        // could not find it in backup - added
                        changed(thread, curRef, ADDED);
                    }
                }
                //备份引用与当前引用比较，在当前引用里找不到，则说明该备份引用是已被删除的
                next:
                for (Reference<?> backupRef : backup) {
                    for (Reference<?> curRef : current) {
                        if (curRef == backupRef) {
                            continue next;
                        }
                    }
                    // could not find it in current - removed
                    changed(thread, backupRef, REMOVED);
                }
            }
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Access denied", e);
        }
    }

    //调用监听器的changed方法
    private void changed(Thread thread, Reference<?> reference, ThreadLocalChangeListener.Mode mode)
            throws IllegalAccessException {
        listener.changed(mode,
                thread, (ThreadLocal<?>) reference.get(),
                threadLocalEntryValueField.get(reference));
    }

    //======================================== ↓↓↓ 核心内部方法forEach ↓↓↓ ========================================

    private static void forEach(
            Thread thread, Field field,
            BiConsumer<ThreadLocal<?>, Object> consumer)
    {
        try {
            Object threadLocals = field.get(thread);
            if (threadLocals != null) {
                //
                Reference<?>[] table = (Reference<?>[]) tableField.get(threadLocals);
                for (Reference<?> ref : table) {
                    if (ref != null) {
                        ThreadLocal<?> key = (ThreadLocal<?>) ref.get();
                        if (key != null) {
                            Object value = threadLocalEntryValueField.get(ref);
                            consumer.accept(key, value);
                        }
                    }
                }//end of for
            }//end of outside if
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    //======================================== ↓↓↓ 核心内部方法saveOldThreadLocals - 及其附属工具方法 ↓↓↓ ========================================

    /**
     * 保存类被构造时，当前线程的threadLocals状态
     */
    private static void saveOldThreadLocals() {
        copyOfThreadLocals.set(copy(threadLocalsField));
        copyOfInheritableThreadLocals.set(copy(inheritableThreadLocalsField));
    }

    private static Reference<?>[] copy(Field field) {
        try {
            Thread thread = Thread.currentThread();
            Object threadLocals = field.get(thread);
            if (threadLocals == null) {
                return null;
            }
            Reference<?>[] table = (Reference<?>[]) tableField.get(threadLocals);
            return Arrays.copyOf(table, table.length);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Access denied", e);
        }
    }

    //======================================== ↓↓↓ 核心内部方法restoreOldThreadLocals - 及其附属工具方法 ↓↓↓ ========================================

    /**
     * 重置当前线程threadLocals为，类被构造时的状态（期间新增的ThreadLocal值都会被解除引用，从而使其能够被回收）
     * <p>注意：未直接将ThreadLocal本身置为null，而是把其里面的值全部解除引用，ThreadLocal本身回收起来好说</p>
     */
    private static void restoreOldThreadLocals() {
        try {
            restore(threadLocalsField, copyOfThreadLocals.get());
            restore(inheritableThreadLocalsField, copyOfInheritableThreadLocals.get());
        } finally {
            copyOfThreadLocals.remove();
            copyOfInheritableThreadLocals.remove();
        }
    }

    private static void restore(Field field, Object value) {
        try {
            Thread thread = Thread.currentThread();
            if (value == null) {
                field.set(thread, null);
            }
            else {
                tableField.set(field.get(thread), value);
            }
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Access denied", e);
        }
    }

}
