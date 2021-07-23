package com.github.ScipioAM.scipio_utils_javafx.register;

/**
 * 注册服务的接口
 * @author Alan Min
 * @since 2021/1/13
 */
public interface RegisterService {

    String SEPARATOR = "@";//激活码明文的分隔符

    String KEY_REQUEST_CODE = "glopkj781@730==COM/QI9lzc23ES";//请求码的密钥
    String KEY_ACTIVE_CODE = "wE%efr$fv67@#15RHWq4T==JD83ap896r";//激活码的密钥
    String SALT_SIGNATURE = "rew32gdgz@g&@d83785RG$$WGS";//签名的盐

    //===========================================================

    /**
     * 获取原始机器特征码
     * @return 原始机器特征码，例如CPU序列号、主板序列号等
     */
    String getOriginalMachineCode();

    /**
     * 获取本机的机器码（以原始机器特征码为内容进行信息摘要加密）
     * @return 机器码（已经过信息摘要加密）
     */
    String getLocalMachineCode();

    /**
     * 获取机器码（从请求码获取）
     * @return 机器码，如果解密出错则返回null
     */
    String getMachineCode(String requestCode);

    //===========================================================

    /**
     * 获取本机的签名 - 用于与激活码里的签名进行比较
     * 签名是以机器码为内容，加盐后信息摘要加密而成
     * @return 激活序列号
     */
    String getLocalSignature();

    /**
     * 创建签名
     * @param machineCode 客户端给予的机器码（从请求码里获得）
     * @return 签名，作为inputSignature与客户端本机签名进行比较
     */
    String createSignature(String machineCode);

    /**
     * 验证输入的签名是否与本机的一致
     * @param inputSignature 输入的签名（从激活码里解密得出）
     * @return 返回true代表一致，返回false代表不一致
     */
    boolean verifySignature(String inputSignature);

    //===========================================================

    /**
     * 根据机器码获取本机的请求码
     * @return 请求码，如果加密出错则返回null
     */
    String getLocalRequestCode();

    /**
     * 创建激活码
     * @param requestCode 请求码
     * @param registerMode 注册模式
     * @param data 付费模式附带的数据，如果是时效许可模式就是到期的时间戳，如果是永久许可模式则为null
     * @return 激活码
     * 激活码格式：
     * 永久模式：[签名]@[注册模式ID]
     * 时效模式[签名]@[注册模式ID]@[到期日子的时间戳]
     */
    String createActiveCode(String requestCode, RegisterMode registerMode, Long data);

    /**
     * 解析激活码字符串
     * @param activeCodeStr 激活码字符串
     * @return 解析过的激活码对象
     */
    ActiveCode analyzeActiveCode(String activeCodeStr);

}
