package com.codepath.com.sffoodtruck.ui.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.codepath.com.sffoodtruck.ui.util.SmartFragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saip92 on 10/25/2017.
 * Can be reused to add title and fragments.
 */

public class CustomFragmentPagerAdapter extends SmartFragmentStatePagerAdapter {

    private final List<Fragment> mFragmentsList = new ArrayList<>();
    private final List<String> mFragmentTitles = new ArrayList<>();
    private boolean showTitles = false;

    public CustomFragmentPagerAdapter(FragmentManager fm, boolean showTitles) {
        super(fm);
        this.showTitles = showTitles;
    }

    public void addFragment(Fragment fragment, String title){
        mFragmentsList.add(fragment);
        mFragmentTitles.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentsList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentsList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(showTitles)
            return mFragmentTitles.get(position);
        else
            return null;
    }

    public int getPositionFromTitle(String fragmentTitle){
        for(int i = 0 ; i < mFragmentTitles.size(); i++){
            if(fragmentTitle.equals(mFragmentTitles.get(i))){
                return i;
            }
        }
        return -1;
    }

    public String getPageTitleFromPosition(int position){
        return mFragmentTitles.get(position);
    }

}
