package com.github.ScipioAM.scipio_utils_common.validation;

import jakarta.validation.ValidationException;
import jakarta.validation.ValidatorFactory;

/**
 * 手动JavaBean校验工具类(单例模式)
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/7
 */
public class ValidationUtil {

    private final static Validator validator;

    static {
        validator = Validator.newInstance();
    }

    //==================================================================================================================

    public static void setFactory(ValidatorFactory factory) {
        validator.setFactory(factory);
    }

    public static void setDefaultExecutor(jakarta.validation.Validator defaultExecutor) {
        validator.setDefaultExecutor(defaultExecutor);
    }

    public static void setCustomInvalidHandler(InvalidBeanHandler customInvalidHandler) {
        validator.setCustomInvalidHandler(customInvalidHandler);
    }

    public static void setCustomValidHandler(ValidBeanHandler customValidHandler) {
        validator.setCustomValidHandler(customValidHandler);
    }

    public static jakarta.validation.Validator getDefaultExecutor() {
        return validator.getDefaultExecutor();
    }

    public static jakarta.validation.Validator buildDefaultExecutor() {
        return validator.buildDefaultExecutor();
    }

    //==================================================================================================================

    public static <T> void validateOnce(T obj) throws ValidationException {
        validator.validateOnce(obj);
    }

    public static <T> boolean validateOnceIf(T obj) {
        return validator.validateOnceIf(obj);
    }

    public static <T> void validate(T obj) throws ValidationException {
        validator.validate(obj);
    }

    public static <T> boolean validateIf(T obj) {
        return validator.validateIf(obj);
    }

}
