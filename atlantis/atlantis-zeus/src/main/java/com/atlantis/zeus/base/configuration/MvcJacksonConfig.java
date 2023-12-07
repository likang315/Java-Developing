package com.atlantis.zeus.base.configuration;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

/**
 * 自定义 SpringBoot 接口序列化格式
 *
 * @author likang02@corp.netease.com
 * @date 2023/12/7 17:57
 */
@Configuration
public class MvcJacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> {
            builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            // 支持 Java 8 中的日期时间类的序列化和反序列化
            JavaTimeModule javaTimeModule = new JavaTimeModule();
            builder.modules(javaTimeModule);
            builder.modules(new AfterburnerModule());
            // 注册自定义的 LocalDateTime 序列化器
            SimpleModule customModule = new SimpleModule();
            customModule.addSerializer(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            builder.modules(customModule);
            builder.dateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        };
    }
}