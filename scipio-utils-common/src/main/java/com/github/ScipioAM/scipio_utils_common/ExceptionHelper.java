package com.github.ScipioAM.scipio_utils_common;

/**
 * 异常信息帮助类
 * @author Alan Scipio
 * @since 2021/4/13
 */
public class ExceptionHelper {

    /**
     * 构造完整的异常堆栈信息
     * 包括第一行的异常类、信息和后续诸多行的异常位置
     */
    public static String buildErrorMessage(Exception ex) {
        String result;
        String stackTrace = getStackTraceString(ex);
        String exceptionType = ex.toString();
        String exceptionMessage = ex.getMessage();

        result = String.format("%s : %s \r\n %s", exceptionType, exceptionMessage, stackTrace);
        return result;
    }

    /**
     * 获取异常堆栈信息
     * @return 诸多行的异常位置
     */
    public static String getStackTraceString(Throwable ex){//(Exception ex) {
        StackTraceElement[] traceElements = ex.getStackTrace();
        StringBuilder traceBuilder = new StringBuilder();
        if (traceElements != null && traceElements.length > 0) {
            for (StackTraceElement traceElement : traceElements) {
                traceBuilder.append("\tat ").append(traceElement.toString());
                traceBuilder.append("\n");
            }
        }
        return traceBuilder.toString();
    }

}
