package com.example.drivertest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DatabaseHelper{

    private final int BUFFER_SIZE = 400000;
    public static final String DB_NAME = "kaoshi.db"; // 保存的数据库文件名称
    public static final String PACKAGE_NAME = "com.example.drivertest";// 应用的包名
    public static final String DB_PATH = "/data"
            + Environment.getDataDirectory().getAbsolutePath() +"/"
            + PACKAGE_NAME+ "/databases"; // 在手机里存放数据库的位置
    //sdcard的路径(在android 4.4中不好使，文件成功创建是在手机的)
    //public static final String DB_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/idiom";

    private Context context;

    public DatabaseHelper(Context context) {
        this.context = context;
    }


    public SQLiteDatabase openDatabase() {
        try {
            File myDataPath = new File(DB_PATH);
            if (!myDataPath.exists())
            {
                myDataPath.mkdirs();// 假设没有这个文件夹,则创建
            }
            String dbfile=myDataPath+"/"+DB_NAME;
            if (!(new File(dbfile).exists())) {// 推断数据库文件是否存在，若不存在则运行导入，否则直接打开数据库
                InputStream is = context.getResources().openRawResource(
                        R.raw.kaoshi); // 欲导入的数据库
                FileOutputStream fos = new FileOutputStream(dbfile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile,
                    null);
            return db;
        } catch (FileNotFoundException e) {
            Log.e("Database", "File not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Database", "IO exception");
            e.printStackTrace();
        }
        return null;
    }
}

