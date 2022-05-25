package com.G12LTUDDD.collagecommunication;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class GroupChatActivity extends AppCompatActivity {

    TextView username;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        auth = FirebaseAuth.getInstance();
        username = (TextView) findViewById(R.id.lblGroupChatUserName);
        username.setText(auth.getCurrentUser().getEmail());

    }
}