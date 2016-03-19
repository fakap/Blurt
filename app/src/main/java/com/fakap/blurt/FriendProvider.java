package com.fakap.blurt;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.BaseAdapter;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.fakap.blurt.model.Friend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FriendProvider {

    private static final String TAG = "FriendProvider";
    private static List<Friend> friendList = new ArrayList<>();
    private BaseAdapter friendListAdapter;

    public FriendProvider() {
        getFriendList();
    }

    public void setAdapter(BaseAdapter adapter) {
        friendListAdapter = adapter;
    }

    public int getCount() {
        return friendList.size();
    }

    public Friend getFriend(int index) {
        return friendList.get(index);
    }

    private void getFriendList() {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        parseApiFriendListResponse(response);
                    }
                }
        ).executeAsync();
    }

    private void parseApiFriendListResponse(GraphResponse response) {
        friendList = new ArrayList<>();
        JSONObject responseJsonObject = response.getJSONObject();
        Log.d(TAG, response.toString());
        try {
            JSONArray friendListArray = (JSONArray) responseJsonObject.get("data");
            String id;
            String name;
            Bitmap profilePhoto;
            for (int i = 0; i < friendListArray.length(); i++) {
                JSONObject friendObject = friendListArray.getJSONObject(i);
                id = friendObject.getString("id");
                name = friendObject.getString("name");
                getProfilePicAndCreateFriend(id, name);
                Log.d(TAG, "friend list size: " + friendList.size());
            }
            if (friendListAdapter != null) {
                friendListAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Bitmap getProfilePicAndCreateFriend(final String userId, final String name) {
        final Bitmap[] picture = {null};
        GraphRequest request = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                String.format("/%s/picture", userId),
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
                            Friend friend = new Friend(userId, name, picture[0]);
                            friendList.add(friend);
                            if (friendListAdapter != null) {
                                friendListAdapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
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
