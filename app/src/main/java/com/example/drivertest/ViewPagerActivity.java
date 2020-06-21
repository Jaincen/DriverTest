package com.example.drivertest;

import android.app.AlertDialog;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.PointerIcon;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.drivertest.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.refactor.lib.colordialog.ColorDialog;
import cn.refactor.lib.colordialog.PromptDialog;


public class ViewPagerActivity extends AppCompatActivity {
    private List<DataBean> mDatas;
    private RadioGroup radioGroupAnser;
    private EditText pageCount;
    private int sumScore = 0;
    private int questionSize = 0;
    private TextView timeCount;
    private TextView title;
    private int[] currentSelect;
    private Button submitTest;
    private int currentStatues = 0;
    private TextView showAnswerExplain;

    SwipeRefreshLayout mSwipeRefreshLayout;
    ViewPager mViewPager;
    int time = 3600;

    private ViewPagerAdapter mAdapter;
    private int mCurrentIndex;
    TestDao test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        mViewPager = findViewById(R.id.view_pager);
        pageCount = findViewById(R.id.pageCount);
        timeCount = findViewById(R.id.time_count);
        title = findViewById(R.id.Alltitle);
        submitTest = findViewById(R.id.submit);
        showAnswerExplain = findViewById(R.id.textAnswer);
        title.setText("在线考试");

