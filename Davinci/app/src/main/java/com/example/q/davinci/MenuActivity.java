package com.example.q.davinci;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.q.davinci.Davinci.DaRoomActivity;
import com.example.q.davinci.Davinci.DaRoomListAdapter;

public class MenuActivity extends AppCompatActivity implements  View.OnClickListener{
    TextView text1;
    TextView text2;
    TextView text3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        text1 = (TextView) findViewById(R.id.startDavinci);
        text2 = (TextView) findViewById(R.id.startOne);
        text3 = (TextView) findViewById(R.id.quit);
        text1.setOnClickListener(this);
        text2.setOnClickListener(this);
        text3.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.startDavinci:
                Intent davinciIntent = new Intent(MenuActivity.this, DaRoomActivity.class);
                startActivity(davinciIntent);
                break;
            case R.id.startOne:
                Intent oneIntent = new Intent(MenuActivity.this, OneActivity.class);
                startActivity(oneIntent);
                break;
            case R.id.quit:
                moveTaskToBack(true);
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                break;
        }

    }





}
