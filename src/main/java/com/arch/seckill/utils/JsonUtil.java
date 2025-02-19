package com.arch.seckill.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @Description: Json
 * @Author:yaomy
 * @DateTime: 2024/4/23 22:39
 **/
public class JsonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();
    /*
    * 将对象转换成json字符串
    * */
    public static String objectToJsonStr(Object obj){
        try{
            return objectMapper.writeValueAsString(obj);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return null;
    }
    /*
    * 将字符串转换为对象
    * */
    public static <T> T jsonStrToObject(String jsonStr,Class<T> clazz){
        try{
            return objectMapper.readValue(jsonStr.getBytes("UTF-8"),clazz);
        }catch (JsonParseException e){
            e.printStackTrace();
        }catch (JsonMappingException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
