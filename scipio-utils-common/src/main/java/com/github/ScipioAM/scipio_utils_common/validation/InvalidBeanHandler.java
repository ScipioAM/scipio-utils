package com.github.ScipioAM.scipio_utils_common.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;

import java.util.Set;

/**
 * JavaBean检验非法时的处理器
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/7
 */
@FunctionalInterface
public interface InvalidBeanHandler {

    /**
     * 非法时的处理
     * @param violations 所有非法情况
     * @param originalObj 原始检查的JavaBean
     * @param <T> JavaBean的类型
     * @throws ValidationException JavaBean非法异常
     */
    <T> void handle(Set<ConstraintViolation<T>> violations, T originalObj) throws ValidationException;

    //==================================================================================================================

    /**
     * 碰到第一个校验不通过的就结束
     */
    InvalidBeanHandler ONCE = new InvalidBeanHandler() {
        @Override
        public <T> void handle(Set<ConstraintViolation<T>> violations, T originalObj) throws ValidationException {
            for(ConstraintViolation<?> violation : violations) {
                throw new ValidationException(violation.getMessage());
            }
        }
    };

    /**
     * 所有校验不通过的都走一遍
     */
    InvalidBeanHandler ALL = new InvalidBeanHandler() {
        @Override
        public <T> void handle(Set<ConstraintViolation<T>> violations, T originalObj) throws ValidationException {
            StringBuilder msgBuilder = new StringBuilder();
            for(ConstraintViolation<?> violation : violations) {
                msgBuilder.append(violation.getMessage()).append(";");
            }
            String msg = msgBuilder.length() <=0 ? "bean invalid, but no message left" : msgBuilder.deleteCharAt(msgBuilder.length() - 1).toString();
            throw new ValidationException(msg);
        }
    };

}
