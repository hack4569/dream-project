package com.book.aladin.constants;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class AladinConstants {
    @Value("${aladin.host}")
    String aladinHost;
    public static final String ITEM_LOOKUP = "/ttb/api/ItemLookUp.aspx";

    public String url(String path){
        return aladinHost + path;
    }
}
