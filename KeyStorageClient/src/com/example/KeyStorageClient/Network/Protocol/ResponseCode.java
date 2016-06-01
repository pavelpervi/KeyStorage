package com.example.KeyStorageClient.Network.Protocol;

/**
 * Created by Pasha on 5/29/2016.
 */
public enum ResponseCode {
    UNDEF(0),
    OK(1),
    ERROR(2),
    UNSUPPORTED_METHOD(3)
    ;

    private final int value;

    ResponseCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ResponseCode getCodeById(int id){
        try{
            return values()[id];
        }catch (IndexOutOfBoundsException e){
            return UNDEF;
        }
    }

    @Override
    public String toString() {
        return "ResponseCode{" +
                "value=" + value +
                '}';
    }
}
