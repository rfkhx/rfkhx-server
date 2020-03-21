package edu.upc.mishuserver.init;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import edu.upc.mishuserver.dto.StringSetting;
import edu.upc.mishuserver.repositories.StringSettingRepositoriy;
import edu.upc.mishuserver.utils.StringConfigUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * InitRandom
 */
@Component
@Order(2)
@Slf4j
public class InitStringConfig implements ApplicationRunner {

    @Autowired
    private StringSettingRepositoriy stringSettingRepositoriy;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.debug("开始读入系统配置信息。");
        StringConfigUtil.configMap=new HashMap<>();
        stringSettingRepositoriy.findAll().forEach(new Consumer<StringSetting>() {

			@Override
			public void accept(StringSetting t) {
                StringConfigUtil.configMap.put(t.getItem(), t.getValue());
			}
        });
    }
}