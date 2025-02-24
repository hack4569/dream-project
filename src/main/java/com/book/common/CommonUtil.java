package com.book.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Slf4j
public class CommonUtil {

    /*	 * 공백 또는 null 체크	 */
    public static boolean isEmpty(Object obj) {
        if(obj == null) return true;
        if ((obj instanceof String) && (((String)obj).trim().length() == 0)) {
            return true;
        }
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map<?, ?>)obj).isEmpty();
        }
        if (obj instanceof List) {
            return ((List<?>)obj).isEmpty();
        }
        if (obj instanceof Object[]) {
            return (((Object[])obj).length == 0);
        }
        return false;
    }

    public static MultiValueMap<String, String> getApiParamMap(Object thisObj){
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        try {
            Object obj = thisObj;

            for (Field field : thisObj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(thisObj);
                if(!CommonUtil.isEmpty(value)){
                    if (value instanceof List<?>) {
                        for (Object list : (List<?>)value) {

                        }
                    } else {
                        map.set(field.getName(),field.get(obj).toString());
                    }
                }
            }
        }
        catch (Exception e)
        {
            log.error("getApiParamMap error : {}", e.getMessage(), e);
        }
        return map;
    }
}
