package com.codeviser.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.codeviser.Adapter.ChatAdapter;
import com.codeviser.Adapter.VedioAdapter;
import com.codeviser.Model.ChatModel;
import com.codeviser.Model.VedioModel;
import com.codeviser.R;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {


    ChatAdapter chatAdapter;
    ArrayList<ChatModel> chatModelArrayList = new ArrayList<>();
    RecyclerView recyclechat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        recyclechat = findViewById(R.id.recyclechat);

        recyclechat.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        chatAdapter= new ChatAdapter(chatModelArrayList, this);
        recyclechat.setAdapter(chatAdapter);
        chat();
    }
    public void chat(){

       ChatModel chatModel=new ChatModel("Neelam mehra",R.drawable.circleimg);
        for (int i = 0; i <10 ; i++) {

            chatModelArrayList.add(chatModel);
            //set Recycleview Adapter
            chatAdapter=new ChatAdapter(chatModelArrayList,ChatActivity.this);
            recyclechat.setAdapter(chatAdapter);
        }
    }
}