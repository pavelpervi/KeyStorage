package com.hw.Network.Protocol;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Pasha on 5/29/2016.
 */
public class Response {

    @Expose
    @SerializedName("status")
    private ResponseCode mStatus = ResponseCode.UNDEF;

    @Expose
    @SerializedName("errorMsg")
    private String mErrorMsg = "";

    @Expose
    @SerializedName("result")
    private ArrayList<String> result = new ArrayList<>();

    public Response() {
    }

    public Response(ResponseCode mStatus, String mErrorMsg, ArrayList<String> result) {
        this.mStatus = mStatus;
        this.mErrorMsg = mErrorMsg;
        this.result = result;
    }

    public ResponseCode getStatus() {
        return mStatus;
    }

    public void setStatus(ResponseCode mStatus) {
        this.mStatus = mStatus;
    }

    public String getErrorMsg() {
        return mErrorMsg;
    }

    public void setErrorMsg(String mErrorMsg) {
        this.mErrorMsg = mErrorMsg;
    }

    public ArrayList<String> getResult() {
        return result;
    }

    public void setResult(ArrayList<String> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "Response{" +
                "mStatus=" + mStatus +
                ", mErrorMsg='" + mErrorMsg + '\'' +
                ", result=" + result +
                '}';
    }
}
