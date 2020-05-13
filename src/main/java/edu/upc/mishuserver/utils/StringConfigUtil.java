package edu.upc.mishuserver.utils;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.upc.mishuserver.model.StringSetting;
import edu.upc.mishuserver.repositories.StringSettingRepositoriy;
import lombok.extern.slf4j.Slf4j;

/**
 * StringConfigUtil
 */
@Component
@Slf4j
public class StringConfigUtil {
    @Autowired
    private StringSettingRepositoriy settingRepositoriy;

    private static StringConfigUtil stringConfigUtil; 

    public static Map<String,String> configMap;

    @PostConstruct
    public void init(){
        stringConfigUtil = this;
    }

    public static String getConfig(String key,String defaultValue) {
        if(configMap.containsKey(key)){
            return configMap.get(key);
        }
        return defaultValue;
    }

    public static String getConfig(String key) {
        return getConfig(key, "");
    }

    public static void writeConfig(String key,String value) {
        log.debug("写入配置{},{}",key,value);
        StringSetting stringSetting=stringConfigUtil.settingRepositoriy.findByItem(key);
        if(stringSetting==null){
            stringSetting=StringSetting.builder().item(key).value(value).build();
        }else{
            stringSetting.setValue(value);
        }
        stringConfigUtil.settingRepositoriy.save(stringSetting);
        if(configMap.containsKey(key)){
            configMap.remove(key);
        }
        configMap.put(key, value);
    }
}