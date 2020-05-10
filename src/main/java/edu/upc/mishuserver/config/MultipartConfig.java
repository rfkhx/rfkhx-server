/*
 * @Website: https://ntutn.top
 * @Date: 2019-09-29 19:57:03
 * @LastEditors: zero
 * @LastEditTime: 2019-11-29 14:35:42
 * @Description: 配置文件上传临时目录，防止被系统自动删除配置文件目录导致异常。
 * @FilePath: /zeroblog/src/main/java/top/ntutn/zeroblog/config/MultipartConfig.java
 */
package edu.upc.mishuserver.config;

import java.io.File;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class MultipartConfig {

    /**
     * 文件上传临时路径
     */
    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        String location = System.getProperty("user.dir") + "/data/tmp";
        File tmpFile = new File(location);
        if (!tmpFile.exists()) {
            log.warn("临时文件目录{}不存在，将创建目录。", location);
            tmpFile.mkdirs();
        }
        factory.setLocation(location);
        return factory.createMultipartConfig();
    }
}
