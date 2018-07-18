package com.example.q.davinci.Davinci;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.q.davinci.MenuActivity;
import com.example.q.davinci.R;

import java.util.Timer;
import java.util.TimerTask;

public class DaLoadingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dafinish);

        ImageView mona2 = (ImageView) findViewById(R.id.finish);
        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(mona2);
        Glide.with(this).load(R.drawable.mo2).into(gifImage);
        new Handler().postDelayed(() -> {
            Intent damenuIntent = new Intent(DaLoadingActivity.this, MenuActivity.class);
            startActivity(damenuIntent);
            finish();
        },7000);



    }
}
