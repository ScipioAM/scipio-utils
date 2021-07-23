package com.github.ScipioAM.scipio_utils_javafx.register;

import com.github.ScipioAM.scipio_utils_crypto.CryptoUtil;
import com.github.ScipioAM.scipio_utils_crypto.mode.SaltLevel;

/**
 * 注册服务
 * @author Alan Min
 * @since 2021/1/13
 */
public class RegisterServiceImpl implements RegisterService {

    private final CryptoUtil cryptoUtil = new CryptoUtil();

    /**
     * 获取原始机器特征码
     * @return 原始机器特征码，例如CPU序列号、主板序列号等
     */
    @Override
    public String getOriginalMachineCode() {
        return MachineCodeUtil.getCpuSerial();
    }

    /**
     * 获取本机的机器码（以原始机器特征码为内容进行信息摘要加密）
     * @return 机器码（已经过信息摘要加密）
     */
    @Override
    public String getLocalMachineCode() {
        return cryptoUtil.sha_256(getOriginalMachineCode(), SaltLevel.NONE);
    }

    /**
     * 获取机器码（从请求码获取）
     * @return 机器码，如果解密出错则返回null
     */
    @Override
    public String getMachineCode(String requestCode) {
        String machineCode = null;
        try {
            machineCode = cryptoUtil.decryptStrAES(requestCode,KEY_REQUEST_CODE);
        }catch (Exception e){
            e.printStackTrace();
        }
        return machineCode;
    }

    //===========================================================

    /**
     * 获取本机的签名 - 用于与激活码里的签名进行比较
     * 签名是以机器码为内容，加盐后信息摘要加密而成
     * @return 激活序列号
     */
    @Override
    public String getLocalSignature() {
        return cryptoUtil.md5( (getLocalMachineCode()+SALT_SIGNATURE), SaltLevel.NONE );
    }

    /**
     * 创建签名
     * @param requestCode 客户端给予的请求码（需要拿出里面的机器码）
     * @return 签名，作为inputSignature与客户端本机签名进行比较，如果请求码解密失败则返回null
     */
    @Override
    public String createSignature(String requestCode) {
        String machineCode = getMachineCode(requestCode);
        if(machineCode==null || "".equals(machineCode)) {
            return null;
        }
        return cryptoUtil.md5( (machineCode+SALT_SIGNATURE), SaltLevel.NONE );
    }

    /**
     * 验证输入的签名是否与本机的一致
     * @param inputSignature 输入的签名（从激活码里解密得出）
     * @return 返回true代表一致，返回false代表不一致
     */
    @Override
    public boolean verifySignature(String inputSignature) {
        if(inputSignature==null || "".equals(inputSignature)) {
            return false;
        }
        return getLocalSignature().equals(inputSignature);
    }

    //===========================================================

    /**
     * 根据机器码获取本机的请求码
     * @return 请求码，如果加密出错则返回null
     */
    @Override
    public String getLocalRequestCode() {
        String requestCode=null;
        try {
            String localMachineCode = getLocalMachineCode();
            requestCode=cryptoUtil.encryptStrAES(localMachineCode,KEY_REQUEST_CODE);
        }catch (Exception e){
            e.printStackTrace();
        }
        return requestCode;
    }

    /**
     * 创建激活码
     * @param requestCode 请求码
     * @param registerMode 注册模式
     * @param data 付费模式附带的数据，如果是时效许可模式就是到期的时间戳，如果是永久许可模式则为null
     * @return 激活码（如果请求码解密失败则返回null）
     */
    @Override
    public String createActiveCode(String requestCode, RegisterMode registerMode, Long data) {
        String signature = createSignature(requestCode);
        if(signature==null || "".equals(signature)) {
            return null;
        }
        StringBuilder activeCode = new StringBuilder();
        switch (registerMode)
        {
            case PERMANENT:
                activeCode.append(signature);
                activeCode.append(SEPARATOR);
                activeCode.append(RegisterMode.PERMANENT.getId());
                break;
            case TIME:
                activeCode.append(signature);
                activeCode.append(SEPARATOR);
                activeCode.append(RegisterMode.TIME.getId());
                activeCode.append(SEPARATOR);
                activeCode.append(data);
                break;
        }

        //加密激活码密文
        String activeCodeStr = null;
        try {
            activeCodeStr = cryptoUtil.encryptStrAES(activeCode.toString(),KEY_ACTIVE_CODE);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return activeCodeStr;
    }

    /**
     * 解析激活码字符串
     * @param activeCodeStr 激活码字符串（密文）
     * @return 解析过的激活码对象，如果解析不对就返回null
     */
    @Override
    public ActiveCode analyzeActiveCode(String activeCodeStr) {
        String codeContent;//激活码明文
        try {
            codeContent = cryptoUtil.decryptStrAES(activeCodeStr,KEY_ACTIVE_CODE);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        //切割激活码明文
        String[] arr = codeContent.split(SEPARATOR);
        if(arr.length<=1) {
            return null;
        }

        ActiveCode codeObj = new ActiveCode();
        codeObj.setPlaintext(codeContent);
        //解析签名
        codeObj.setSignature(arr[0]);
        //解析注册模式
        if(arr[1].equals(RegisterMode.PERMANENT.getId())) {
            codeObj.setMode(RegisterMode.PERMANENT);
        }
        else if(arr[1].equals(RegisterMode.TIME.getId())) {
            codeObj.setMode(RegisterMode.TIME);
            //解析时间戳
            try {
                Long data = Long.parseLong(arr[2]);
                codeObj.setData(data);
            }catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        else {
            return null;
        }

        return codeObj;
    }

}
