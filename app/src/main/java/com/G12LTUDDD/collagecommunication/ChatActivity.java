package com.G12LTUDDD.collagecommunication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.G12LTUDDD.collagecommunication.Adapters.MessageAdapter;
import com.G12LTUDDD.collagecommunication.Models.Group;
import com.G12LTUDDD.collagecommunication.Models.Message;
import com.G12LTUDDD.collagecommunication.Models.User;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseFirestore db;
    StorageReference reference;
    User u;
    Group group;
    List<Message> messages;
    MessageAdapter messageAdapter;
    RecyclerView rvMessage;

    EditText etInput;
    ImageButton ibSend, ibBack, ibDetail,ibSendImg;
    TextView tvGroupName,tvGroupId;
    CircleImageView civImgGroup;

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
        reference = FirebaseStorage.getInstance().getReference();

        u = new User();
        messages = new ArrayList<>();

        rvMessage = findViewById(R.id.rvChatMessage);
        etInput = findViewById(R.id.etChat);
        ibSend = findViewById(R.id.ibSendChat);
        ibBack = findViewById(R.id.ibBackChat);
        ibDetail =findViewById(R.id.ibDetailGroup);
        ibSendImg = findViewById(R.id.ibSendImg);
        tvGroupName = findViewById(R.id.tvChat);
        civImgGroup = findViewById(R.id.civImgGroup);
        tvGroupId = findViewById(R.id.tvGroupId);
        
        if(!group.getImg().equals(""))
            Picasso.get().load(group.getImg()).into(civImgGroup);
        tvGroupName.setText(group.getName());
        tvGroupId.setText(group.getGid());

        final FirebaseUser curUser = auth.getCurrentUser();
        u.setUid(curUser.getUid());
        u.setEmail(curUser.getEmail());


        db.collection("Users").document(u.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        u = task.getResult().toObject(User.class);
                    }
                });

        db.collection("Groups").document(group.getGid())
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w("TAG", "Listen failed.", error);
                        return;
                    }
                    if(!value.exists()) {
                        group = value.toObject(Group.class);
                        if(!group.getUsers().contains(u.getUid())){
                            finish();
                        }
                    }
                });

        db.collection("Messages")
                .orderBy("time", Query.Direction.ASCENDING)
                .whereEqualTo("gid", group.getGid())
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        return;
                    }
                    if(!value.isEmpty()) {
                        messages = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : value) {
                            Message message = doc.toObject(Message.class);
                            messages.add(message);
                        }
                        displayMessages(messages, u.getUid());
                    }
                });

        ibBack.setOnClickListener(view -> finish());

        ibSend.setOnClickListener(v -> {
            if (!etInput.getText().toString().equals("")) {
                Message message = new Message();
                message.setValue(etInput.getText().toString());
                etInput.setText("");
                message.setGid(group.getGid());
                message.setUid(u.getUid());
                message.setTime(Timestamp.now().toDate());
                db.collection("Messages").add(message).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        message.setKey(task.getResult().getId());
                        db.collection("Messages").document(message.getKey()).update("key", message.getKey());
                        db.collection("Groups").document(group.getGid()).update("lastMsg",message.getValue(),"modTime",message.getTime());
                    }
                });
            }
        });

        ibSendImg.setOnClickListener(v -> {
            ImagePicker.with(this)
                    .crop()
                    .start();
        });

        ibDetail.setOnClickListener(v -> {
            startActivity(new Intent(ChatActivity.this, GroupDetailActivity.class).putExtra("group",group));
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // compare the resultCode with the
            // SELECT_PICTURE constant
            // Get the url of the image from data
            Uri selectedImageUri = data.getData();
            if (null != selectedImageUri) {
                Message message = new Message();
                message.setGid(group.getGid());
                message.setUid(u.getUid());
                message.setTime(Timestamp.now().toDate());
                message.setType("image");
                db.collection("Messages").add(message).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        message.setKey(task.getResult().getId());
                        reference = reference.child("img/messages/"+message.getKey());
                        reference.putFile(selectedImageUri)
                                .addOnCompleteListener(taskSnapshot -> {
                                    reference.getDownloadUrl()
                                            .addOnSuccessListener(uri -> {
                                                db.collection("Messages").document(message.getKey())
                                                        .update("key", message.getKey(),"value", uri.toString());
                                            });
                                });
                        db.collection("Groups").document(group.getGid())
                                .update("lastMsg",u.getName()+" đã gửi 1 ảnh","modTime",message.getTime());
                    }
                });
            }
        }
    }

    private void displayMessages(List<Message> messages, String uid) {
        rvMessage.setHasFixedSize(true);
        rvMessage.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        messageAdapter = new MessageAdapter(ChatActivity.this, messages, db, uid);
        rvMessage.setAdapter(messageAdapter);
        rvMessage.scrollToPosition(messageAdapter.getItemCount() - 1);
    }

}