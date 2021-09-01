package com.github.ScipioAM.scipio_utils_common.os.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Windows cmd 执行后响应的信息
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/1
 */
public class WinExecMessage {

    /**
     * 普通输入流里读取到的执行结果
     */
    private List<String> normalResultList;

    /**
     * 错误输入流里读取到的执行结果
     */
    private List<String> errorResultList;

    public WinExecMessage check() {
        if(normalResultList == null && errorResultList == null) {
            return null;
        }
        else {
            return this;
        }
    }

    public List<String> getNormalResultList() {
        return normalResultList;
    }

    public String getNormalResult() {
        if(normalResultList == null) {
            return null;
        }
        return getResultStr(normalResultList);
    }

    public void setNormalResultList(List<String> normalResultList) {
        this.normalResultList = normalResultList;
    }

    public WinExecMessage addNormalResultList(String resultLine) {
        if(normalResultList == null) {
            normalResultList = new ArrayList<>();
        }
        normalResultList.add(resultLine);
        return this;
    }

    public List<String> getErrorResultList() {
        return errorResultList;
    }

    public String getErrorResult() {
        if(errorResultList == null) {
            return null;
        }
        return getResultStr(errorResultList);
    }

    public void setErrorResultList(List<String> errorResultList) {
        this.errorResultList = errorResultList;
    }

    public WinExecMessage addErrorResultList(String resultLine) {
        if(errorResultList == null) {
            errorResultList = new ArrayList<>();
        }
        errorResultList.add(resultLine);
        return this;
    }

    private String getResultStr(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for(String line : list) {
            sb.append(line);
        }
        return sb.toString();
    }
}
