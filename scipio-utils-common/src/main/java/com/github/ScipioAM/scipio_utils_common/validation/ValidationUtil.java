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

    public void setFactory(ValidatorFactory factory) {
        validator.setFactory(factory);
    }

    public void setDefaultExecutor(jakarta.validation.Validator defaultExecutor) {
        validator.setDefaultExecutor(defaultExecutor);
    }

    public void setCustomInvalidHandler(InvalidBeanHandler customInvalidHandler) {
        validator.setCustomInvalidHandler(customInvalidHandler);
    }

    public void setCustomValidHandler(ValidBeanHandler customValidHandler) {
        validator.setCustomValidHandler(customValidHandler);
    }

    public jakarta.validation.Validator getDefaultExecutor() {
        return validator.getDefaultExecutor();
    }

    public jakarta.validation.Validator buildDefaultExecutor() {
        return validator.buildDefaultExecutor();
    }

    //==================================================================================================================

    public static <T> void validateOnce(T obj) throws ValidationException {
        validator.validateOnce(obj);
    }

    public <T> boolean validateOnceIf(T obj) {
        return validator.validateOnceIf(obj);
    }

    public <T> void validate(T obj) throws ValidationException {
        validator.validate(obj);
    }

    public <T> boolean validateIf(T obj) {
        return validator.validateIf(obj);
    }

}
