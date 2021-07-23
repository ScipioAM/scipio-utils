package com.github.ScipioAM.scipio_utils_net.catcher.bean;

import java.util.*;

/**
 * 网页抓取结果集
 * @author alan scipio
 * @since 2021/6/9
 */
public class CatchResult {

    /**
     * 抓取的字符串结果list <br/>
     */
    private List<String> resultStrList;

    /**
     * 抓取的对象结果list <br/>
     */
    private List<Object> resultObjList;

    /**
     * 抓取的结果map <br/>
     */
    private Map<String,Object> resultMap;

    /**
     * 抓取的结果对象 <br/>
     */
    private Object resultObj;

    public CatchResult() {}

    public CatchResult(List<String> resultStrList) {
        this.resultStrList = resultStrList;
    }

    public CatchResult(Map<String,Object> resultMap) {
        this.resultMap = resultMap;
    }

    public CatchResult(Object resultObj) {
        this.resultObj = resultObj;
    }

    public List<String> getResultStrList() {
        return resultStrList;
    }

    public void setResultStrList(List<String> resultStrList) {
        this.resultStrList = resultStrList;
    }

    /**
     * 设置结果字符串list <br/>
     * 注：会覆盖之前的list
     */
    public void setResultStrList(String... resultArr) {
        if(resultArr==null) {
            throw new IllegalArgumentException("argument is null");
        }
        resultStrList = new ArrayList<>();
        Collections.addAll(resultStrList, resultArr);
    }

    public List<Object> getResultObjList() {
        return resultObjList;
    }

    public void setResultObjList(List<Object> resultObjList) {
        this.resultObjList = resultObjList;
    }

    /**
     * 设置结果对象list <br/>
     * 注：会覆盖之前的list
     */
    public void setResultObjList(String... resultArr) {
        if(resultArr==null) {
            throw new IllegalArgumentException("argument is null");
        }
        resultObjList = new ArrayList<>();
        Collections.addAll(resultStrList, resultArr);
    }

    public Map<String, Object> getResultMap() {
        return resultMap;
    }

    public void setResultMap(Map<String, Object> resultMap) {
        this.resultMap = resultMap;
    }

    /**
     * 添加结果键值对到结果map <br/>
     * 注：形式为追加
     */
    public void addResultMap(String key, Object value) {
        if(key==null) {
            throw new IllegalArgumentException("argument [key] is null");
        }
        if(resultMap==null) {
            resultMap = new HashMap<>();
        }
        resultMap.put(key,value);
    }

    /**
     * 添加结果键值对到结果map <br/>
     * 注：形式为覆盖
     */
    public void setResultMap(String key, Object value) {
        if(key==null) {
            throw new IllegalArgumentException("argument [key] is null");
        }
        resultMap = new HashMap<>();
        resultMap.put(key,value);
    }

    public Object getResultObj() {
        return resultObj;
    }

    public void setResultObj(Object resultObj) {
        this.resultObj = resultObj;
    }

    @Override
    public String toString() {
        return "CatchResult{" +
                "resultStrList=" + resultStrList +
                ", resultObjList=" + resultObjList +
                ", resultMap=" + resultMap +
                ", resultObj=" + resultObj +
                '}';
    }
}
