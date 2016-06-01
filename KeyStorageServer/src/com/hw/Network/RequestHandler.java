package com.hw.Network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.hw.Network.Protocol.Request;
import com.hw.Network.Protocol.Response;
import com.hw.Network.Protocol.ResponseCode;

import java.io.*;
import java.net.Socket;

/**
 * Created by Pasha on 5/29/2016.
 */
public class RequestHandler implements Runnable{

    private Socket mSocket = null;
    private OutputStream output;
    private InputStream input;
    private Gson mGson;

    public RequestHandler(Socket socket) {
        this.mSocket = socket;
        this.mGson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }

    @Override
    public void run() {
        // 1. Read request from the client socket
        // 2. Prepare an response
        // 3. Send response to the client
        // 4. Close the socket

        JsonReader jr = null;
        JsonWriter jw = null;
        try {
            output = mSocket.getOutputStream();
            input = mSocket.getInputStream();

            jr = new JsonReader(new InputStreamReader(input, "UTF-8"));
            jw = new JsonWriter(new OutputStreamWriter(output, "UTF-8"));

            Request request = null;
            try{
                request = mGson.fromJson(jr, Request.class);
                System.out.println("[RequestHandler] Received request " + request);
                if(request == null)
                    throw new IllegalArgumentException("Request not provided (null)");
            }catch (JsonSyntaxException | JsonIOException | IllegalArgumentException e){
                if(e != null)try {
                    mGson.toJson(new Response(ResponseCode.ERROR, "Request JSON error " + e.getMessage(), null), Response.class, jw);
                    jw.flush();
                }catch (JsonIOException ex){
                    System.out.println("[RequestHandler] connection info " + mSocket + " , error in " + ex.getMessage() );
                    ex.printStackTrace();
                }
                if(e instanceof IllegalArgumentException)
                    System.out.println("[RequestHandler] connection info " + mSocket + " , error in " + ((IllegalArgumentException) e).getMessage());
                return;
            }

            Response response = new HandlerDispatcher().process(request);
            try{
                System.out.println("[RequestHandler] Sending response " + response);
                mGson.toJson(response, Response.class, jw);
                jw.flush();
            }catch (JsonSyntaxException | JsonIOException e){
                if(e != null)try {
                    mGson.toJson(new Response(ResponseCode.ERROR, "Response JSON error " + e.getMessage(), null), Response.class, jw);
                    jw.flush();
                }catch (JsonIOException ex){
                    System.out.println("[RequestHandler] connection info " + mSocket + " , error in " + ex.getMessage() );
                    ex.printStackTrace();
                }
                return;
            }

            jr.close();
            jw.close();

        }catch (IOException e){
            System.out.println("[RequestHandler] I/O problem");
            e.printStackTrace();
        }finally {
            if(jr != null)try{ jr.close(); }catch (Exception e){}
            if(jw != null)try{ jw.close(); }catch (Exception e){}
            if(mSocket != null)try{mSocket.close();}catch (Exception e){}

        }

    }
}