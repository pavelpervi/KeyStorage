package com.hw.DataStorage;

import com.hw.Utils.FileUtils;
import com.sun.istack.internal.Nullable;
import com.sun.javafx.beans.annotations.NonNull;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Pasha on 5/27/2016.
 */
public class StorageHandler {
    public static final String STORAGE_PATH;
    public static final String STORAGE_DIR = "KeyStorage";
    public static final String VALUES_SEPARATOR = "\n";
    private final File mStorageFile;

    private static final int MAX_CACHE_SIZE = 100;
    private static final long CACHE_TIMER_INTERVAL = 1 * 60;
    private static final long MAX_TIME_TO_LEAVE_CACHE = 5 * 60;
    private final MemoryCache<String, ArrayList<String>> mCache;

    static {
        STORAGE_PATH = (System.getProperty("user.dir") != null ? System.getProperty("user.dir") : System.getProperty("java.io.tmpdir"));
    }

    private static StorageHandler mInstance = new StorageHandler();

    /**
     *
     * @return return single instance of StorageHandler
     */
    public static StorageHandler getInstance() {
        return mInstance;
    }

    private StorageHandler() {
        mCache = new MemoryCache<>(MAX_TIME_TO_LEAVE_CACHE, CACHE_TIMER_INTERVAL, MAX_CACHE_SIZE);
        mStorageFile = new File(STORAGE_PATH + "\\" + STORAGE_DIR);
        try {
            if (!mStorageFile.exists() || (mStorageFile.exists() && !mStorageFile.isDirectory()))
                mStorageFile.mkdirs();
        }catch (SecurityException e){
            e.fillInStackTrace();
            System.out.println("[StorageHandler] Unable to create local storage. server terminated...");
            System.exit(1);
        }
    }

    /**
     * Adds a value to key key, from the right
     * @param key - key
     * @param value - value
     */
    public synchronized void rightAdd(final String key, final String value) {
        if(key == null || value == null || key.isEmpty() || value.isEmpty())
            return;
        File[] fileList = mStorageFile.listFiles(new FilenameFilter(){
            @Override
            public boolean accept(File dir, String name) {
                return key.equals(name) && new File(dir, name).isFile();
            }
        });

        BufferedReader br = null;

        if(fileList == null || fileList.length <= 0){
            try{
                fileList = new File[]{new File(mStorageFile + "\\" + key)};
                fileList[0].createNewFile();
            }catch (IOException e){
                System.out.println("[StorageHandler] Unable to create file");
                e.printStackTrace();
            }
        }
        writeValuesDataToFile(fileList[0], value + "\n");
    }

    /**
     * Adds a value to key key, from the left
     * @param key - key
     * @param value - value
     */
    public synchronized void leftAdd(final String key, final String value){
        if(key == null || value == null || key.isEmpty() || value.isEmpty())
            return;
        File[] fileList = mStorageFile.listFiles(new FilenameFilter(){
            @Override
            public boolean accept(File dir, String name) {
                return key.equals(name) && new File(dir, name).isFile();
            }
        });

        BufferedReader br = null;
        if(fileList != null && fileList.length > 0){ // if key is already exists
            try{
                br = new BufferedReader(new FileReader(fileList[0]));
                StringBuilder prevValues = new StringBuilder(value).append(VALUES_SEPARATOR);
                String buff;
                while ((buff = br.readLine()) != null){
                    prevValues.append(buff).append(VALUES_SEPARATOR);
                }
                br.close();

                FileUtils.deleteFile(fileList[0]);
                writeValuesDataToFile(new File(mStorageFile, fileList[0].getName()), prevValues.toString());
            }catch (IOException e){
                e.printStackTrace();
                System.out.println();
            }finally {
                if(br != null) try{br.close();}catch (IOException  e){}
            }
        }else{ //if the key is first time added
            try{
                fileList = new File[]{new File(mStorageFile + "\\" + key)};
                fileList[0].createNewFile();
                writeValuesDataToFile(fileList[0], value);
            }catch (IOException e){
                System.out.println("[StorageHandler] Unable to create file");
                e.printStackTrace();
            }
        }
    }

    private synchronized void writeValuesDataToFile(File file, String valuesData){
        BufferedWriter bw = null;
        try{
            FileUtils.writeToEndOfFile(file, valuesData);
        }catch (IOException e){
            System.out.println("[StorageHandler] Unable to write value " + valuesData + "into file " + file);
            e.printStackTrace();
        }finally {
            if(bw != null) try{bw.close();}catch (Exception e){}
        }
    }

    /**
     * Set new value list to key, erase values before.
     * @param key
     * @param values
     */
    public synchronized void set(final String key, final List<String> values){
        StringBuilder sb = new StringBuilder();

        for(String item : values)
            sb.append(item).append(VALUES_SEPARATOR);

        File file = new File(mStorageFile + "\\" + key);
        FileUtils.deleteFile(file);
        writeValuesDataToFile(file, sb.toString());
    }

    /**
     *  Gets a list by its key
     * @param key
     * @return list of values associated with given key
     */
    @NonNull
    public synchronized ArrayList<String> get(final String key) {
        ArrayList<String> values = mCache.get(key);
        if(values != null && !values.isEmpty())
            return values;

        if (key == null || key.isEmpty())
            return new ArrayList<String>();

        String[] fileList = mStorageFile.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return key.equals(name) && new File(dir, name).isFile();
            }
        });

        if (fileList == null || fileList.length <= 0)
            return new ArrayList<String>();

        values = new ArrayList<>(fileList.length);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(mStorageFile + "\\" + fileList[0]));
            String buff;
            while ((buff = br.readLine()) != null)
                values.add(buff);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) try {
                br.close();
            } catch (IOException e) {
            }
        }
        if(!values.isEmpty())
            mCache.put(key, values);

        return values;
    }

        /**
         * Get all keys matching pattern.
         * @param pattern - regular expression
         * @return List of matched keys
         */
        @NonNull
        public synchronized ArrayList<String> getAllKeys(final String pattern){
            if(pattern == null || pattern.isEmpty())
                return new ArrayList<String>();

            String[] fileList = null;
            try {
                fileList = mStorageFile.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return Pattern.matches(pattern, name) && new File(dir, name).isFile();
                    }
                });
            }catch (Exception e){
                System.out.println("[StorageHandler] Wrong regular expression");
                e.printStackTrace();
                fileList = null;
            }

            if(fileList == null || fileList.length <= 0)
                return new ArrayList<String>();

            ArrayList<String> values = new ArrayList<>(fileList.length);
            Collections.addAll(values, fileList);

            return values;
        }
    }
