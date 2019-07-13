package com.xxm.minidouyin.personalCenter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.xxm.minidouyin.videoRecyclerView.VideoGridFragment;
import com.xxm.minidouyin.videoRecyclerView.VideoGridViewHolder;

public class SectionPagerAdapter extends FragmentPagerAdapter {

    private static final int PAGE_COUNT = 2;
    private static String[] tabs = {"作品", "收藏"};
    private String user_name;

    public SectionPagerAdapter(FragmentManager fm, String user) {
        super(fm);
        user_name = user;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int i) {

        if (i == 1) {
            return VideoGridFragment.newInstance(user_name);
        }
        else {
            return VideoGridFragment.newInstance("None");
        }
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }
}
