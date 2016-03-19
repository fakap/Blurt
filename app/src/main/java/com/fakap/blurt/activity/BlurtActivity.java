package com.fakap.blurt.activity;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.fakap.blurt.fragment.ChatFragment;
import com.fakap.blurt.fragment.FriendListFragment;
import com.fakap.blurt.R;
import com.fakap.blurt.model.Friend;

public class BlurtActivity extends FragmentActivity
        implements FriendListFragment.OnListFragmentInteractionListener,
        ChatFragment.OnFragmentInteractionListener {
    private static final String TAG = "BlurtActivity";
    public static String lastContactedFriendId = null;
    private ChatFragment chatFragment;
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_blurt);

        pager = (ViewPager) findViewById(R.id.view_pager);
        pager.setAdapter(new BlurtPagerAdapter(getSupportFragmentManager()));
    }

    // Chat Fragment Interaction Listener
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(Friend item) {
        chatFragment.setUpConversation(item.getId());
        pager.setCurrentItem(1);
    }

    // PagerAdapter for swiping between fragments
    private class BlurtPagerAdapter extends FragmentPagerAdapter {
        public BlurtPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return FriendListFragment.newInstance();
                case 1:
                    chatFragment = ChatFragment.newInstance("dummyId");
                    return chatFragment;
                default:
                    return FriendListFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
