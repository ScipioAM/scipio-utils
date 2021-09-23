package com.github.ScipioAM.scipio_utils_common.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;

import java.util.Set;

/**
 * JavaBean检验合法时的处理器
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/7
 */
@FunctionalInterface
public interface ValidBeanHandler {

    /**
     * 合法时的处理
     * @param violations 所有非法情况
     * @param originalObj 原始检查的JavaBean
     * @param <T> JavaBean的类型
     * @throws ValidationException JavaBean非法异常
     */
    <T> void handle(Set<ConstraintViolation<T>> violations, T originalObj);

}
