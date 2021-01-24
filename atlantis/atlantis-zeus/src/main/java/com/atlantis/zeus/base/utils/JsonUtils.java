package com.atlantis.zeus.base.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;

/**
 * Json 工具类
 *
 * @author kangkang.li@qunar.com
 * @date 2021-01-24 18:39
 */
@Slf4j
@UtilityClass
public class JsonUtils {

    @Getter
    private final ObjectMapper om;

    private final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    static {
        om = JsonMapper.builder()
                .enable(JsonParser.Feature.ALLOW_COMMENTS)
                .enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)
                .enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES)
                .enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS)
                // 反序列化时忽略Json中存在但对象中不存在的属性
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                // 忽略为 null 的属性
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .defaultDateFormat(new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS))
                // @JsonFormat将用到这个时区
                .defaultTimeZone(TimeZone.getDefault())
                .addModule(new AfterburnerModule())
                .addModule(new GuavaModule())
                // 增加自定义序列化类
                .addModule(new JavaTimeModule())
                .build();
    }

    /**
     * 反序列化
     *
     * @param jsonStr
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> Optional<T> readValue(String jsonStr, Class<T> clazz) {
        if (Objects.isNull(jsonStr) || Objects.isNull(clazz)) {
            return Optional.empty();
        }

        try {
            return Optional.of(om.readValue(jsonStr, clazz));
        } catch (IOException e) {
            log.error("JSON字符串反序列化失败: jsonStr = {}, class = {}", jsonStr, clazz, e);
            return Optional.empty();
        }
    }

    /**
     * 将异常情况封装成RunTimeException抛出
     *
     * @param jsonStr
     * @param clazz
     * @param <T>
     * @return
     */
    @SneakyThrows
    public <T> T readValueUnchecked(String jsonStr, Class<T> clazz) {
        return om.readValue(jsonStr, clazz);
    }

    /**
     * 反序列化复杂类型
     *
     * @param jsonStr
     * @param typeReference
     * @param <T>
     * @return
     */
    public <T> Optional<T> readValue(String jsonStr, TypeReference<T> typeReference) {
        if (Objects.isNull(jsonStr) || Objects.isNull(typeReference)) {
            return Optional.empty();
        }

        try {
            return Optional.of(om.readValue(jsonStr, typeReference));
        } catch (IOException e) {
            log.error("JSON字符串反序列化失败: jsonStr = {}, typeReference = {}", jsonStr, typeReference.getType(), e);
            return Optional.empty();
        }
    }

    /**
     * 将异常情况封装成RunTimeException抛出
     *
     * @param jsonStr
     * @param typeReference
     * @param <T>
     * @return
     */
    @SneakyThrows
    public <T> T readValueUnchecked(String jsonStr, TypeReference<T> typeReference) {
        return om.readValue(jsonStr, typeReference);
    }

    /**
     * 从树模型中反序列化对象
     *
     * @param jsonNode
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> Optional<T> readValue(JsonNode jsonNode, Class<T> clazz) {
        if (Objects.isNull(jsonNode) || Objects.isNull(clazz)) {
            return Optional.empty();
        }

        try {
            return Optional.of(om.treeToValue(jsonNode, clazz));
        } catch (IOException e) {
            log.error("JSON字符串反序列化失败: jsonNode = {}, class = {}", jsonNode, clazz, e);
            return Optional.empty();
        }
    }

    /**
     * 从树模型中反序列化对象
     *
     * @param jsonNode
     * @param typeReference
     * @param <T>
     * @return
     */
    public <T> Optional<T> readValue(JsonNode jsonNode, TypeReference<T> typeReference) {
        if (Objects.isNull(jsonNode) || Objects.isNull(typeReference)) {
            return Optional.empty();
        }

        try {
            return Optional.of(om.readValue(om.treeAsTokens(jsonNode), typeReference));
        } catch (IOException e) {
            log.error("JSON字符串反序列化失败: jsonNode = {}, typeReference = {}", jsonNode, typeReference.getType(), e);
            return Optional.empty();
        }
    }

    /**
     * 序列化对象
     *
     * @param obj
     * @return
     */
    public String writeValueAsString(Object obj) {
        try {
            return Optional.of(om.writeValueAsString(obj)).orElse("");
        } catch (IOException e) {
            log.error("序列化对象到JSON字符串失败: object = {}", obj, e);
            return "";
        }
    }

    /**
     * 从Json串中获取树模型，处理大Json
     *
     * @param jsonStr
     * @return
     */
    public Optional<JsonNode> readTree(String jsonStr) {
        if (Objects.isNull(jsonStr)) {
            return Optional.empty();
        }

        try {
            return Optional.of(om.readTree(jsonStr));
        } catch (IOException e) {
            log.error("JSON字符串反序列化失败: jsonStr = {}", jsonStr, e);
            return Optional.empty();
        }
    }
}
