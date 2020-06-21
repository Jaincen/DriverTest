package com.example.drivertest;

import androidx.annotation.NonNull;

public class DataBean {
    public String question_id;
    public String media_type;
    public String label;
    public String question;
    public int answer;
    public String optionA;
    public String optionB;
    public String optionC;
    public String optionD;
    public byte[] media;
    public int errorType;
    public String explain;

    public DataBean(){

    }
    public DataBean(String question_id, String media_type, String label, String question, int answer) {
        this.media_type = media_type;
        this.label = label;
        this.question = question;
        this.answer = answer;
        this.question_id=question_id;
    }
    public DataBean(String question_id, String question,
                    String mediaType, byte[] media,
                    String optionA, String optionB,
                    String optionC, String optionD,
                    int answer,int errorType,
                    String explain){
        this.question_id=question_id;
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.media_type = mediaType;
        this.media = media;
        this.answer = answer;
        this.errorType = errorType;
        this.explain = explain;


    }
    public DataBean(String question_id,int errorType){
        this.errorType = errorType;
        this.question_id = question_id;

    }

    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        DataBean stu = null;
        try{
            stu = (DataBean)super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return stu;
    }
}
