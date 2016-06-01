package com.hw.Network.Handlers;

import com.hw.Network.Protocol.Request;
import com.hw.Network.Protocol.Response;
import com.hw.Network.Protocol.ResponseCode;
import com.hw.DataStorage.StorageHandler;

/**
 * Created by Pasha on 5/29/2016.
 */
public class RightAddMethodHandler implements MethodHandler {

    @Override
    public String[] handledRequests() {
        return new String[]{"rightAdd"};
    }

    @Override
    public MethodHandler getNewInstance() {
        return new RightAddMethodHandler();
    }

    @Override
    public Response process(Request req) {
        String key = null, value = null;
        try {
            key = req.getParams().containsKey("key") ? (String) req.getParams().get("key") : null;
            value = req.getParams().containsKey("value") ? (String) req.getParams().get("value") : null;
        }catch (ClassCastException e){
            return new Response(ResponseCode.ERROR, "value is not a String", null);
        }

        if(key == null || key.isEmpty())
            return new Response(ResponseCode.ERROR, "key is not provided", null);
        if(value == null || key.isEmpty())
            return new Response(ResponseCode.ERROR, "value is not provided", null);

        StorageHandler.getInstance().rightAdd(key, value); // TODO: Add response on error to set value
        return new Response(ResponseCode.OK, null, null);
    }
}
