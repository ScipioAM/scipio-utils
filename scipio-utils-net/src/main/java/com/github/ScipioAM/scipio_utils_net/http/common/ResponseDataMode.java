package com.github.ScipioAM.scipio_utils_net.http.common;

/**
 * Class: ResponseDataMode
 * Description: 接收数据方式
 * @author Alan Min
 * Create Date: 2020/9/24
 */
public enum ResponseDataMode {

    DEFAULT, //默认只获取返回的数据
    NONE, //不需要响应体数据
    DOWNLOAD_FILE,//下载文件
    STREAM_ONLY //直接返回InputStream对象

}
