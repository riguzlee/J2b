package com.riguz.j2b.util;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * json操作工具类
 * 
 * @author peiyu
 */
public final class JsonUtil {

    /**
     * 默认json格式化方式
     */
    public static final SerializerFeature[] DEFAULT_FORMAT = { SerializerFeature.WriteDateUseDateFormat,
                                                           SerializerFeature.WriteEnumUsingToString,
                                                           SerializerFeature.WriteNonStringKeyAsString,
                                                           SerializerFeature.QuoteFieldNames,
                                                           SerializerFeature.SkipTransientField,
                                                           SerializerFeature.SortField, SerializerFeature.PrettyFormat };

    private JsonUtil() {
    }

    /**
     * 从json获取指定key的字符串
     * 
     * @param json
     *            json字符串
     * @param key
     *            字符串的key
     * @return 指定key的值
     */
    public static Object getStringFromJSONObject(final String jsonString, final String key) {
        if (jsonString == null)
            return null;
        return JSON.parseObject(jsonString).getString(key);
    }

    /**
     * 将字符串转换成JSON字符串
     * 
     * @param jsonString
     *            json字符串
     * @return 转换成的json对象
     */
    public static JSONObject getJSONFromString(final String jsonString) {
        if (jsonString == null) {
            return new JSONObject();
        }
        return JSON.parseObject(jsonString);
    }

    /**
     * 将json字符串，转换成指定java bean
     * 
     * @param jsonStr
     *            json串对象
     * @param beanClass
     *            指定的bean
     * @param <T>
     *            任意bean的类型
     * @return 转换后的java bean对象
     */
    public static <T> T toBean(String jsonString, Class<T> beanClass) {
        if (jsonString == null)
            return null;
        JSONObject jo = JSON.parseObject(jsonString);
        jo.put(JSON.DEFAULT_TYPE_KEY, beanClass.getName());
        return JSON.parseObject(jo.toJSONString(), beanClass);
    }

    /**
     * @param obj
     *            需要转换的java bean
     * @param <T>
     *            入参对象类型泛型
     * @return 对应的json字符串
     */
    public static <T> String toJson(T obj) {
        if (obj == null)
            return null;
        return JSON.toJSONString(obj, DEFAULT_FORMAT);
    }

    /**
     * 通过Map生成一个json字符串
     * 
     * @param map
     *            需要转换的map
     * @return json串
     */
    public static String toJson(Map<String, Object> map) {
        if (map == null)
            return null;
        return JSON.toJSONString(map, DEFAULT_FORMAT);
    }

    /**
     * 美化传入的json,使得该json字符串容易查看
     * 
     * @param jsonString
     *            需要处理的json串
     * @return 美化后的json串
     */
    public static String prettyFormatJson(String jsonString) {
        if (jsonString == null)
            return null;
        return JSON.toJSONString(getJSONFromString(jsonString), true);
    }

    /**
     * 将传入的json字符串转换成Map
     * 
     * @param jsonString
     *            需要处理的json串
     * @return 对应的map
     */
    public static Map<String, Object> toMap(String jsonString) {
        if (jsonString == null)
            return null;
        return getJSONFromString(jsonString);
    }

}
