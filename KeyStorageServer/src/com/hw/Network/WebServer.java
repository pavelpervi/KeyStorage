package com.hw.Network;

import com.hw.Network.Handlers.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Pasha on 5/27/2016.
 */
public class WebServer {
    private int mPort = 12345;
    private ServerSocket mServerSocket = null;
    private ExecutorService threadPool = Executors.newFixedThreadPool(20);

    public WebServer(){}

    public WebServer(int port){
        mPort = port;
    }

    public void start() throws Exception{
        mServerSocket = new ServerSocket(mPort);
        //init method handlers
        HandlerDispatcher.register(new GetAllKeysMethodHandler());
        HandlerDispatcher.register(new RightAddMethodHandler());
        HandlerDispatcher.register(new LeftAddMethodHandler());
        HandlerDispatcher.register(new SetMethodHandler());
        HandlerDispatcher.register(new GetMethodHandler());

        while(true) {
            try {
                Socket clientSocket = mServerSocket.accept();
                System.out.println("[WebServer] Connect " + clientSocket.toString());
                threadPool.execute(new RequestHandler(clientSocket));

            } catch (IOException e) {
                System.out.println("Failed to start server: ");
                e.printStackTrace();
                System.exit(0);
            }
        }
    }

    public void close() throws Exception{
        if(mServerSocket != null)
            mServerSocket.close();
    }
}
