package com.example.q.davinci.Davinci;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.q.davinci.R;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static android.support.v7.widget.LinearLayoutManager.*;

public class DavinciGameActivity extends AppCompatActivity {
    private ImageView back;
    private RecyclerView recycler1,recycler2;
    private RecyclerView.Adapter mAdapter,mAdapter2;
    private RecyclerView.LayoutManager mLayoutManager,mLayoutManager2;
    private ArrayList<Double> cards1,cards2;
    public Socket mSocket;
    public String userNo;
    private String inroom;
    private String guess;
    private String color="white";

    {try {
        mSocket = IO.socket("http://52.231.65.38:5000/");
    } catch (URISyntaxException e) {
        e.printStackTrace();
    }}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dagame);
        Intent dagame=getIntent();
        userNo=dagame.getExtras().getString("user");
        inroom=dagame.getExtras().getString("inroom");

        JSONObject gamestart=new JSONObject();
        try {
            gamestart.put("room",inroom);
            gamestart.put("username",userNo);
        } catch (JSONException e) { e.printStackTrace(); }

        mSocket.connect();
        mSocket.emit("gamestart",gamestart);
        mSocket.on("gameresult",finishGame);
        mSocket.on("hands",hands);
//        mSocket.on("whoseturn",turnset);

        cards1=new ArrayList<Double>(Arrays.<Double>asList(1.0,1.5,3.0,4.0));
        cards2=new ArrayList<Double>(Arrays.<Double>asList(3.5,5.0,8.0,9.0));

    }


    private Emitter.Listener hands = new Emitter.Listener() {
    @Override
    public void call(Object... args) {
        recycler1 = (RecyclerView) findViewById(R.id.recycler_card1);
        recycler1.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, HORIZONTAL,false);
        recycler1.setLayoutManager(mLayoutManager);
        mAdapter = new MyAdapter(cards1);
        recycler1.setAdapter(mAdapter);

        recycler2 = (RecyclerView) findViewById(R.id.recycler_card2);
        recycler2.setHasFixedSize(true);
        mLayoutManager2 = new LinearLayoutManager(this, HORIZONTAL,false);
        recycler2.setLayoutManager(mLayoutManager2);
        mAdapter2 = new MyAdapter(cards2);
        recycler2.setAdapter(mAdapter2);

        recycler2.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recycler2, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getApplicationContext(), "To confirm the chosen chip, make long click item", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongItemClick(View view, int position) {
                JSONObject guess = new JSONObject();
                try {
                    guess.put("room",inroom);
                    guess.put("username",userNo);
                    guess.put("position",String.valueOf(position));
                    Log.i("color",String.valueOf(view.getDrawingCacheBackgroundColor()));
                    show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Position 은 클릭한 Row의 position 을 반환해 준다.
                Toast.makeText(getApplicationContext(), "" + position, Toast.LENGTH_SHORT).show();
                mSocket.emit("guess",guess);
            }
        }));
        }
    };

    void show()
    {
        final List<String> ListItems = new ArrayList<>(Arrays.<String>asList("0","1","2","3","4","5","6","7","8","9","10","11","조커"));
        final CharSequence[] items =  ListItems.toArray(new String[ ListItems.size()]);

        final List SelectedItems  = new ArrayList();
        int defaultItem = 0;
        SelectedItems.add(defaultItem);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Guess the Number of Chip");
        builder.setMessage("If you don't choose any number, default number 0 will be selected.");
        builder.setSingleChoiceItems(items, defaultItem,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SelectedItems.clear();
                        SelectedItems.add(which);
                    }
                });
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String msg="";
                        if (!SelectedItems.isEmpty()) {
                            int index = (int) SelectedItems.get(0);
                            msg = ListItems.get(index);
                        }
                        if (color.equals("black")) guess=(msg+".5");
                        if (color.equals("white")) guess=(msg+".0");
                    }
                });
        builder.show();
    }

/*
    private Emitter.Listener turnset = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject res = new JSONObject(args[0].toString());
            String turn = res.getString("turn");
            if (userNo==turn){
                recycler2.setClickable(true);
                recycler1.setClickable(false);
            }else{
                recycler2.setClickable(true);
                recycler1.setClickable(false);
            }
        }
    };
*/

    private Emitter.Listener finishGame = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject res = (JSONObject) args[0];
            String winner = new String();
            TextView finishtxt= (TextView) findViewById(R.id.win);
            try { winner = res.getString("winner"); } catch (JSONException e) { e.printStackTrace(); }
            if (winner.equals("None")) finishtxt.setText("Draw!!");
            else if (winner.equals(userNo)) finishtxt.setText("You Win!!");
            else finishtxt.setText("You Lose!!");

            Intent daload = new Intent(DavinciGameActivity.this, DaLoadingActivity.class);
            startActivity(daload);
            finish();

        }
    };

}