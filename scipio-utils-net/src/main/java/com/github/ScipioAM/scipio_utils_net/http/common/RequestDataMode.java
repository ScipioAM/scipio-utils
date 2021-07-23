package com.github.ScipioAM.scipio_utils_net.http.common;

/**
 * Enum:RequestDataMode
 * Description: 数据发送方式
 * Author: Alan Min
 * Create Date:2020/9/24
 */
public enum RequestDataMode {

    DEFAULT,//默认传参方式(比如x=1&y=2...)
    JSON,//直接输出json数据
    XML//直接输出xml数据

}
