package com.G12LTUDDD.collagecommunication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.G12LTUDDD.collagecommunication.Adapters.MessageAdapter;
import com.G12LTUDDD.collagecommunication.Models.AllMethods;
import com.G12LTUDDD.collagecommunication.Models.Message;
import com.G12LTUDDD.collagecommunication.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {


    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference messagedb;
    MessageAdapter messageAdapter;
    User u;
    List<Message> messages;

    RecyclerView rvMessage;
    EditText etInput;
    ImageButton imgBSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();
    }

    public void init(){
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        u = new User();
        rvMessage = findViewById(R.id.rvChatMessage);
        etInput = findViewById(R.id.txtChatInput);
        imgBSend = findViewById(R.id.imgBChatSend);
        messages = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        if(!TextUtils.isEmpty(etInput.getText().toString())){
            Message message = new Message(etInput.getText().toString(),u.getName());
            etInput.setText("");
            messagedb.push().setValue(message);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menuLogout){
            auth.signOut();
            finish();
            startActivity(new Intent(ChatActivity.this,MainActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        final FirebaseUser curUser = auth.getCurrentUser();

        u.setUid(curUser.getUid());
        u.setEmail(curUser.getEmail());

        db.getReference("Users").child(curUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                u = snapshot.getValue(User.class);
                u.setUid(curUser.getUid());
                AllMethods.name = u.getName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        messagedb = db.getReference("Messages");
        messagedb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                message.setKey(snapshot.getKey());
                messages.add(message);

                displayMessages(messages);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                message.setKey(snapshot.getKey());

                List<Message> newMessages = new ArrayList<Message>();

                for(Message m:messages){
                    if(m.getKey().equals(message.getKey())){
                        newMessages.add(message);
                    }
                    else {
                        newMessages.add(m);
                    }
                }

                messages = newMessages;
                displayMessages(messages);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Message message = snapshot.getValue(Message.class);
                message.setKey(snapshot.getKey());

                List<Message> newMessages = new ArrayList<Message>();

                for(Message m:messages){
                    if(m.getKey().equals(message.getKey())){
                        newMessages.add(m);
                    }
                }

                messages = newMessages;
                displayMessages(messages);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        messages = new ArrayList<Message>();
    }

    private void displayMessages(List<Message> messages){
        rvMessage.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        messageAdapter = new MessageAdapter(ChatActivity.this,messages,messagedb);
        rvMessage.setAdapter(messageAdapter);
    }
}