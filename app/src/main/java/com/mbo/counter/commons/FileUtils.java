package com.mbo.counter.commons;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils
{
    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable()
    {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable()
    {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public static File getPublicDownloadStorageFile(String fileName)
    {
        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
    }

    public static boolean writeToFile(String data, File file)
    {
        try
        {
            FileOutputStream stream = new FileOutputStream(file);
            try
            {
                stream.write(data.getBytes());
            }
            finally
            {
                stream.close();
            }
            return true;
        }
        catch (IOException e)
        {
            Log.e("FileUtils.java", "File write failed: " + e.toString());
            return false;
        }
    }
}
