package com.fakap.blurt.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.VideoView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.fakap.blurt.Constants;
import com.fakap.blurt.R;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class WelcomeActivity extends AppCompatActivity {
    public static final String TAG = "WelcomeActivity";

    VideoView parrotVideo;
    TextView blurtLabel;
    private Typeface labelTypeface;

    private LoginButton facebookLoginButton;
    private CallbackManager facebookCallbackManager;
    private AccessTokenTracker facebookAccessTokenTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Firebase.setAndroidContext(this);
            FacebookSdk.sdkInitialize(this);
        }
        Constants.firebaseReference = new Firebase("https://fakap-blurt.firebaseio.com/");
        setContentView(R.layout.activity_welcome);

        setUpFacebookLogin();
        loadParrotVideo();
        loadBlurtLabel();
    }

    private void setUpFacebookLogin() {
        facebookCallbackManager = CallbackManager.Factory.create();
        facebookLoginButton = (LoginButton) findViewById(R.id.facebook_login_button);
        assert facebookLoginButton != null;
        facebookLoginButton.setReadPermissions("user_friends");
//        facebookLoginButton.registerCallback(facebookCallbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                signInWithFacebook();
//                Intent intent = new Intent(getApplicationContext(), BlurtActivity.class);
//                startActivity(intent);
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//
//            }
//        });

        facebookAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                Log.i(TAG, "Facebook.AccessTokenTracker.OnCurrentAccessTokenChanged");
                if (currentAccessToken != null) {
                    Constants.firebaseReference.authWithOAuthToken("facebook",
                            currentAccessToken.getToken(), new Firebase.AuthResultHandler() {
                                @Override
                                public void onAuthenticated(AuthData authData) {
                                    Log.i(TAG, "Facebook authentication successful.");
                                    Intent intent = new Intent(getApplicationContext(), BlurtActivity.class);
                                    startActivity(intent);
                                }

                                @Override
                                public void onAuthenticationError(FirebaseError firebaseError) {
                                    Log.e(TAG, firebaseError.toString());
                                }
                            });
                } else {
                    Constants.firebaseReference.unauth();
                }
            }
        };
    }

    private void loadParrotVideo() {
        parrotVideo = (VideoView) findViewById(R.id.parrot_video_view);
        String videoPath = ("android.resource://" + getPackageName() + "/raw/parrot");
        Uri parrotVideoUri = Uri.parse(videoPath);

        parrotVideo.setVideoURI(parrotVideoUri);

        parrotVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                parrotVideo.start();
            }
        });
    }

    private void loadBlurtLabel() {
        blurtLabel = (TextView) findViewById(R.id.blurt_title_label_text_view);

        labelTypeface = Typeface.createFromAsset(getAssets(),
                "fonts/confetti.otf");

        blurtLabel.setTypeface(labelTypeface);
    }

    @Override
    protected void onStart() {
        super.onStart();
        parrotVideo.start();
        if (parrotVideo != null && !parrotVideo.isPlaying()) {
            parrotVideo.resume();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        parrotVideo.suspend();
        super.onStop();
    }
}