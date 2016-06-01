package com.hw.Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Pasha on 5/27/2016.
 */
public class FileUtils {

    public static void writeToEndOfFile(File file, String text) throws IOException{
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));){
            bw.append(text);
        }
    }

    public static void deleteFile(File file){
        if(file == null || !file.exists())
            return;

        if(file.isFile()) try {
            file.delete();
        }catch (SecurityException e){
            System.out.println("[FileUtils] Unable to delete file " + file.getName());
        }
    }
}
