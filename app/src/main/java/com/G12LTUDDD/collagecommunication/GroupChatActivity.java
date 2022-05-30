package com.G12LTUDDD.collagecommunication;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.G12LTUDDD.collagecommunication.Adapters.MessageAdapter;
import com.G12LTUDDD.collagecommunication.Models.Message;
import com.G12LTUDDD.collagecommunication.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class GroupChatActivity extends AppCompatActivity {

    TextView username;
    FirebaseAuth auth;
    FirebaseDatabase  db;
    MessageAdapter messageAdapter;
    User u;
    List<Message> messageList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);


    }

    public void init(){
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        u = new User();

    }

    public void LogoutUser(View v){
        auth.signOut();
        onDestroy();
    }
}