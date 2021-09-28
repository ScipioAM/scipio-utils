package com.github.ScipioAM.scipio_utils_common.data.cache;

/**
 * @author Alan Scipio
 * @since 1.0.2
 * @date 2021/9/28
 */
public class EasyCacheException extends RuntimeException{
    public EasyCacheException() {
    }

    public EasyCacheException(String message) {
        super(message);
    }

    public EasyCacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public EasyCacheException(Throwable cause) {
        super(cause);
    }
}
