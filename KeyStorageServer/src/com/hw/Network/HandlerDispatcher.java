package com.hw.Network;

import com.hw.Network.Protocol.Request;
import com.hw.Network.Protocol.Response;
import com.hw.Network.Protocol.ResponseCode;
import com.hw.Network.Handlers.MethodHandler;

import java.util.ArrayList;

/**
 * Created by Pasha on 5/29/2016.
 */
public class HandlerDispatcher {

    private static ArrayList<MethodHandler> mRequestHandlersList = new ArrayList<>();

    public HandlerDispatcher() {
    }

    public static void register(MethodHandler handler){
        if(!mRequestHandlersList.contains(handler))
            mRequestHandlersList.add(handler);
    }

    public Response process(Request request){
        for(MethodHandler handler : mRequestHandlersList)
            for(String handlerName : handler.handledRequests())
                if(handlerName.equals(request.getMethod()))
                    return handler.getNewInstance().process(request);
        return new Response(ResponseCode.UNSUPPORTED_METHOD, "Handler not found for request: " + request, null );
    }

}
