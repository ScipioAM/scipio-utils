package com.github.ScipioAM.scipio_utils_common.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

/**
 * 手动JavaBean校验者
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/7
 */
public class Validator {

    private ValidatorFactory factory;
    private jakarta.validation.Validator defaultExecutor;

    private InvalidBeanHandler customInvalidHandler;//自定义校验非法时的处理器
    private ValidBeanHandler customValidHandler;//自定义校验合法时的处理器

    private Validator() {}

    private Validator initFactory() {
        factory = Validation.buildDefaultValidatorFactory();
        return this;
    }

    private Validator initDefaultExecutor() {
        defaultExecutor = factory.getValidator();
        return this;
    }

    public static Validator newInstance() {
        return new Validator()
                .initFactory()
                .initDefaultExecutor();
    }

    public static Validator newInstance(ValidatorFactory validatorFactory) {
        return new Validator()
                .setFactory(validatorFactory)
                .initDefaultExecutor();
    }

    public static Validator newInstance(ValidatorFactory validatorFactory, jakarta.validation.Validator defaultExecutor) {
        return new Validator()
                .setFactory(validatorFactory)
                .setDefaultExecutor(defaultExecutor);
    }

    /**
     * 校验后的处理
     * @param violations 所有非法信息，如果完全合法则
     * @param originalObj 原始要校验的JavaBean
     * @param <T> JavaBean的类型
     * @throws ValidationException JavaBean非法异常
     */
    private <T> void afterValidation(Set<ConstraintViolation<T>> violations, T originalObj, InvalidBeanHandler defaultInvalidHandler) throws ValidationException {
        if(!violations.isEmpty()) { //非法
            if(customInvalidHandler == null) {
                defaultInvalidHandler.handle(violations,originalObj);
            }
            else {
                customInvalidHandler.handle(violations,originalObj);
            }
        }
        else { //合法
            if(customValidHandler != null) {
                customValidHandler.handle(violations,originalObj);
            }
        }
    }

    //==================================================================================================================

    /**
     * 校验JavaBean - 遇到第1个非法的就终止
     * @param obj 要校验的JavaBean
     * @param <T> JavaBean的类型
     * @throws ValidationException 非法的信息
     */
    public <T> void validateOnce(T obj) throws ValidationException {
        Set<ConstraintViolation<T>> violations = defaultExecutor.validate(obj);//校验
        afterValidation(violations,obj,InvalidBeanHandler.ONCE);
    }

    /**
     * 校验JavaBean - 遇到第1个非法的就终止
     * @param obj 要校验的JavaBean
     * @param <T> JavaBean的类型
     * @return 返回true代表校验不通过
     */
    public <T> boolean validateOnceIf(T obj) {
        Set<ConstraintViolation<T>> violations = defaultExecutor.validate(obj);//校验
        boolean invalid = false;
        try {
            afterValidation(violations,obj,InvalidBeanHandler.ONCE);
        }catch (ValidationException ve) {
            invalid = true;
            ve.printStackTrace();
        }
        return invalid;
    }

    /**
     * 校验JavaBean - 所有非法的情况都了解一遍
     * @param obj 要校验的JavaBean
     * @param <T> JavaBean的类型
     * @throws ValidationException 非法的信息
     */
    public <T> void validate(T obj) throws ValidationException {
        Set<ConstraintViolation<T>> violations = defaultExecutor.validate(obj);//校验
        afterValidation(violations,obj,InvalidBeanHandler.ALL);
    }

    /**
     * 校验JavaBean - 所有非法的情况都了解一遍
     * @param obj 要校验的JavaBean
     * @param <T> JavaBean的类型
     * @return 返回true代表校验不通过
     */
    public <T> boolean validateIf(T obj) {
        Set<ConstraintViolation<T>> violations = defaultExecutor.validate(obj);//校验
        boolean invalid = false;
        try {
            afterValidation(violations,obj,InvalidBeanHandler.ALL);
        }catch (ValidationException ve) {
            invalid = true;
            ve.printStackTrace();
        }
        return invalid;
    }

    //==================================================================================================================

    public Validator setFactory(ValidatorFactory factory) {
        this.factory = factory;
        return this;
    }

    public Validator setDefaultExecutor(jakarta.validation.Validator defaultExecutor) {
        this.defaultExecutor = defaultExecutor;
        return this;
    }

    public Validator setCustomInvalidHandler(InvalidBeanHandler customInvalidHandler) {
        this.customInvalidHandler = customInvalidHandler;
        return this;
    }

    public Validator setCustomValidHandler(ValidBeanHandler customValidHandler) {
        this.customValidHandler = customValidHandler;
        return this;
    }

    public jakarta.validation.Validator getDefaultExecutor() {
        return defaultExecutor;
    }

    public jakarta.validation.Validator buildDefaultExecutor() {
        defaultExecutor = factory.getValidator();
        return defaultExecutor;
    }
    
}
