package com.github.ScipioAM.scipio_utils_common.snowflake;

import java.util.HashMap;
import java.util.Map;

/**
 * 雪花算法下的ID生成器
 *
 * @author Alan Scipio
 * @since 2022/7/15
 */
public class SnowFlakeIdGenerator {

    //默认id生成器（不指定机器id和数据中心id）
    private static final String DEFAULT_SEQ = "ds";
    private static final Map<String, Sequence> SEQUENCE_MAP = new HashMap<>();

    private SnowFlakeIdGenerator() {
    }

    /**
     * 雪花算法下，生成ID
     */
    public static long nextId() {
        Sequence sequence = SEQUENCE_MAP.computeIfAbsent(DEFAULT_SEQ, key -> new Sequence());
        return sequence.nextId();
    }

    /**
     * 雪花算法下，生成ID
     *
     * @param workerId     机器ID
     * @param datacenterId 数据中心ID
     */
    public static long nextId(long workerId, long datacenterId) {
        Sequence sequence = SEQUENCE_MAP.computeIfAbsent(workerId + "-" + datacenterId, key -> new Sequence(workerId, datacenterId));
        return sequence.nextId();
    }

}
