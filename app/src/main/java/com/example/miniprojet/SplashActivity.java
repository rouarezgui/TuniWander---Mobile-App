package com.example.miniprojet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashActivity extends AppCompatActivity {

    private VideoView videoView;
    private GestureDetector gestureDetector;
    private TextView arrowUp;

    // Swipe sensitivity
    private static final int SWIPE_THRESHOLD     = 100;
    private static final int SWIPE_VELOCITY      = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        videoView = findViewById(R.id.videoView);
        arrowUp   = findViewById(R.id.arrowUp);

        // Load video from res/raw
        Uri videoUri = Uri.parse(
                "android.resource://" + getPackageName() + "/" + R.raw.tunisia_video
        );
        videoView.setVideoURI(videoUri);
        videoView.start();

        // Loop video
        videoView.setOnCompletionListener(mp -> videoView.start());

        // Arrow bounce animation
        startArrowAnimation();

        // GestureDetector for swipe
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2,
                                   float velocityX, float velocityY) {

                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();

                // Check if swipe is more vertical than horizontal
                if (Math.abs(diffY) > Math.abs(diffX)) {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD &&
                            Math.abs(velocityY) > SWIPE_VELOCITY) {

                        // Swipe UP → diffY negative
                        if (diffY < 0) {
                            goToMain();
                            return true;
                        }
                    }
                }
                return false;
            }
        });

        // Apply gesture to full screen
        findViewById(R.id.main).setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return true;
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Arrow bouncing up animation
    private void startArrowAnimation() {
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -20);
        animation.setDuration(600);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        arrowUp.startAnimation(animation);
    }

    private void goToMain() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoView.isPlaying()) {
            videoView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.start();
    }
}