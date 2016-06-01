package com.example.KeyStorageClient;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import com.example.KeyStorageClient.Network.AsyncMethodTransaction;
import com.example.KeyStorageClient.Network.Protocol.Request;
import com.example.KeyStorageClient.Network.Protocol.Response;

import java.util.HashMap;

public class MainActivity extends FragmentActivity {

    private ViewPager mViewPagerMenu = null;
    private PagerAdapter mPagerAdapter = null;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mViewPagerMenu = (ViewPager) findViewById(R.id.viewPagerMenu);
        mPagerAdapter = new PagerViewAdapter(getSupportFragmentManager());
        mViewPagerMenu.setAdapter(mPagerAdapter);

        HashMap<String, Object> param = new HashMap<>();
        param.put("key", "Fruits");
        new AsyncMethodTransaction(new Request("get", param )).setTransactionListener(new AsyncMethodTransaction.TransactionListener() {
            @Override
            public void onStartTransaction() {
            }

            @Override
            public void onFinishTransaction(Response response) {
            }

            @Override
            public void onErrorTransaction(Exception e) {
            }
        });
    }
}
