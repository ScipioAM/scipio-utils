package com.github.ScipioAM.scipio_utils_io.parser;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * json差异检查，目前未完成
 * @author Alan Scipio
 * @since 1.0.2
 * @date 2021/10/8
 */
@Deprecated
public class JsonDiffCheck {

    public void checkType(File jsonFile, Class<?> targetCheckType, JsonParser parser) throws IOException{
        String jsonContent = readFromFile(jsonFile);
        checkType(jsonContent,targetCheckType,parser);
    }

    public void checkType(String json, Class<?> targetCheckType, JsonParser parser) {

    }

    //==================================================================================================================

    public static void parseTargetType(Class<?> targetCheckType, Map<String, Object> targetMap) {
        Field[] fields = targetCheckType.getDeclaredFields();
        for(Field field : fields) {
            Class<?> fieldType = field.getType();
            System.out.println("isSynthetic:" + fieldType.isSynthetic());
            if(fieldType.isPrimitive() || fieldType.isArray() || fieldType.isEnum() || fieldType == String.class) {
                targetMap.put(field.getName(), field);
            }
            else {
                System.out.println("Do recursive call");
//                parseTargetType(fieldType,targetMap);//有问题，像Boolean这种包装类就进此else了
            }
        }
    }

    /**
     * 从文件中读取数据
     * @param file 要读取的文件对象
     * @return 文件中的字符串数据
     * @throws IOException 读取失败
     */
    private String readFromFile(File file) throws IOException {
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                //读取时过滤空格和\r \n
                String s = line.replace(" ","").replace("\n","").replace("\r","");
                content.append(s);
            }
            return content.toString();
        }
    }

    //==================================================================================================================

    @FunctionalInterface
    public interface JsonParser {

        Map<String, Object> parseJson(String json) throws Exception;

        @SuppressWarnings("unchecked")
        JsonParser GSON = json -> {
            Gson gson = new Gson();
            return gson.fromJson(json, Map.class);
        };
    }

}
