package com.example.mapnotes;

import android.widget.SimpleAdapter;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

class ViewPagerAdapter extends FragmentPagerAdapter {


    ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null ;

        switch ( position ){
            case 0: fragment = new simpleNoteFragment() ;
                    break;

            case 1: fragment = new mapNoteFragment() ;
                    break;
        };

        return fragment ;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return "SIMPLE NOTES" ;
            case 1: return "MAP NOTES" ;
        }
        return ""  ;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
