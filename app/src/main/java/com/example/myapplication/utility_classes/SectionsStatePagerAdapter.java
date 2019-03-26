package com.example.myapplication.utility_classes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * File created by tcarau18
 **/
public class SectionsStatePagerAdapter extends FragmentStatePagerAdapter {

    public final List<Fragment> mFragmentList = new ArrayList<>();
    public final HashMap<Fragment, Integer> mFragments = new HashMap<>();
    public final HashMap<String, Integer> mFragmentNumbers = new HashMap<>();
    public final HashMap<Integer, String> mFragmentNames = new HashMap<>();

    public SectionsStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return mFragmentList.get(i);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String fragmentName) {
        mFragmentList.add(fragment);
        mFragments.put(fragment, mFragmentList.size() - 1);
        mFragmentNumbers.put(fragmentName, mFragmentList.size() - 1);
        mFragmentNames.put(mFragmentList.size() - 1, fragmentName);
    }

    /**
     * returns the fragment with the name @param
     *
     * @param fragmentName represents Fragment's Name
     * @return returns the fragment with the name @param
     */
    public Integer getFragmentNumber(String fragmentName) {
        if (mFragmentNumbers.containsKey(fragmentName)) {
            return mFragmentNumbers.get(fragmentName);
        } else {
            return null;
        }
    }

    /**
     * returns the fragment with the name @param
     *
     * @param fragment represents the Fragment
     * @return returns the fragment with the name @param
     */
    public Integer getFragmentNumber(Fragment fragment) {
        if (mFragmentNumbers.containsKey(fragment)) {
            return mFragmentNumbers.get(fragment);
        } else {
            return null;
        }
    }

    /**
     * returns the fragment with the name @param
     *
     * @param fragmentNumber represents the Fragment
     * @return returns the fragment with the name @param
     */
    public String getFragmentName(Integer fragmentNumber) {
        if (mFragmentNames.containsKey(fragmentNumber)) {
            return mFragmentNames.get(fragmentNumber);
        } else {
            return null;
        }
    }
}
