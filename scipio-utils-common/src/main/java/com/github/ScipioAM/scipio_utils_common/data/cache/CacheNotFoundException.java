package com.github.ScipioAM.scipio_utils_common.data.cache;

/**
 * @author Alan Scipio
 * @since 1.0.2
 * @date 2021/9/29
 */
public class CacheNotFoundException extends RuntimeException{
    public CacheNotFoundException() {
    }

    public CacheNotFoundException(String message) {
        super(message);
    }

    public CacheNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CacheNotFoundException(Throwable cause) {
        super(cause);
    }
}
