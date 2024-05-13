package com.book.aladin.constants;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AladinConstants {
    @Value("${aladin.host}")
    public String aladinHost;
    public static final String ITEM_LOOKUP = "/ttb/api/ItemLookUp.aspx";

    public static final String ITEM_LIST = "/ttb/api/ItemList.aspx";

    public String url(String path){
        return aladinHost + path;
    }
}

