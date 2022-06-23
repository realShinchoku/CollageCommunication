package com.G12LTUDDD.collagecommunication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.G12LTUDDD.collagecommunication.Adapters.UserAdapter;
import com.G12LTUDDD.collagecommunication.Models.Group;
import com.G12LTUDDD.collagecommunication.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class UsersListActivity extends AppCompatActivity {

    Group group;
    User u;
    FirebaseAuth auth;
    FirebaseFirestore db;

    UserAdapter userAdapter;
    RecyclerView rvUsers;
    ImageButton ibBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);
        init();
    }

    void init() {
        Intent i = getIntent();
        group = (Group) i.getExtras().getSerializable("group");

        u = new User();
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        final FirebaseUser curUser = auth.getCurrentUser();
        u.setUid(curUser.getUid());
        u.setEmail(curUser.getEmail());

        rvUsers = (RecyclerView) findViewById(R.id.rvUsers);
        ibBack = (ImageButton) findViewById(R.id.ibBackUsersList);

        ibBack.setOnClickListener(v -> finish());

        db.collection("Users").document(u.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        u = task.getResult().toObject(User.class);
                    }
                });

        db.collection("Groups").document(group.getGid())
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w("TAG", "Listen failed.", error);
                        return;
                    }
                    if (value.exists()) {
                        group = value.toObject(Group.class);
                        displayUsers(group, u.getUid());
                    }
                });
    }

    private void displayUsers(Group group, String uid) {
        rvUsers.setLayoutManager(new LinearLayoutManager(UsersListActivity.this));
        userAdapter = new UserAdapter(UsersListActivity.this, group, db, uid);
        rvUsers.setAdapter(userAdapter);
    }
}