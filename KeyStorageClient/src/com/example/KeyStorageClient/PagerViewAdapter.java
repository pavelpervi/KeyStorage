package com.example.KeyStorageClient;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.KeyStorageClient.View.Add_Fragment;
import com.example.KeyStorageClient.View.GetAllKeys_Fragment;
import com.example.KeyStorageClient.View.Get_Fragment;
import com.example.KeyStorageClient.View.Set_Fragment;

import java.lang.ref.WeakReference;

/**
 * Created by Pasha on 5/30/2016.
 */
public class PagerViewAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 4;

    public PagerViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return Add_Fragment.newInstance(0, "Add");
            case 1:
                return Set_Fragment.newInstance(0, "Add");
            case 2:
                return Get_Fragment.newInstance(2, "Get");
            case 3:
                return GetAllKeys_Fragment.newInstance(2, "Get All keys");
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Add";
            case 1:
                return "Set";
            case 2:
                return "Get";
            case 3:
                return "Get All keys";
            default:
                return super.getPageTitle(position);
        }
    }
}
