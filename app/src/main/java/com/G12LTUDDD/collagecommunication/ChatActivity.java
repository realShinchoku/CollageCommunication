package com.G12LTUDDD.collagecommunication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.G12LTUDDD.collagecommunication.Adapters.MessageAdapter;
import com.G12LTUDDD.collagecommunication.Models.Group;
import com.G12LTUDDD.collagecommunication.Models.Message;
import com.G12LTUDDD.collagecommunication.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {


    FirebaseAuth auth;
    FirebaseFirestore db;
    User u;
    Group group;
    List<Message> messages;
    MessageAdapter messageAdapter;
    RecyclerView rvMessage;

    EditText etInput;
    ImageButton ibSend, ibBack, ibCall, ibVideo;
    TextView tvGroupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();

    }

    public void init() {

        Intent i = getIntent();
        group = (Group) i.getExtras().getSerializable("group");

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        u = new User();
        messages = new ArrayList<>();

        rvMessage = findViewById(R.id.rvChatMessage);
        etInput = findViewById(R.id.etChat);
        ibSend = findViewById(R.id.ibSendChat);
        ibBack = findViewById(R.id.ibBackChat);
        tvGroupName = findViewById(R.id.tvChat);
        tvGroupName.setText(group.getName());

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ibSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etInput.getText().toString().equals("")) {
                    Message message = new Message();
                    message.setValue(etInput.getText().toString());
                    message.setGid(group.getGid());
                    message.setUid(u.getUid());
                    message.setTime(Timestamp.now().toDate());
                    db.collection("Messages").add(message).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                message.setKey(task.getResult().getId());
                                db.collection("Messages").document(message.getKey()).update("key", message.getKey());
                                etInput.setText("");
                            }
                        }
                    });
                }
            }
        });


        final FirebaseUser curUser = auth.getCurrentUser();
        u.setUid(curUser.getUid());
        u.setEmail(curUser.getEmail());


        db.collection("Users").document(u.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                u = documentSnapshot.toObject(User.class);
            }
        });

        db.collection("Messages")
                .orderBy("time", Query.Direction.ASCENDING)
                .whereEqualTo("gid", group.getGid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {

                        }
                        messages = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : value) {
                            Message message = doc.toObject(Message.class);
                            messages.add(message);
                        }
                        displayMessages(messages, u);
                    }
                });


    }

    private void displayMessages(List<Message> messages, User u) {
        rvMessage.setHasFixedSize(true);
        rvMessage.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        messageAdapter = new MessageAdapter(ChatActivity.this, messages, db, u.getUid());
        rvMessage.setAdapter(messageAdapter);
        rvMessage.getLayoutManager().smoothScrollToPosition(rvMessage, null, messageAdapter.getItemCount() - 1);
    }

}