package com.github.ScipioAM.scipio_utils_io.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Jackson工具类
 * @author Alan Scipio
 * @since 1.0.2
 * @date 2021/10/8
 */
public class JacksonUtil {

    /**
     * 将java对象序列化成Json
     */
    public static String toJson(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }

    /**
     * 将Json反序列化成java对象
     */
    public static <T> T fromJson(String jsonStr,Class<T> clazz) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonStr, clazz);
    }

    /**
     * 根据key获取value
     */
    public static String getValueByKey(String jsonStr, String key) throws IOException {
        ObjectMapper mapper=new ObjectMapper();
        JsonNode tree = mapper.readTree(jsonStr);
        return tree.findValue(key).asText();
    }

    /**
     * 根据多个key获取value
     */
    public static Map<String,String> getValueByKeys(String jsonStr, String... keys) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode tree = mapper.readTree(jsonStr);
        Map<String, String> result = new HashMap<>();
        for (String k : keys) {
            result.put(k, tree.findValue(k).asText());
        }
        return result;
    }

}
