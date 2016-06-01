package com.hw.Network.Handlers;

import com.hw.Network.Protocol.Request;
import com.hw.Network.Protocol.Response;

/**
 * Created by Pasha on 5/29/2016.
 */
public interface MethodHandler {

    public String[] handledRequests();
    public Response process(Request req);
    public MethodHandler getNewInstance();

}
