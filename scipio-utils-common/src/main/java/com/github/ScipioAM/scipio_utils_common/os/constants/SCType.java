package com.github.ScipioAM.scipio_utils_common.os.constants;

/**
 * sc命令的类型
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/6
 */
public enum SCType {

    /** 查询服务当前状态 */
    QUERY,
    /** 启动服务 */
    START,
    /** 停止服务 */
    STOP,
    /** 创建服务 */
    CREATE,
    /** 删除服务 */
    DELETE,
    /** 构建任意名称的sid */
    SHOW_SID,
    /** 设置指定服务的描述 */
    DESCRIPTION

}
