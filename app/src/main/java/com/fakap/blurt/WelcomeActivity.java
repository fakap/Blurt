package com.fakap.blurt;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

public class WelcomeActivity extends AppCompatActivity {
    public static final String TAG = "WelcomeActivity";

    VideoView parrotVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        loadParrotVideo();
    }

    private void loadParrotVideo() {
        parrotVideoView = (VideoView) findViewById(R.id.parrot_video_view);
        String videoPath = ("android.resource://"+getPackageName()+"/raw/parrot");
        Uri parrotVideoUri = Uri.parse(videoPath);

        parrotVideoView.setVideoURI(parrotVideoUri);

        parrotVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                parrotVideoView.start();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        parrotVideoView.start();
        if (parrotVideoView != null && !parrotVideoView.isPlaying()) {
            parrotVideoView.resume();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        parrotVideoView.suspend();
        super.onStop();
    }
}