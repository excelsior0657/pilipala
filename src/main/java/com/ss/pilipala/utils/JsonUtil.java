package com.ss.pilipala.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ss.pilipala.errors.BusinessException;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class JsonUtil {
    private static ObjectMapper mapper;

    public static void initMapper() {
        if (Objects.nonNull(mapper)) {
            return;
        }
        mapper = SpringContextUtil.getBean(ObjectMapper.class);
    }

    public static <T> T parse(String json, Class<T> clazz) {
        initMapper();
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            log.error("json 解析失败，", e);
            throw BusinessException.businessError("json 解析失败");
        }
    }

    public static <T> T parse(String json, TypeReference<T> type) {
        initMapper();
        try {
            return mapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            log.error("json 解析失败，", e);
            throw BusinessException.businessError("json 解析失败");
        }
    }

    public static String toJson(Object obj) {
        initMapper();
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("json 序列化失败", e);
            throw BusinessException.businessError("json 序列化失败");
        }
    }

    public static <T> T convert(Object obj, Class<T> clazz) {
        initMapper();
        try {
            return mapper.convertValue(obj, clazz);
        } catch (Exception e) {
            log.error("json 转换失败", e);
            throw BusinessException.businessError("json 转换失败");
        }
    }
}