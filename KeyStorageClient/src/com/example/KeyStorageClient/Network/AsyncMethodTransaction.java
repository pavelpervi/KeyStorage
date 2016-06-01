package com.example.KeyStorageClient.Network;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.example.KeyStorageClient.Global.Globals;
import com.example.KeyStorageClient.Network.Protocol.Request;
import com.example.KeyStorageClient.Network.Protocol.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Pasha on 5/30/2016.
 */
public class AsyncMethodTransaction {

    private Socket mSocket = null;
    private OutputStream output;
    private InputStream input;
    private Gson mGson;
    private Request mRequest = null;


    public AsyncMethodTransaction(Request request) {
        Log.e(Globals.LOG_TAG, "[AsyncMethodTransaction] in AsyncMethodTransaction(Request request)");
        this.mRequest = request;
        this.mGson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }

    public void setTransactionListener(TransactionListener mTransactionListener) {
        Log.e(Globals.LOG_TAG, "[AsyncMethodTransaction] in setTransactionListener(TransactionListener mTransactionListener) = " + mTransactionListener);
        this.mTransactionListener = mTransactionListener;
    }

    public Request getRequest() {
        return mRequest;
    }

    public void setRequest(Request mRequest) {
        Log.e(Globals.LOG_TAG, "[AsyncMethodTransaction] in setRequest(Request mRequest) = " + mRequest);
        this.mRequest = mRequest;
    }

    public void connect(TransactionListener transactionListener){
        Log.e(Globals.LOG_TAG, "[AsyncMethodTransaction] in connect(TransactionListener transactionListener) = " + transactionListener);
        this.mTransactionListener = transactionListener;
        connect();
    }

    public void connect() {
        Log.e(Globals.LOG_TAG, "[AsyncMethodTransaction] in connect()");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e(Globals.LOG_TAG, "[AsyncMethodTransaction] in Thread run()");
                String host = Globals.getInstance().getNetworkSettings().getServerHost();
                int port = Globals.getInstance().getNetworkSettings().getServerPort();

                fireTransactionStatusEvent(TransactionStatus.FIRE_START_TRANSACTION);
                JsonReader jr = null;
                JsonWriter jw = null;
                try {
                    //mSocket = new Socket(host, port);
                    mSocket = new Socket();
                    mSocket.setSoLinger(true, 10 * 1000);
                    mSocket.setSoTimeout(10 * 1000);
                    mSocket.connect(new InetSocketAddress(host, port), 10*1000);

                    output = mSocket.getOutputStream();
                    input = mSocket.getInputStream();

                    jr = new JsonReader(new InputStreamReader(input, "UTF-8"));
                    jw = new JsonWriter(new OutputStreamWriter(output, "UTF-8"));




                    try{
                        Log.e(Globals.LOG_TAG, "------------------------ Send - " + mGson.toJson(mRequest));
                        mGson.toJson(mRequest, Request.class, jw);
                        jw.flush();
                    }catch (JsonSyntaxException | JsonIOException e){
                        Log.e(Globals.LOG_TAG, "[AsyncMethodTransaction] Request JSON error ", e);
                        fireTransactionStatusEvent(TransactionStatus.FIRE_ERROR_TRANSACTION, new Exception("Send Request JSON error " + (e != null ? e.getMessage() : "fatal error")));
                        return;
                    }

                    Response response = null;
                    try{
                        response = mGson.fromJson(jr, Response.class);
                        Log.e(Globals.LOG_TAG, "------------------------ Received - " + mGson.toJson(response));
                        if(response == null)
                            throw new IllegalArgumentException("Server Response is (null)");
                    }catch (JsonSyntaxException | JsonIOException | IllegalArgumentException e){
                        Log.e(Globals.LOG_TAG, "[AsyncMethodTransaction] Response JSON error ", e);
                        if(e instanceof IllegalArgumentException)
                            fireTransactionStatusEvent(TransactionStatus.FIRE_ERROR_TRANSACTION, new Exception("Request JSON error " + ((IllegalArgumentException)e).getMessage()));
                        else
                            fireTransactionStatusEvent(TransactionStatus.FIRE_ERROR_TRANSACTION, new Exception("Request JSON error " + (e != null ? e.getMessage() : "fatal error")));
                        return;
                    }

                    fireTransactionStatusEvent(TransactionStatus.FIRE_FINISH_TRANSACTION, response);

                    jr.close();
                    jw.close();

                }catch (IOException e){
                    Log.e(Globals.LOG_TAG, "[AsyncMethodTransaction]  I/O problem ", e);
                    fireTransactionStatusEvent(TransactionStatus.FIRE_ERROR_TRANSACTION, new Exception(e.getMessage()));
                }finally {
                    if(jr != null)try{ jr.close(); }catch (Exception e){}
                    if(jw != null)try{ jw.close(); }catch (Exception e){}
                    if(mSocket != null)try{mSocket.close();}catch (Exception e){}

                }

            }
        }).start();
    }

    //Callback handler - run on main thread
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if(mTransactionListener == null)
                return false;
            TransactionStatus status = TransactionStatus.getStatusFromInt(message.what);
            switch (status){
                case FIRE_START_TRANSACTION:
                    mTransactionListener.onStartTransaction();
                    return true;
                case FIRE_FINISH_TRANSACTION:
                    mTransactionListener.onFinishTransaction((Response)message.obj);
                    return true;
                case FIRE_ERROR_TRANSACTION:
                    mTransactionListener.onErrorTransaction((Exception)message.obj);
                    return true;
            }
            return false;
        }
    });

    //Callback listener
    private TransactionListener mTransactionListener = null;
    public interface TransactionListener{
        public void onStartTransaction();
        public void onFinishTransaction(Response response);
        public void onErrorTransaction(Exception e);
    }

    private enum TransactionStatus {
        FIRE_START_TRANSACTION(0),
        FIRE_FINISH_TRANSACTION(1),
        FIRE_ERROR_TRANSACTION(2)
        ;

        private final int id;

        TransactionStatus(int id) {
            this.id = id;
        }

        static TransactionStatus getStatusFromInt(int status){
            try{
                return values()[status];
            }catch (IndexOutOfBoundsException e){
                return FIRE_ERROR_TRANSACTION;
            }
        }

        int getId() {
            return id;
        }
    }

    private void fireTransactionStatusEvent(TransactionStatus status){
        fireTransactionStatusEvent(status, null);
    }

    private void fireTransactionStatusEvent(TransactionStatus status, Object obj){
        if(obj != null)
            mHandler.sendMessage(mHandler.obtainMessage(status.getId(), obj));
        else
            mHandler.sendEmptyMessage(status.getId());
    }

    public void registerTransactionListener(TransactionListener transactionListener){
        this.mTransactionListener = transactionListener;
    }

    public void unregisterTransactionListener(){
        this.mTransactionListener = null;
    }

}
