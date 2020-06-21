package com.example.drivertest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //高版本安卓要申请授权

        if(ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
//            申请权限
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }

        if(ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS)!= PackageManager.PERMISSION_GRANTED){
//            申请权限
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS},1);
        }
        if(ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
//            申请权限
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }

    }

    public void turn2Test(View view){

        Intent intent = new Intent();
        intent.setClass(MainActivity.this,ViewPagerActivity.class);
        startActivity(intent);

    }
    public void turn2errorTest(View view){

        Intent intent = new Intent();
        intent.setClass(MainActivity.this,ErrorQuestionActivity.class);
        startActivity(intent);

    }
}
