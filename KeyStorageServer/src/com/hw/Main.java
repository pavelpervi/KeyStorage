package com.hw;

import com.google.gson.GsonBuilder;
import com.hw.DataStorage.StorageHandler;
import com.hw.Network.WebServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Main {
    private static final String SERVER_CONFIG_FILE_NAME = "ServerConfig.json";
    private static ServerConfig mServerConfig = null;

    static {
        File file = new File(SERVER_CONFIG_FILE_NAME);
        BufferedReader br = null;
        try{
            br = new BufferedReader(new FileReader(file));
            mServerConfig = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create().fromJson(br, ServerConfig.class);
        }catch(Exception e){
            System.out.println("[Main] Unable to read server config, because ");
            e.fillInStackTrace();
        }finally {
            if (br != null)try {br.close();} catch (Exception e){};
        }
    }

    public static void main(String[] args) {
        //storageTest_1();

        WebServer webServer = null;
        try {
            if(mServerConfig!= null && mServerConfig.getPort() != null)
                webServer = new WebServer(mServerConfig.getPort());
            else
                webServer = new WebServer();
            webServer.start();
        }catch (Exception e){
            e.fillInStackTrace();
        }finally {
            if(webServer != null)try{ webServer.close(); }catch (Exception e){}
        }
    }

    private static void storageTest_1(){
        StorageHandler storageHandler = StorageHandler.getInstance();

        ArrayList<String> values = new ArrayList<String>();
        values.add("apple");
        values.add("orange");
        values.add("malone");
        storageHandler.set("Fruits", values);
        storageHandler.leftAdd("Fruits", "Avocado");
        storageHandler.rightAdd("Fruits", "Kabobs");
        storageHandler.leftAdd("Fruits", "Ananias");

        StringBuilder sb = new StringBuilder();
        values = null;
        values = storageHandler.get("Fruits");
        for(String s : values)
            sb.append(s).append(", ");

        System.out.println("Fruits -> " + sb.toString());

        sb = new StringBuilder();
        for(String s : storageHandler.getAllKeys("\\w+"))
            sb.append(s).append(", ");

        System.out.println("Keys -> " + sb.toString());
    }
}
