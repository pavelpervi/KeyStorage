package com.hw;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Pasha on 6/1/2016.
 */
public class ServerConfig {

    @Expose
    @SerializedName("port")
    private Integer port = null;

    public Integer getPort() {
        return port;
    }
}
