package com.fakap.blurt;

import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.facebook.FacebookSdk;
import android.widget.VideoView;

public class WelcomeActivity extends AppCompatActivity {
    public static final String TAG = "WelcomeActivity";

    VideoView parrotVideo;
    TextView blurtLabel;
    private Typeface labelTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        loadParrotVideo();
        loadBlurtLabel();
    }

    private void loadParrotVideo() {
        parrotVideo = (VideoView) findViewById(R.id.parrot_video_view);
        String videoPath = ("android.resource://"+getPackageName()+"/raw/parrot");
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        parrotVideo.suspend();
        super.onStop();
    }
}