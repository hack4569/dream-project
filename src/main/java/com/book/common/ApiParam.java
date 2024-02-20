package com.book.common;

import lombok.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

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
    private int maxResults;
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
    private String optResult = "ebookList,usedList,reviewList,fulldescription,fulldescription2,phraseList,mdrecommend";

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
            e.printStackTrace();
        }
        return map;
    }

    public String getApiParam(){
        String result = "";
        try {
            StringBuilder sb = new StringBuilder();
            Object obj = this;
            for (Field field : this.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if(field.get(obj)!=null){
                    sb.append(field.getName() + "=" + field.get(obj).toString() + "&");
                }
            }
            result = sb.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }
}
