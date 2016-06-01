package com.example.KeyStorageClient.Global;

import android.app.Application;
import android.util.Log;
import com.google.gson.GsonBuilder;

import java.io.*;

/**
 * Created by Pasha on 5/30/2016.
 */
public class Globals extends Application{
    public static final String LOG_TAG = "MyLog";
    private static Globals ourInstance = null;
    private static final String NETWORK_SETTINGS_FILE_NAME = "NetworkSettings.json";
    private static final String CONFIG_FILE_PATH = "";

    private static boolean isInit = false;

    private NetworkSettings mNetworkSettings = null;

    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = this;
        init();
    }

    public static void init(){
        if(!isInit) {
            BufferedReader br = null;
            try {
                File configFile = new File(getInstance().getAssets().toString());
                ;
                br = new BufferedReader(new InputStreamReader(getInstance().getAssets().open(NETWORK_SETTINGS_FILE_NAME)));
                ourInstance.mNetworkSettings = new GsonBuilder()
                        .excludeFieldsWithoutExposeAnnotation()
                        .create()
                        .fromJson(br, NetworkSettings.class);
            } catch (Exception e) {
                Log.e(LOG_TAG, "[Globals] Failed to init Globals on reading NetworkSettings.json ", e);
                return;
            }finally {
                if(br != null)try{ br.close(); }catch (Exception e){}
            }
            isInit = true;
        }
    }

    public static Globals getInstance() {
        return ourInstance;
    }


    public NetworkSettings getNetworkSettings() {
        return mNetworkSettings;
    }
}
