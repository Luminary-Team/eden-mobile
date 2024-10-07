package com.eden.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.eden.PostsProfileTab;
import com.eden.ProductsProfileTab;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1: return new PostsProfileTab();
            default: return new ProductsProfileTab();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}