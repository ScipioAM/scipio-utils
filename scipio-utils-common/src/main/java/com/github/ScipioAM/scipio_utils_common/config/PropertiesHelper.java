package com.github.ScipioAM.scipio_utils_common.config;

import java.io.*;
import java.util.Map;
import java.util.Properties;

/**
 * 配置文件工具类（.properties文件）
 * @author Alan Scipio
 * @since 2020/8/14
 */
public class PropertiesHelper {

    private File configFile;

    public PropertiesHelper() {}

    public PropertiesHelper(String filePath) {
        this.configFile = new File(filePath);
    }

    public PropertiesHelper(File configFile) {
        this.configFile = configFile;
    }

    /**
     * 保存配置到文件
     * @param data 要保存的数据（键值对）
     * @param comments 配置文件抬头的注释
     * @throws IOException 创建file对象失败，properties对象载入文件失败，properties对象保存失败
     */
    public void saveConfig(Map<String, String> data, String comments) throws IOException {
        if(configFile==null) {
            return;
        }
        if(!configFile.exists()){
            if(!configFile.createNewFile()){
                throw new IOException("Create config file failed, the file already exists: "+configFile.getPath());
            }
        }
        Properties properties=new Properties();
        properties.load(new FileInputStream(configFile));
        for(Map.Entry<String,String> entry : data.entrySet()){
            properties.setProperty(entry.getKey(),entry.getValue());
        }
        properties.store(new FileOutputStream(configFile),comments);
    }

    /**
     * 从文件对象中读取配置文件
     * @return 返回读取到的properties对象，如果配置文件不存在则返回null
     * @throws IOException 读取文件时失败
     */
    public Properties getConfig() throws IOException {
        if(configFile==null) {
            throw new IllegalArgumentException("Did not set configFile arg");
        }
        if(!configFile.exists()){
            return null;
        }

        Properties properties=new Properties();
        properties.load(new FileInputStream(configFile));
        return properties;
    }

    /**
     * 从文件对象中读取配置文件
     * @return 返回读取到的properties对象，如果配置文件不存在则返回null
     * @throws IOException 读取文件时失败
     */
    public Properties loadConfigFromFile() throws IOException {
        if(configFile==null) {
            throw new IllegalArgumentException("Did not set configFile arg");
        }
        if(!configFile.exists()){
            return null;
        }
        FileInputStream fis = new FileInputStream(configFile);
        return loadConfigFromStream(fis);
    }

    /**
     * 从输入流中读取配置文件
     * @param stream 配置文件的输入流，不能在该方法里getResourceAsStream，而要在当前工程下getResourceAsStream
     * @return 返回读取到的properties对象
     */
    public Properties loadConfigFromStream(InputStream stream) throws IOException {
        Properties properties=new Properties();
        properties.load(stream);
        return properties;
    }

    /**
     * 读取配置文件，如果不存在就创建
     * @param defaultData 不存在然后创建时写入的值（为null或size为0则不创建）
     * @param comments 不存在然后创建时写入的注释（可为null）
     * @return 返回读取或创建的properties对象，如果文件不存在且defaultData为null则返回null
     * @throws IOException 创建file对象失败，properties对象载入文件失败，properties对象保存失败
     */
    public Properties getConfigOrCreate(Map<String, String> defaultData, String comments) throws IOException {
        if(configFile==null) {
            throw new IllegalArgumentException("Did not set configFile arg");
        }
        Properties properties=null;
        if(!configFile.exists()){
            if(defaultData!=null && defaultData.size()>0){
                properties=new Properties();
                if(!configFile.createNewFile()){
                    throw new IOException("Create config file failed, the file already exists: "+configFile.getPath());
                }
                properties.load(new FileInputStream(configFile));
                for(Map.Entry<String,String> entry : defaultData.entrySet()){
                    properties.setProperty(entry.getKey(),entry.getValue());
                }
                properties.store(new FileOutputStream(configFile),comments);
            }
        }
        else{
            properties=new Properties();
            properties.load(new FileInputStream(configFile));
        }
        return properties;
    }

    public void setConfigFile(File configFile) {
        this.configFile = configFile;
    }

    public void setConfigFile(String configFilePath) {
        this.configFile = new File(configFilePath);
    }

}
