package com.hw.Network.Handlers;

import com.hw.Network.Protocol.Request;
import com.hw.Network.Protocol.Response;
import com.hw.Network.Protocol.ResponseCode;
import com.hw.DataStorage.StorageHandler;

import java.util.ArrayList;

/**
 * Created by Pasha on 5/29/2016.
 */
public class GetAllKeysMethodHandler implements MethodHandler {

    @Override
    public String[] handledRequests() {
        return new String[]{"getAllKeys"};
    }

    @Override
    public MethodHandler getNewInstance() {
        return new GetAllKeysMethodHandler();
    }

    @Override
    public Response process(Request req) {
        String pattern = req.getParams().containsKey("pattern") ? (String) req.getParams().get("pattern") : null;

        if(pattern == null || pattern.isEmpty())
            return new Response(ResponseCode.ERROR, "key is not provided", null);

        ArrayList<String> allKeys = StorageHandler.getInstance().getAllKeys(pattern); // TODO: Add response on error to set value

        if(allKeys.isEmpty())
            return new Response(ResponseCode.OK, "No data found ", null);

        return new Response(ResponseCode.OK, "", allKeys);
    }
}
