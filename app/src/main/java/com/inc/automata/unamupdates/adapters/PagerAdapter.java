package com.inc.automata.unamupdates.adapters;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.inc.automata.unamupdates.fragments.CourseNewsFragment;
import com.inc.automata.unamupdates.fragments.GeneralNewsFragment;

/**
 * Created by Detox on 14-Aug-15.
 */
/*
Project by Manfred T Makawa
University of Namibia
201201453
Computer Science and Information Technology
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
   int numOfTabs;//keep track of number of tabs

   //constructor
    public PagerAdapter (FragmentManager fm,int numOfTabs){
        super(fm);
        this.numOfTabs=numOfTabs;
    }


    @Override
    public Fragment getItem(int position) {
        //get tabs depending on position pressed
        switch(position){
            case 0:
                GeneralNewsFragment gnTab = new GeneralNewsFragment();
                return gnTab;
            case 1:
                CourseNewsFragment cnTab= new CourseNewsFragment();
                return cnTab;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
