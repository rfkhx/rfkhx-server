package edu.upc.mishuserver.utils;

import java.util.Map;

/**
 * StringConfigUtil
 */
public class StringConfigUtil {

    public static Map<String,String> configMap;

    public static String getConfig(String key,String defaultValue) {
        if(configMap.containsKey(key)){
            return configMap.get(key);
        }
        return defaultValue;
    }

    public static String getConfig(String key) {
        return getConfig(key, "");
    }
}