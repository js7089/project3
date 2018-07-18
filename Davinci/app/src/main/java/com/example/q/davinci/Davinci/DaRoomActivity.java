package com.example.q.davinci.Davinci;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.q.davinci.MenuActivity;
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
import java.util.Map;

public class DaRoomActivity  extends AppCompatActivity {
    private ArrayList<Map<String, String>> roomlist;
    private ListView roomlists;
    public Socket mSocket;
    public String userNo;
    public Vibrator v;
    private String inroom="";
    {
        try {
            mSocket = IO.socket("http://52.231.65.38:5000/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daroom);
        roomlists = (ListView) findViewById(R.id.daRoomList);
        final Button addroom = (Button) findViewById(R.id.addroom);
        addroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        });
        roomlists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String,String> chosen = roomlist.get(position);
                JSONObject clicked = new JSONObject();
                if (inroom.equals("")){
                    try {
                        clicked.put("room",chosen.get("title"));
                        clicked.put("content",chosen.get("context"));
                        clicked.put("username",userNo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), "You are in the room-"+chosen.get("title"),Toast.LENGTH_LONG).show();
                    inroom=chosen.get("title");
                    mSocket.emit("join",clicked);
                }
                else if(chosen.get("title").equals(inroom)){
                    try {
                        clicked.put("room",chosen.get("title"));
                        clicked.put("content",chosen.get("context"));
                        clicked.put("username",userNo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    inroom="";
                    Toast.makeText(getApplicationContext(), "Now you are NOT in the room",Toast.LENGTH_LONG).show();
                    mSocket.emit("leave",clicked);
                }
                else{
                    JSONObject out=new JSONObject();
                    try {
                        out.put("username",userNo);
                        out.put("room",inroom);
                        clicked.put("room",chosen.get("title"));
                        clicked.put("content",chosen.get("context"));
                        clicked.put("username",userNo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    inroom=chosen.get("title");
                    Toast.makeText(getApplicationContext(), "You are in the room-"+chosen.get("title"),Toast.LENGTH_LONG).show();
                    mSocket.emit("join",clicked);
                }

                // Position 은 클릭한 Row의 position 을 반환해 준다.
                Toast.makeText(getApplicationContext(), "" + position, Toast.LENGTH_SHORT).show();
            }
        });

        mSocket.connect();
        mSocket.emit("roomlist",new JSONObject());
        mSocket.emit("userID",new JSONObject());
        mSocket.on("roomlist",setroomlist);
        mSocket.on("gamestart",onRoomJoining);
        mSocket.on("userid",onUserIDrequested);
    }











    void show()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_addroom, null);
        builder.setView(view);
        final Button submit = (Button) view.findViewById(R.id.buttonSubmit);
        final EditText roomtit = (EditText) view.findViewById(R.id.roomtit);
        final EditText roomcont = (EditText) view.findViewById(R.id.roomcont);

        final AlertDialog dialog = builder.create();
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String roomt = roomtit.getText().toString();
                String roomc = roomcont.getText().toString();
                JSONObject newroom = new JSONObject();
                try {
                    newroom.put("username",userNo);
                    newroom.put("room",roomt);
                    newroom.put("content",roomc);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "You are in the room-"+roomt+". Please waite other one.",Toast.LENGTH_LONG).show();
                mSocket.emit("mkroom",newroom);
                inroom=roomt;
                dialog.dismiss();
            }
        });

        dialog.show();

    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.close();
    }
    private Emitter.Listener setroomlist = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i("id","$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              //변수 초기화
                              DaRoomListAdapter custom_adapter = new DaRoomListAdapter();
                              roomlists.setAdapter(custom_adapter);
                              roomlist=new ArrayList<>();
                              Log.i("id","@@@@@@@@@@@@"+args[0].toString());
                              //String[] rooms = args[0].toString().split("}, {");

                              JSONArray arg= null;
                              try {
                                  arg = new JSONArray(args[0].toString());
                              } catch (JSONException e) {
                                  e.printStackTrace();
                              }
                              try {
                                  Log.i("id","@@@$$$$$$$$$$$$$@"+arg.toString());

                                  for (int i=0;i<arg.length();i++) {
                                      JSONObject onerow = (JSONObject) arg.getJSONObject(i);
                                      HashMap<String,String> map = new HashMap<>();
                                      try {
                                          custom_adapter.addVO(onerow.getString("state"),onerow.getString("title"),onerow.getString("content"));
                                          map.put("state",onerow.getString("state"));
                                          map.put("title",onerow.getString("title"));
                                          map.put("context",onerow.getString("content"));
                                          roomlist.add(map);
                                      } catch (JSONException e) {
                                          e.printStackTrace();
                                      }
                                  }
                              } catch (JSONException e) {
                                  e.printStackTrace();
                              }
                          }
                    });
                }
            }).start();

        }
    };


    private Emitter.Listener onUserIDrequested = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject res = (JSONObject) args[0];
            String userid = (res.toString().split(":\"")[1]).split("\"\\}")[0];
            Log.i("id","@@@@@@@@@@@@@@@@@@@"+userid);
            userNo = userid;
        }
    };


    private Emitter.Listener onRoomJoining = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject roomnum = (JSONObject)args[0];
            String roomNo= null;
            try { roomNo = roomnum.getString("title"); } catch (JSONException e) { e.printStackTrace(); }
            //Toast.makeText(getApplicationContext(), "Game is started in Room-"+roomNo, Toast.LENGTH_LONG).show();
            Intent dagame = new Intent(DaRoomActivity.this, DavinciGameActivity.class);
            dagame.putExtra("user",userNo);
            dagame.putExtra("inroom",inroom);
            startActivity(dagame);
            finish();
        }
    };

}
