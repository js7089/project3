package com.example.q.davinci;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.q.davinci.Davinci.DaLoadingActivity;

public class OneActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);

        ImageView mona2 = (ImageView) findViewById(R.id.finish);
        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(mona2);
        Glide.with(this).load(R.drawable.mo2).into(gifImage);
        new Handler().postDelayed(() -> {
            Intent onemenu = new Intent(OneActivity.this, MenuActivity.class);
            startActivity(onemenu);
            finish();
        },7000);



    }
}