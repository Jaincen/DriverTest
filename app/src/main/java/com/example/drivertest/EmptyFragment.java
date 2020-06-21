package com.example.drivertest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;

import com.example.drivertest.base.BaseLazyFragment;


public class EmptyFragment extends BaseLazyFragment {
    private static final String ARG_TITLE = "arg_title";

    TextView mTvClick;
    private RadioGroup radioGroup;

    private RadioButton onlinetest_radioA;
    private RadioButton onlinetest_radioB;
    private RadioButton onlinetest_radioC;
    private RadioButton onlinetest_radioD;
    private ImageView questionImage;

    private DataBean mTitle;

    public EmptyFragment() {
    }

    public static EmptyFragment newInstance(DataBean dataBean) {
        EmptyFragment fragment = new EmptyFragment();
        Bundle bundle = new Bundle();
        //bundle.putString(ARG_TITLE, title);
        ParcelableData parcelableData = new ParcelableData(dataBean);
        bundle.putParcelable(ARG_TITLE,parcelableData);

        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParcelableData parcelableCartoon = getArguments().getParcelable(ARG_TITLE);
        mTitle = parcelableCartoon.getDataBean();




    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_empty, container, false);
        mTvClick = (TextView) view.findViewById(R.id.tv_click);

        onlinetest_radioA = view.findViewById(R.id.ButtonA);
        onlinetest_radioB = view.findViewById(R.id.ButtonB);
        onlinetest_radioC= view.findViewById(R.id.ButtonC);
        onlinetest_radioD= view.findViewById(R.id.ButtonD);
        questionImage = view.findViewById(R.id.mquestion_image);
        questionImage.setVisibility(View.GONE);


        mIsprepared = true;
        lazyLoad();
        return view;
    }

    @Override
    protected void lazyLoad() {
        if (!mIsprepared || !mIsVisible || mHasLoadedOnce) {
            return;
        }
        mHasLoadedOnce = true;
        //UI和业务逻辑
        Log.e("zrg", "lazyLoad: 当前的fragment mTitle=:" + mTitle);
        mTvClick.setText("第"+mTitle.question_id+"题： "+mTitle.question);
        onlinetest_radioA.setVisibility(View.VISIBLE);
        onlinetest_radioB.setVisibility(View.VISIBLE);
        onlinetest_radioA.setText(mTitle.optionA.replaceAll("\r|\n",""));
        onlinetest_radioB.setText(mTitle.optionB.replaceAll("\r|\n",""));
        if(mTitle.optionC.equals("null")==true){
            onlinetest_radioC.setVisibility(View.INVISIBLE);
            onlinetest_radioD.setVisibility(View.INVISIBLE);
        }
        else {
            onlinetest_radioC.setText(mTitle.optionC.replaceAll("\r|\n",""));
            onlinetest_radioD.setText(mTitle.optionD.replaceAll("\r|\n",""));
            onlinetest_radioC.setVisibility(View.VISIBLE);
            onlinetest_radioD.setVisibility(View.VISIBLE);

        }
        if (mTitle.media_type.equals("1")==true) {
            // 0意思是可见的
            questionImage.setVisibility(View.VISIBLE);
            // 获取图片

            Bitmap bitmap = BitmapFactory.decodeByteArray(mTitle.media, 0, mTitle.media.length);
            questionImage.setImageBitmap(bitmap);


        } else {
            questionImage.setVisibility(View.GONE);
            // 常量值为4，意思是不可见的
            // 常量值为8，意思是不可见的，而且不占用布局空间
        }


    }

    public DataBean getTitle() {
        return mTitle;
    }
}
