package com.hw.Network.Handlers;

import com.hw.Network.Protocol.Request;
import com.hw.Network.Protocol.Response;
import com.hw.Network.Protocol.ResponseCode;
import com.hw.DataStorage.StorageHandler;

import java.util.ArrayList;

/**
 * Created by Pasha on 5/29/2016.
 */
public class SetMethodHandler implements MethodHandler {
    private Response mResponse = new Response();

    @Override
    public String[] handledRequests() {
        return new String[]{"set"};
    }

    @Override
    public MethodHandler getNewInstance() {
        return new SetMethodHandler();
    }

    @Override
    public Response process(Request req) {
        String key = null;
        ArrayList<String> values = null;

        try {
            key = req.getParams().containsKey("key") ? (String) req.getParams().get("key") : null;
            values = req.getParams().containsKey("value") ? (ArrayList<String>) req.getParams().get("value") : null;
        }catch (ClassCastException e){
            return new Response(ResponseCode.ERROR, "values is not ArrayList<String> ", null);
        }
        
        if(key == null || key.isEmpty())
            return new Response(ResponseCode.ERROR, "key is not provided", null);
        if(values == null || key.isEmpty())
            return new Response(ResponseCode.ERROR, "values is not provided", null);

        StorageHandler.getInstance().set(key, values); // TODO: Add response on error to set value
        return new Response(ResponseCode.OK, null, null);
    }
}
