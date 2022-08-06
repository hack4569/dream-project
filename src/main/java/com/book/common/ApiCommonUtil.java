package com.book.common;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public class ApiCommonUtil {
    public static boolean isConnected(ResponseEntity response){
        boolean isConnected = false;
        if(response.getStatusCodeValue()==200) isConnected = true;
        return isConnected;
    }


}
