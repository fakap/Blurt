package com.fakap.blurt.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fakap.blurt.FriendListFragment;
import com.fakap.blurt.R;
import com.fakap.blurt.dummy.DummyContent;

public class BlurtActivity extends AppCompatActivity
        implements FriendListFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blurt);
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
