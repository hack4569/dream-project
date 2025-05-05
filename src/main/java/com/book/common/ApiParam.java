package com.book.common;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiParam{
    @Builder.Default
    private String ttbkey = "ttbhack45691028002";
    private String itemId;
    @Builder.Default
    private String itemIdType = "ISBN13";
    private int start;
    private int maxResults = 10;
    private String cover;
    @Builder.Default
    private String searchTarget = "Book";
    @Builder.Default
    private String querytype = "Title";
    @Builder.Default
    private String output = "js";
    @Builder.Default
    private String version = "20131101";
    private String query;
    @Builder.Default
    private String optResult = "ebookList,usedList,reviewList,fulldescription,fulldescription2,phraseList,mdrecommend,toc";

    public MultiValueMap<String, String> getApiParamMap(){
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        try {
            Object obj = this;
            for (Field field : this.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if(!CommonUtil.isEmpty(field.get(obj))){
                    map.set(field.getName(),field.get(obj).toString());
                }
            }
        }
        catch (Exception e)
        {
            log.error("AladinApiParam getApiParamMap error : {}", e.getMessage(), e);
        }
        return map;
    }
}
