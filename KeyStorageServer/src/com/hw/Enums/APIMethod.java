package com.hw.Enums;


/**
 * Created by Pasha on 5/28/2016.
 */
public enum  APIMethod {
    UNDEF("UNDEF"),
    RIGHT_ADD("rightAdd"),
    LEFT_ADD("leftAdd"),
    SET("set"),
    GET("get"),
    GET_ALL_KEYS("getAllKeys")
    ;

    private final String value;

    APIMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static APIMethod getAPIMethodByName(String name){
        for(APIMethod method : values())
            if(method.getValue().equals(name))
                return method;
        return UNDEF;
    }

}
