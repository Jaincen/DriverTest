package com.example.drivertest.adapter;

import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.drivertest.DataBean;
import com.example.drivertest.EmptyFragment;

import java.util.List;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private String[] mTitles;
    private List<DataBean> mDatas;

    public ViewPagerAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        mTitles = titles;
    }
    public ViewPagerAdapter(FragmentManager fm, List<DataBean> datas) {
        super(fm);
        mDatas = datas;
    }

    @Override
    public Fragment getItem(int position) {
        EmptyFragment fragment = EmptyFragment.newInstance(mDatas.get(position));//EmptyFragment.newInstance(mTitles[position]);
       // Log.e("zrg", "getItem: 当前位置position=" + position);
        return fragment;
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //Log.e("zrg", "instantiateItem: 当前位置position=" + position);
        return super.instantiateItem(container, position);
    }

    //@Override
//    public CharSequence getPageTitle(int position) {
//        return "";//mDatas == null ? "" : mDatas.get(position);
//    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void setTitles(String[] titles) {
        mTitles = titles;
        notifyDataSetChanged();
    }
    public void setTitles(List<DataBean> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

}
