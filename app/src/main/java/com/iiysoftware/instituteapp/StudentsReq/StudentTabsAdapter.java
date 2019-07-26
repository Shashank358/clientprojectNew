package com.iiysoftware.instituteapp.StudentsReq;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class StudentTabsAdapter extends FragmentStatePagerAdapter {


    public StudentTabsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new StudentsSendReq();
            case 1:
                return new StudentsAppReq();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0:
                return "Send Request";

            case 1:
                return "Approved Request";

            default:
                return null;
        }
    }
}
