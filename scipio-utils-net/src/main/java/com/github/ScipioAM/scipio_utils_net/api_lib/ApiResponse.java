package com.github.ScipioAM.scipio_utils_net.api_lib;

import java.io.InputStream;
import java.util.List;

/**
 * 调用API的响应结果
 * @author Alan Scipio
 * @since 2021/7/14
 */
public interface ApiResponse {

    /**
     * 获取成功与否的结果
     */
    boolean isSuccess();

    /**
     * 获取响应码(默认是HTTP响应码，除非子类改写)
     */
    int getResponseCode();

    /**
     * 设置响应码(默认是HTTP响应码，除非子类改写)
     */
    void setResponseCode(int responseCode);

    /**
     * 获取响应体内容
     */
    String getOriginData();

    /**
     * 设置响应体内容
     */
    void setOriginData(String originData);

    /**
     * 设置响应字节数据
     * @param in 响应的输入流，需要从中读取字节数据
     */
    default void setResponseBytes(InputStream in) {

    }

    /**
     * 读取响应的字节数据
     * @return 响应的字节数据
     */
    default List<Byte> getResponseBytes() {
        return null;
    }

}
