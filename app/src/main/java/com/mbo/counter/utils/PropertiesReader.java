package com.mbo.counter.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader
{
    private Context context;
    private Properties properties;

    public PropertiesReader(Context context)
    {
        this.context = context;
        properties = new Properties();
    }

    public Properties getProperties(String fileName)
    {
        if (context != null)
        {
            try
            {
                AssetManager am = context.getAssets();
                InputStream inputStream = am.open(fileName);
                properties.load(inputStream);
            }
            catch (IOException e)
            {
                Log.e("PropertiesReader", e.toString());
            }
        }
        return properties;
    }
}
