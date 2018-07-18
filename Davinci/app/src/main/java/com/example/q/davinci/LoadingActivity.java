package com.example.q.davinci;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.q.davinci.R;

import java.util.Timer;
import java.util.TimerTask;

public class LoadingActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        final ProgressBar loading = (ProgressBar) findViewById(R.id.progressBar);
        ImageView kylie = (ImageView) findViewById(R.id.mona3);
        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(kylie);
        Glide.with(this).load(R.drawable.mo3_43).into(gifImage);

        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                loading.setProgress(loading.getProgress()+500);
            }
        };
        final Timer timer = new Timer();
        timer.schedule(tt,0,700);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                timer.cancel();
                Intent mainIntent = new Intent(LoadingActivity.this, MenuActivity.class);
                startActivity(mainIntent);
                finish();
                //overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        },SPLASH_TIME_OUT);



    }
}
