package com.example.q.davinci.Davinci;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.q.davinci.R;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DaRoomListAdapter extends BaseAdapter {
    private ArrayList<DaRoomListObject> listVO = new ArrayList<DaRoomListObject>() ;
    public DaRoomListAdapter() {

    }

    @Override
    public int getCount() {
        return listVO.size() ;
    }

    // ** 이 부분에서 리스트뷰에 데이터를 넣어줌 **
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //postion = ListView의 위치      /   첫번째면 position = 0
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.daroom_list, parent, false);
        }

        TextView state = (TextView) convertView.findViewById(R.id.room_state) ;
        TextView title = (TextView) convertView.findViewById(R.id.room_title) ;
        TextView Context = (TextView) convertView.findViewById(R.id.room_context) ;


        DaRoomListObject listViewItem = listVO.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        state.setText(listViewItem.getState());
        title.setText(listViewItem.getTitle());
        Context.setText(listViewItem.getContext());

        return convertView;
    }


    @Override
    public long getItemId(int position) {
        return position ;
    }


    @Override
    public Object getItem(int position) {
        return listVO.get(position) ;
    }

    // 데이터값 넣어줌
    public void addVO(String stt, String title, String cont) {
        DaRoomListObject item = new DaRoomListObject();

        item.setState("Number of People in the Room : "+stt);
        item.setTitle(title);
        item.setContext("Detail : "+cont);

        listVO.add(item);
    }

}