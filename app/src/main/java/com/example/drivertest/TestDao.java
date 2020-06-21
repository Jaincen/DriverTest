package com.example.drivertest;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class TestDao {
    private DatabaseHelper dbHelper;

    public TestDao (Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public List<DataBean> getAllTests(List<DataBean> mDatas) {
        mDatas=new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = dbHelper.openDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from t_question where question_id < 101", null);
        int i = 0;
        while(cursor.moveToNext()){
            DataBean dataBean = new DataBean(cursor.getString(cursor.getColumnIndex("question_id"))
                    ,cursor.getString(cursor.getColumnIndex("question"))
                    ,cursor.getString(cursor.getColumnIndex("media_type"))
                    ,cursor.getBlob(cursor.getColumnIndex("media_content"))
                    ,cursor.getString(cursor.getColumnIndex("option_a"))
                    ,cursor.getString(cursor.getColumnIndex("option_b"))
                    ,cursor.getString(cursor.getColumnIndex("option_c"))
                    ,cursor.getString(cursor.getColumnIndex("option_d"))
                    ,cursor.getInt(cursor.getColumnIndex("answer"))
                    ,cursor.getInt(cursor.getColumnIndex("error_type"))
                    ,cursor.getString(cursor.getColumnIndex("explain")));
            mDatas.add(dataBean);
        }
        cursor.close();
        sqLiteDatabase.close();
        return mDatas;
    }

    public void setErrorType(int id,int type){

        SQLiteDatabase sqLiteDatabase = dbHelper.openDatabase();
        sqLiteDatabase.execSQL("update t_question set error_type = "+ type + " where question_id = "+id);
        sqLiteDatabase.close();
    }
    public List<DataBean> getAllErrorQuestion(List<DataBean> mDatas){

        mDatas=new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = dbHelper.openDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from t_question where error_type = 1", null);
        int i = 0;
        while(cursor.moveToNext()){
            DataBean dataBean = new DataBean(cursor.getString(cursor.getColumnIndex("question_id"))
                    ,cursor.getString(cursor.getColumnIndex("question"))
                    ,cursor.getString(cursor.getColumnIndex("media_type"))
                    ,cursor.getBlob(cursor.getColumnIndex("media_content"))
                    ,cursor.getString(cursor.getColumnIndex("option_a"))
                    ,cursor.getString(cursor.getColumnIndex("option_b"))
                    ,cursor.getString(cursor.getColumnIndex("option_c"))
                    ,cursor.getString(cursor.getColumnIndex("option_d"))
                    ,cursor.getInt(cursor.getColumnIndex("answer"))
                    ,cursor.getInt(cursor.getColumnIndex("error_type"))
                    ,cursor.getString(cursor.getColumnIndex("explain")));
            mDatas.add(dataBean);
        }
        cursor.close();
        sqLiteDatabase.close();
        return mDatas;



    }


}
