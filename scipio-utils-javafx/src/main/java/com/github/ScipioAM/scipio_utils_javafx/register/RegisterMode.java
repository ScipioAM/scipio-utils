package com.github.ScipioAM.scipio_utils_javafx.register;

/**
 * 注册模式
 * @author Alan Min
 * @since 2021/1/13
 */
public enum RegisterMode {

    PERMANENT("0","永久许可"),
    TIME("1","时效许可");

    private final String id;
    private final String name;

    RegisterMode(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
