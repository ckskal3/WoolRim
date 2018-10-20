package org.woolrim.woolrim;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    List<Fragment> items = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addItem(Fragment item){
        items.add(item);
    }

    @Override
    public Fragment getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence title = null;
        switch (position){
            case 0:
                title = "마이울림";
                break;
            case 1:
                title = "마이알람";
                break;
        }
        return title;
    }
}