        LayoutInflater inflater = LayoutInflater.from(mViewPager.getContext());
        View view =inflater.inflate(R.layout.fragment_empty,null);
        test = new TestDao(this);
        mDatas = new ArrayList<>();
        mDatas = test.getAllTests(mDatas);
        questionSize = mDatas.size();
        currentSelect = new int[questionSize];
        pageCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageCount.setCursorVisible(true);
            }
        });
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mDatas);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);
        radioGroupAnser = view.findViewById(R.id.radioGroupId);



        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                mCurrentIndex = position;
                //int a = mViewPager.getCurrentItem();
                Log.e("zjc","当前位置: "+mCurrentIndex);
                if(currentStatues ==1 ){
                    showAnswer(mDatas.get(mCurrentIndex).answer,mCurrentIndex);


                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        time= 10;
        final Handler handler = new Handler(){
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                if(msg.obj.toString().equals("TimeOver") == true){
                    showAnswerExplain.setVisibility(View.VISIBLE);
                    timeCount.setText("");
                    broutSubmitToast();


                }else {
                    timeCount.setText(msg.obj.toString());

                }


            }
        };
        Thread t = new Thread(){
            public void run(){
                while(time>0){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message msg= new Message();
                    --time;
                    if(currentStatues == 1){
                        msg.obj = "";
                        handler.sendMessage(msg);
                        break;

                    }
                    else{
                        msg.obj = "剩余时间："+time/60 +"分" + time%60+"秒";
                        handler.sendMessage(msg);
                    }



                }
                if(currentStatues == 0){
                    currentStatues =1;
                    Message msg= new Message();
                    msg.obj = "TimeOver";
                    handler.sendMessage(msg);



                }





            }
        };
        t.start();

    }

    public void next(View view)  {
        if(currentStatues == 0){
            switch (view.getId()){
                case R.id.ButtonA:
                    currentSelect[mCurrentIndex] = 1;
                    break;
                case R.id.ButtonB:
                    currentSelect[mCurrentIndex] = 2;
                    break;
                case R.id.ButtonC:
                    currentSelect[mCurrentIndex] = 3;
                    break;
                case R.id.ButtonD:
                    currentSelect[mCurrentIndex] = 4;
                    break;
                default:break;


            }
            radioGroupAnser.setEnabled(false);
            Log.e("Score Current",String.valueOf(sumScore));

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(mCurrentIndex+1<questionSize){
                        mViewPager.setCurrentItem(mCurrentIndex+1,true);
                    }
                    else{
                        submitTest.setVisibility(View.VISIBLE);

                    }

                }
            }, 300);//300ms后执行Runnable中的run方法

        }


    }
    public void submitPage(View view){

        if(submitTest.getText().toString().equals("交卷")==true){
            showSubmitToast();
        }
        else if (submitTest.getText().toString().equals("退出")==true){
            finish();

        }



    }
    public void gotoPage(View view){

        pageCount.setCursorVisible(false);
        if (pageCount.getText().toString().equals(""))return;

        int tempCount = Integer.parseInt(pageCount.getText().toString())-1;
        pageCount.setText("");

        if(tempCount<questionSize )
            mViewPager.setCurrentItem(tempCount);


    }
    public void checkAnser(){
        for (int i = 0; i<mDatas.size();i++){
            if(currentSelect[i] == mDatas.get(i).answer){
                ++sumScore;
                test.setErrorType(i,0);

            }
            else{
                test.setErrorType(i,1);

            }
        }

    }
    public void go(View view){
       if(mCurrentIndex<questionSize-1)
           mViewPager.setCurrentItem(mCurrentIndex+1);
       if(currentStatues ==1 )
            showAnswer(mDatas.get(mCurrentIndex).answer,mCurrentIndex);

    }
    public void back(View view){
        if(mCurrentIndex>0)
            mViewPager.setCurrentItem(mCurrentIndex-1);
        if(currentStatues ==1 )
            showAnswer(mDatas.get(mCurrentIndex).answer,mCurrentIndex);
    }
    public void showAnswer(int answer,int i){

        char c=0;
        switch (answer){
            case 1: c = 'A';
                break;
            case 2: c = 'B';
                break;
            case 3: c = 'C';
                break;
            case 4:c = 'D';
                break;
            default:break;
        }


        if(currentSelect[i] == mDatas.get(i).answer){
            showAnswerExplain.setText("对\n答案："+String.valueOf(c)
                    +"\n"+mDatas.get(i).explain);

        }
        else{
            showAnswerExplain.setText("错\n答案："+String.valueOf(c)
                    +"\n"+mDatas.get(i).explain);

        }



    }
    public void showSubmitToast(){
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("确定提交吗？")
                .setCancelText("取消")
                .setConfirmText("确定")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        Toast.makeText(ViewPagerActivity.this,"取消", Toast.LENGTH_SHORT).show();
                        sDialog.dismiss();



                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        sDialog.dismiss();
                        checkAnser();
                        currentStatues = 1;
                        mCurrentIndex = 0;
                        new PromptDialog(ViewPagerActivity.this)
                                .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                                .setAnimationEnable(true)
                                .setTitleText("提交成功")
                                .setContentText("你的得分是："+String.valueOf(1))
                                .setPositiveListener("OK", new PromptDialog.OnPositiveListener() {
                                    @Override
                                    public void onClick(PromptDialog dialog) {
                                        dialog.dismiss();
                                    }
                                }).show();
                        timeCount.setText("");
                        submitTest.setText("退出");
                        showAnswerExplain.setVisibility(View.VISIBLE);

                        mViewPager.setCurrentItem(0);
                        showAnswer(mDatas.get(mCurrentIndex).answer,mCurrentIndex);

                    }
                })
                .show();

    }
    public void broutSubmitToast(){
         //currentStatues = 1;

         mCurrentIndex = 0;

        checkAnser();
        new PromptDialog(ViewPagerActivity.this)
                .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                .setAnimationEnable(true)
                .setTitleText("提交成功")
                .setContentText("你的得分是："+String.valueOf(1))
                .setPositiveListener("OK", new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
        submitTest.setText("退出");
        submitTest.setVisibility(View.VISIBLE);

        mViewPager.setCurrentItem(0);
        showAnswer(mDatas.get(mCurrentIndex).answer,mCurrentIndex);


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(currentStatues == 0){
            if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                showSubmitToast();
                submitTest.setVisibility(View.VISIBLE);
                return true;
            }else {
                return super.onKeyDown(keyCode, event);
            }

        }
        else{
            if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                return true;
            }else {
                return super.onKeyDown(keyCode, event);
            }
        }
        //return super.onKeyDown(keyCode, event);

    }

}


