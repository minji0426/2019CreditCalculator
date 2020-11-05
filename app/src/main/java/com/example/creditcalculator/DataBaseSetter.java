package com.example.creditcalculator;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataBaseSetter {
    public static String DB_NAME;
    public static String ROOT_DIR;

    public static void setDB(Context ctx, String dname) {
        DB_NAME = dname;
        ROOT_DIR = "/data/data/" + ctx.getPackageName() +"/databases/";
        File folder = new File(ROOT_DIR);
        if(folder.exists()) {
        } else {
            folder.mkdirs();
        }
        AssetManager assetManager = ctx.getResources().getAssets();
        File outfile = new File(ROOT_DIR + DB_NAME);
        InputStream is = null;
        FileOutputStream fo = null;
        long filesize = 0;
        try {
            is = assetManager.open(DB_NAME, AssetManager.ACCESS_BUFFER);
            filesize = is.available();
            if (outfile.length() <= 0) {
                byte[] tempdata = new byte[(int) filesize];
                is.read(tempdata);
                is.close();
                outfile.createNewFile();
                fo = new FileOutputStream(outfile);
                fo.write(tempdata);
                fo.close();
            } else {}
        } catch (IOException e) {}
    }
}
