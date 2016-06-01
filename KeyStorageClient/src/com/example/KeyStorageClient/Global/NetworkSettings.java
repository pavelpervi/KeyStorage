package com.example.KeyStorageClient.Global;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Pasha on 5/30/2016.
 */
public class NetworkSettings {

    @Expose
    @SerializedName("host")
    private String serverHost = "";

    @Expose
    @SerializedName("port")
    private int serverPort = -1;

    public NetworkSettings() {
    }

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

}
