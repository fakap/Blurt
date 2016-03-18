package com.fakap.blurt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fakap.blurt.dummy.DummyContent;

public class MainActivity extends AppCompatActivity
        implements FriendListFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
