package com.fullstackyang.crawler.weibo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

public class AbstractDTO {

    public JSONObject toJSON(){
        ObjectMapper objectMapper=new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ;

        try {
            String json = objectMapper.writeValueAsString(this);
            return new JSONObject(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @JsonIgnore
    public List<Field> getAllFields() {
        List<Field> result = Lists.newArrayList();
        for (Class<?> clazz = this.getClass(); clazz != null && clazz != Object.class; clazz = clazz.getSuperclass()) {
            Collections.addAll(result, clazz.getDeclaredFields());
        }
        return result;
    }
}
