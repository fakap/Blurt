package com.fakap.blurt.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.fakap.blurt.fragment.ChatFragment;
import com.fakap.blurt.fragment.FriendListFragment;
import com.fakap.blurt.R;
import com.fakap.blurt.dummy.DummyContent;

public class BlurtActivity extends FragmentActivity
        implements FriendListFragment.OnListFragmentInteractionListener,
        ChatFragment.OnFragmentInteractionListener
{
    private static final String TAG = "BlurtActivity";

    AccessToken currentFbAccessToken;
    AccessToken oldFbAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blurt);

        Intent intent = getIntent();
        getFbAccessToken();
        getFriendList();

        ViewPager pager = (ViewPager) findViewById(R.id.view_pager);
        pager.setAdapter(new BlurtPagerAdapter(getSupportFragmentManager()));
    }

    private void getFbAccessToken() {
        currentFbAccessToken = AccessToken.getCurrentAccessToken();
    }

    private void getFriendList() {
        new GraphRequest(
                currentFbAccessToken,
                "/me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        Log.d(TAG, response.toString());
                    }
                }
        ).executeAsync();
    }

    // List Fragment Interaction Listener
    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
    }

    // Chat Fragment Interaction Listener
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private class BlurtPagerAdapter extends FragmentPagerAdapter {
        public BlurtPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {

                case 0: return FriendListFragment.newInstance();
                case 1: return ChatFragment.newInstance("dummyId");
                default: return FriendListFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
