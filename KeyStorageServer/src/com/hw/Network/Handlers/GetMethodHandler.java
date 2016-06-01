package com.hw.Network.Handlers;

import com.hw.Network.Protocol.Request;
import com.hw.Network.Protocol.Response;
import com.hw.Network.Protocol.ResponseCode;
import com.hw.DataStorage.StorageHandler;

import java.util.ArrayList;

/**
 * Created by Pasha on 5/29/2016.
 */
public class GetMethodHandler implements MethodHandler {

    @Override
    public String[] handledRequests() {
        return new String[]{"get"};
    }

    @Override
    public MethodHandler getNewInstance() {
        return new GetMethodHandler();
    }

    @Override
    public Response process(Request req) {
        String key = req.getParams().containsKey("key") ? (String) req.getParams().get("key") : null;

        if(key == null || key.isEmpty())
            return new Response(ResponseCode.ERROR, "key is not provided", null);

        ArrayList<String> values = StorageHandler.getInstance().get(key); // TODO: Add response on error to set value

        if(values.isEmpty())
            return new Response(ResponseCode.OK, "No data found", null);

        return new Response(ResponseCode.OK, "", values);
    }
}
