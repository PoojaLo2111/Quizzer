package com.example.quizzer;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class StudentTabPageAdapter extends FragmentPagerAdapter {
    private Context context;
    public StudentTabPageAdapter(Context context,@NonNull FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position==0){
            return new StudentRemainingExamFragment();
        }
        else {
            return new StudentCompletedExamFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0){
            return "Remaining Test";
        }
        else {
            return "Completed Test";
        }
    }
}

