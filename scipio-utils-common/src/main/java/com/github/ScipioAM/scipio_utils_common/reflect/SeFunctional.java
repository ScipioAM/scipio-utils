package com.github.ScipioAM.scipio_utils_common.reflect;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Locale;

/**
 * @since 2022/7/8
 */
public interface SeFunctional extends Serializable {

    default SerializedLambda getSerializedLambda() throws Exception {
        Method write = this.getClass().getDeclaredMethod("writeReplace");
        write.setAccessible(true);
        return (SerializedLambda) write.invoke(this);
    }

    default String getImplClass() {
        try {
            return getSerializedLambda().getImplClass();
        } catch (Exception e) {
            return null;
        }
    }

    default String getImplMethodName() {
        try {
            return getSerializedLambda().getImplMethodName();
        } catch (Exception e) {
            return null;
        }
    }

}
