package com.fakap.blurt.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.fakap.blurt.model.Friend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BlurtActivity extends FragmentActivity
        implements FriendListFragment.OnListFragmentInteractionListener,
        ChatFragment.OnFragmentInteractionListener
{
    private static final String TAG = "BlurtActivity";

    AccessToken currentFbAccessToken;
    AccessToken oldFbAccessToken;
    List<Friend> friendList;

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

    private void parseFriendListResponse(GraphResponse response) {
        friendList = new ArrayList<>();
        JSONObject responseJsonObject = response.getJSONObject();
        try {
            JSONArray friendListArray = (JSONArray) responseJsonObject.get("data");
            String id;
            String name;
            Bitmap profilePhoto;
            for (int i = 0; i < friendListArray.length(); i++) {
                JSONObject friendObject = friendListArray.getJSONObject(i);
                id = friendObject.getString("id");
                name = friendObject.getString("name");
                profilePhoto = getProfilePicForId(id);
                Friend friend = new Friend(id, name, profilePhoto);
                friendList.add(friend);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (Friend friend : friendList) {
            Log.d(TAG, friend.toString());
        }

    }

    private Bitmap getProfilePicForId(String id) {
        final Bitmap[] picture = {null};
        GraphRequest request = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                String.format("/%s/picture", id),
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        try {
                            String pictureUrlString = (String) response.getJSONObject()
                                    .getJSONObject("data")
                                    .get("url");
                            URL pictureUrl = new URL(pictureUrlString);
                            picture[0] = new DownloadProfilePicTask().execute(pictureUrl).get();
                        } catch (JSONException | IOException | InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        Bundle parameters = new Bundle();
        parameters.putString("type", "normal");
        parameters.putString("redirect", "false");
        request.setParameters(parameters);
        request.executeAsync();
        return picture[0];
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
                        parseFriendListResponse(response);
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

    private class DownloadProfilePicTask extends AsyncTask<URL, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(URL... urls) {
            //TODO publish progress while loading app
            Bitmap profilePic = null;
            try {
                profilePic = BitmapFactory.decodeStream(
                        urls[0].openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return profilePic;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            Log.d("DownloadProfilePicTask", "Downloaded pic!");
        }
    }
}
