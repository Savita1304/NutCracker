package com.example.nutcracker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity implements Animation.AnimationListener {

    private Animation animSlideup;
    private Animation animScaleOutBounce;
    private Animation animScaleInBounce;
    private LinearLayout layout;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        layout = findViewById(R.id.linear);
        img = findViewById(R.id.img);

        // Load the animations
        animSlideup = AnimationUtils.loadAnimation(this, R.anim.slide_up);
       animScaleOutBounce    = AnimationUtils.loadAnimation(this, R.anim.zoom_out);

        animSlideup.setAnimationListener(this);
        animScaleOutBounce.setAnimationListener(this);

        layout.startAnimation(animSlideup);
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == animSlideup) {
            img.startAnimation(animScaleOutBounce);
        } else if (animation == animScaleOutBounce) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
                }
            }, 2000);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }
}
