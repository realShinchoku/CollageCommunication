package com.G12LTUDDD.collagecommunication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.G12LTUDDD.collagecommunication.Adapters.UserAddAdapter;
import com.G12LTUDDD.collagecommunication.Models.Group;
import com.G12LTUDDD.collagecommunication.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserAddActivity extends AppCompatActivity {
    Group group;
    User u;
    FirebaseAuth auth;
    FirebaseFirestore db;
    List<User> users;
    UserAddAdapter userAddAdapter;
    RecyclerView rvUsers;
    ImageButton ibBack;
    SearchView svUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add);
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

        rvUsers = (RecyclerView) findViewById(R.id.rvUsersAdd);
        ibBack = (ImageButton) findViewById(R.id.ibBackUsersAdd);
        svUsers = (SearchView) findViewById(R.id.svUsersAdd);

        ibBack.setOnClickListener(v -> finish());

        db.collection("Groups").document(group.getGid())
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w("TAG", "Listen failed.", error);
                        return;
                    }
                    if (value.exists()) {
                        group = value.toObject(Group.class);
                        db.collection("Users")
                                .addSnapshotListener((value1, error1) -> {
                                    if (error1 != null) {
                                        Log.w("TAG", "Listen failed.", error1);
                                        return;
                                    }
                                    if (!value1.isEmpty()) {
                                        users = new ArrayList<>();
                                        for (QueryDocumentSnapshot doc : value1) {
                                            if (!group.getUsers().contains(doc.getId())) {
                                                User user = doc.toObject(User.class);
                                                users.add(user);
                                            }
                                        }
                                        displayUsers(users, group.getGid());
                                    }
                                });
                    }
                });

        svUsers.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 0) {
                    List<User> searchUsers = new ArrayList<>();
                    for (User user : users) {
                        if (user.getName().toLowerCase().contains(query.toLowerCase())) {
                            searchUsers.add(user);
                        }
                    }
                    displayUsers(searchUsers, group.getGid());
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 2) {
                    List<User> searchUsers = new ArrayList<>();
                    for (User user : users) {
                        if (user.getName().toLowerCase().contains(newText.toLowerCase())) {
                            searchUsers.add(user);
                        }
                    }
                    displayUsers(searchUsers, group.getGid());
                }
                return true;
            }
        });
    }

    private void displayUsers(List<User> users, String gid) {
        rvUsers.setLayoutManager(new LinearLayoutManager(UserAddActivity.this));
        userAddAdapter = new UserAddAdapter(UserAddActivity.this, users, db, gid);
        rvUsers.setAdapter(userAddAdapter);
    }
}