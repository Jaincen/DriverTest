package com.example.drivertest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.drivertest.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.refactor.lib.colordialog.PromptDialog;

public class ErrorQuestionActivity extends AppCompatActivity{

    private List<DataBean> mDatas;
    private List<DataBean> showDatas;
    private EditText pageCount;
    private int sumScore = 0;
    private TextView timeCount;
    private TextView title;
    private int currentStatues = 0;
    private int questionSize = 0;
    private int[] currentSelect;
    private Button submitTest;
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
        timeCount.setVisibility(View.INVISIBLE);
        title.setText("错题训练");

        //submitTest.setText("退出");
        submitTest.setVisibility(View.VISIBLE);

        LayoutInflater inflater = LayoutInflater.from(mViewPager.getContext());
        View view =inflater.inflate(R.layout.fragment_empty,null);
        test = new TestDao(this);

        mDatas = test.getAllErrorQuestion(new ArrayList<DataBean>());
        showDatas =  test.getAllErrorQuestion(new ArrayList<DataBean>());
        questionSize = mDatas.size();
        for(int i = 0;i<questionSize;i++){
            showDatas.get(i).question_id = String.valueOf(i+1);

        }


        currentSelect = new int[questionSize];
        pageCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageCount.setCursorVisible(true);
            }
        });

        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), showDatas);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);



        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                mCurrentIndex = position;
                showAnswerExplain.setText("");
                if(currentStatues ==1 ) {
                    showAnswer(mDatas.get(mCurrentIndex).answer, mCurrentIndex);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    public void next(View view)  {


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
        showAnswer(currentSelect[mCurrentIndex],mCurrentIndex);


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
        for (int i = 0; i<questionSize;i++){
            if(currentSelect[i] == mDatas.get(i).answer){
                ++sumScore;
                test.setErrorType(i,0);

            }
            else{
                test.setErrorType(i,1);

            }
        }

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
            test.setErrorType(i,0);

        }
        else{
            showAnswerExplain.setText("错\n答案："+String.valueOf(c)
                    +"\n"+mDatas.get(i).explain);
            test.setErrorType(i,1);

        }



    }

    public void go(View view){
        if(mCurrentIndex<questionSize-1)
            mViewPager.setCurrentItem(mCurrentIndex+1);


    }
    public void back(View view){
        if(mCurrentIndex>0)
            mViewPager.setCurrentItem(mCurrentIndex-1);

    }

    public void showSubmitToast(){
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("确定就提交？")
                .setCancelText("取消")
                .setConfirmText("确定")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        Toast.makeText(ErrorQuestionActivity.this,"取消", Toast.LENGTH_SHORT).show();
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
                        new PromptDialog(ErrorQuestionActivity.this)
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
                        showAnswerExplain.setVisibility(View.VISIBLE);

                        mViewPager.setCurrentItem(0);
                        showAnswer(mDatas.get(mCurrentIndex).answer,mCurrentIndex);


                    }
                })
                .show();

    }




}
