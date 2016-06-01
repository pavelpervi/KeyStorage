package com.example.KeyStorageClient.Network.Protocol;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * Created by Pasha on 5/29/2016.
 */
public class Request {

    @Expose
    @SerializedName("method")
    private String mMethod = "";

    @Expose
    @SerializedName("params")
    private HashMap<String, Object> mParams = new HashMap<>();

    public Request(String mMethod, HashMap<String, Object> mParams) {
        this.mMethod = mMethod;
        this.mParams = mParams;
    }

    public String getMethod() {
        return mMethod;
    }

    public void setMethod(String mMethod) {
        this.mMethod = mMethod;
    }



    public HashMap<String, Object> getParams() {
        return mParams;
    }

    public void setParams(HashMap<String, Object> mParams) {
        this.mParams = mParams;
    }

    @Override
    public String toString() {
        return "Request{" +
                "mMethod='" + mMethod + '\'' +
                ", mParams=" + mParams +
                '}';
    }
}
